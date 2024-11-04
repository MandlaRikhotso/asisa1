package za.co.oldmutual.asisa.authorize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import za.co.oldmutual.asisa.authorize.mapper.ClaimAuthorizeResponseRowMapper;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.common.bean.NotificationStatusEnum;
import za.co.oldmutual.asisa.common.bean.OperationTypeEnum;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.CustomBeanPropertyRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;

@Component
public class ClaimAuthorizeDAO {

  @Autowired
  private NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  ReferenceDataCache referenceDataCache;

  @Autowired
  InsuredPersonRowMapper insuredPersonRowMapper;

  @Autowired
  MailService mailService;

  @Autowired
  ClaimAuthorizeResponseRowMapper claimAuthorizeResponseRowMapper;

  @Autowired
  UserLoginIdentifier userLoginIdentifier;

 
  private static final Logger LOGGER = LoggerFactory.getLogger(ClaimAuthorizeDAO.class);
  private static final String STATUS = "status";
  private static final String NOTIFICATION_TXN_TYPE = "notificationTxnType";
  private static final String SUCCESS_MESSAGE = "successMessage";
  private static final String NOTIFICATION_ID = "notificationID";


  public Object displayClaimTransaction() throws ResourceNotFoundException {
    try {
      return namedParameterJdbcTemplate.query(
          ClaimAuthorizeDAOQueries.CLAIM_AUTHORIZE_APPROVAL_QUERY,
          new MapSqlParameterSource(STATUS, NotificationStatusEnum.APPROVAL_PENDING.toString()), 
          claimAuthorizeResponseRowMapper);
    } catch (Exception e) {
      LOGGER.error("Fetching pending Claims failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  public Object displayTransactionCount() throws ResourceNotFoundException {
    try {
      return namedParameterJdbcTemplate.queryForObject(
          ClaimAuthorizeDAOQueries.AUTHORIZE_PENDING_COUNT,
          new MapSqlParameterSource(STATUS, NotificationStatusEnum.APPROVAL_PENDING.toString()),
          Integer.class);
    } catch (Exception e) {
      LOGGER.error("Fetching pending Claims and Impairments failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  public Map<String, Object> findClaimApprovalById(String notificationId)
      throws ResourceNotFoundException {
    Map<String, Object> dataMap = new HashMap<>();
    try {
      dataMap.put("note", getNoteByNotificationId(notificationId));
      dataMap.put("transType", getTransTypeNotifiableImpairmentId(notificationId));
      dataMap.put("insuredPerson", getInsuredPersonByNotifiableClaimId(notificationId));
      dataMap.put("old", getOldClaimForNotifiableId(notificationId));
      dataMap.put("new", getNewClaimForNotifiableId(notificationId));
      return dataMap;
    } catch (Exception e) {
      LOGGER.error("Exception occurred : [{}]", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  private List<NoteBean> getNoteByNotificationId(String notificationId) {
    LOGGER.debug("Initiating request to fetch Note by Id");
    return namedParameterJdbcTemplate.query(
        ClaimAuthorizeDAOQueries.FIND_NOTE_BY_NOTIFICATION_ID_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
        new CustomBeanPropertyRowMapper<NoteBean>(NoteBean.class));
  }

  private InsuredPersonBean getInsuredPersonByNotifiableClaimId(String notificationId) {
    LOGGER.debug("Initiating request to fetch InsuredPerson by Id");
    return namedParameterJdbcTemplate
        .query(ClaimAuthorizeDAOQueries.FIND_INSURED_PERSON,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId), insuredPersonRowMapper)
        .get(0);
  }

  private ClaimAuthorizeResponse getOldClaimForNotifiableId(String notificationId) {
    LOGGER.debug("Initiating request to fetch OldNotifiableClaim by Id");
    return namedParameterJdbcTemplate
        .query(ClaimAuthorizeDAOQueries.FIND_EXISTING_CLAIM_BY_ID_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            claimAuthorizeResponseRowMapper)
        .get(0);
  }

  private ClaimAuthorizeResponse getNewClaimForNotifiableId(String notificationId) {
    LOGGER.debug("Initiating request to NewNotifiableClaim by Id");
    return namedParameterJdbcTemplate.query(ClaimAuthorizeDAOQueries.FIND_UPDATED_CLAIM_BY_ID_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId), claimAuthorizeResponseRowMapper)
        .get(0);
  }

  public Map<String, Object> updateClaimApproved(AuthorizeRequest claimAuthorizeRequest)
      throws ResourceUpdationFailedException {
    HashMap<String, Object> responseObject = new HashMap<>();
    try {
      Map<String, Object> namedParameters = new HashMap<>();
      LOGGER.debug("Intiating updation of Approved Claims with details {}", claimAuthorizeRequest);
      // Update APPROVAL_CLAIM
      namedParameters.put(STATUS, NotificationStatusEnum.ACTIVE.toString());
      namedParameters.put(NOTIFICATION_ID, claimAuthorizeRequest.getNotificationID());
      namedParameterJdbcTemplate
          .update(ClaimAuthorizeDAOQueries.CLAIM_AUTHORIZE_UPDATE_APPROVAL_QUERY, namedParameters);
      updateReason(claimAuthorizeRequest.getNotificationID(),
          claimAuthorizeRequest.getActionRemark());
      namedParameters.put(NOTIFICATION_TXN_TYPE, OperationTypeEnum.UPDATE.toString());
      ClaimAuthorizeResponse notifiableClaimAuditBean = fetchNotifiableClaimById(namedParameters);
      notifiableClaimAuditBean.setUpdateReason(claimAuthorizeRequest.getActionRemark());
      notifiableClaimAuditBean.setNotificationStatus(NotificationStatusEnum.APPROVED.toString());
      notifiableClaimAuditBean.setNotificationTxnType(OperationTypeEnum.UPDATE.toString());
      notifiableClaimAuditBean.setIsActive("Y");
      // Audit Claim logs
      insertClaimAudit(notifiableClaimAuditBean);
      responseObject.put(SUCCESS_MESSAGE, "Authorization for Claim Update successfull");
    } catch (Exception e) {
      LOGGER.error("Exception occurred : [{}] ", e.getMessage());
      LOGGER.error("Exception at updateClaimApproved in ClaimAuthorizeDAO : {}",
          claimAuthorizeRequest);
      throw new ResourceUpdationFailedException();
    }
    return responseObject;
  }

  public Map<String, Object> updateClaimReject(AuthorizeRequest claimAuthorizeRequest)
      throws ResourceUpdationFailedException {
    HashMap<String, Object> responseObject = new HashMap<>();
    try {
      // ClaimTransactionApproval claimApproval, String actionRemark
      Map<String, Object> namedParameters = new HashMap<>();
      LOGGER.debug("Intiating rejection of Claim with details [{}]", claimAuthorizeRequest);
      namedParameters.put(STATUS, NotificationStatusEnum.ACTIVE.toString());
      namedParameters.put(NOTIFICATION_ID, claimAuthorizeRequest.getNotificationID());
      namedParameterJdbcTemplate
          .update(ClaimAuthorizeDAOQueries.CLAIM_AUTHORIZE_UPDATE_APPROVAL_QUERY, namedParameters);
      String createdBy = revertOldClaimOnReject(claimAuthorizeRequest.getNotificationID(),
          claimAuthorizeRequest.getActionRemark());
      namedParameters.put(NOTIFICATION_TXN_TYPE, OperationTypeEnum.UPDATE.toString());
      // Fetch Original From Log
      ClaimAuthorizeResponse notifiableClaimAuditBean = fetchNotifiableClaimById(namedParameters);
      namedParameters.put("eventDate", notifiableClaimAuditBean.getEventDate());
      namedParameters.put("placeOfDeath", notifiableClaimAuditBean.getPlaceOfDeath());
      namedParameters.put("deathCertificateNumber",
          notifiableClaimAuditBean.getDeathCertificateNumber());
      namedParameters.put("dha1663Number", notifiableClaimAuditBean.getDha1663Number());
      namedParameters.put("claimReasonCode", notifiableClaimAuditBean.getClaimReasonCode());
      namedParameters.put(NOTIFICATION_ID, claimAuthorizeRequest.getNotificationID());
      namedParameterJdbcTemplate.update(ClaimAuthorizeDAOQueries.CLAIM_UPDATE_QUERY,
          namedParameters);
      notifiableClaimAuditBean.setUpdateReason(claimAuthorizeRequest.getActionRemark());
      notifiableClaimAuditBean.setNotificationStatus(NotificationStatusEnum.REJECTED.toString());
      notifiableClaimAuditBean.setNotificationTxnType(OperationTypeEnum.UPDATE.toString());
      notifiableClaimAuditBean.setIsActive("Y");
      // Audit Claim logs
      insertClaimAudit(notifiableClaimAuditBean);
      responseObject.put(SUCCESS_MESSAGE, "Authorization for Claim Reject successful");
      Map<String, Object> model = new HashMap<>();
	  model.put("reason", claimAuthorizeRequest.getActionRemark());
     mailService.sendEmail(createdBy, OperationTypeEnum.UPDATE_REJECT.toString(), model);
    } catch (Exception e) {
      LOGGER.error("Exception occurred : [{}] ", e.getMessage());
      LOGGER.error("Exception at updateClaimReject in ClaimAuthorizeDAO : {} ",
          claimAuthorizeRequest);
      throw new ResourceUpdationFailedException();
    }
    return responseObject;
  }

  private String revertOldClaimOnReject(String notificationId, String updateReason) {
    LOGGER.debug("Reverting old claim details due to rejection..");
    ClaimAuthorizeResponse claimAuthorizeDetailRequestRowMappers =
        namedParameterJdbcTemplate.query(ClaimAuthorizeDAOQueries.CLAIM_AUDIT_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            claimAuthorizeResponseRowMapper).get(0);
    namedParameterJdbcTemplate.update(ClaimAuthorizeDAOQueries.REVERT_OLD_NOTIFIABLE_CLAIM_QUERY,
        new MapSqlParameterSource("eventDate", claimAuthorizeDetailRequestRowMappers.getEventDate())
            .addValue("placeOfDeath", claimAuthorizeDetailRequestRowMappers.getPlaceOfDeath())
            .addValue("deathCertificateNumber",
                claimAuthorizeDetailRequestRowMappers.getDeathCertificateNumber())
            .addValue("dha1663Number", claimAuthorizeDetailRequestRowMappers.getDha1663Number())
            .addValue("claimReasonCode", claimAuthorizeDetailRequestRowMappers.getClaimReasonCode())
            .addValue("updateReason", updateReason).addValue(NOTIFICATION_ID, notificationId));
    
    return claimAuthorizeDetailRequestRowMappers.getCreatedBy();
  }

  public Map<String, Object> deleteClaimApproved(AuthorizeRequest claimAuthorizeRequest)
      throws ResourceUpdationFailedException {
    HashMap<String, Object> responseObject = new HashMap<>();
    try {
      Map<String, Object> namedParameters = new HashMap<>();
      namedParameters.put(STATUS, NotificationStatusEnum.ACTIVE.toString());
      namedParameters.put("isActive", "N");
      namedParameters.put(NOTIFICATION_ID, claimAuthorizeRequest.getNotificationID());
      namedParameters.put(NOTIFICATION_TXN_TYPE, OperationTypeEnum.DELETE.toString());
      namedParameterJdbcTemplate
          .update(ClaimAuthorizeDAOQueries.CLAIM_AUTHORIZE_DELETE_APPROVAL_QUERY, namedParameters);
      updateReason(claimAuthorizeRequest.getNotificationID(),
          claimAuthorizeRequest.getActionRemark());
      ClaimAuthorizeResponse notifiableClaimAuditBean = fetchNotifiableClaimById(namedParameters);
      notifiableClaimAuditBean.setUpdateReason(claimAuthorizeRequest.getActionRemark());
      notifiableClaimAuditBean.setNotificationStatus(NotificationStatusEnum.APPROVED.toString());
      notifiableClaimAuditBean.setNotificationTxnType(OperationTypeEnum.DELETE.toString());
      notifiableClaimAuditBean.setIsActive("N");
      // Audit Claim logs
      insertClaimAudit(notifiableClaimAuditBean);
      responseObject.put(SUCCESS_MESSAGE, "Authorization for Claim Delete successfull");
     
    } catch (Exception e) {
      LOGGER.error("Exception Occurred : [{}]", e.getMessage());
      LOGGER.error("Exception at deleteClaimApproved in ClaimsAuthorizeDAO : {}",
          claimAuthorizeRequest);
      throw new ResourceUpdationFailedException();
    }
    return responseObject;
  }


  public Map<String, Object> deleteClaimReject(AuthorizeRequest claimAuthorizeRequest)
      throws ResourceUpdationFailedException {
    HashMap<String, Object> responseObject = new HashMap<>();
    try {
      Map<String, Object> namedParameters = new HashMap<>();
      namedParameters.put(STATUS, NotificationStatusEnum.ACTIVE.toString());
      namedParameters.put("isActive", "Y");
      namedParameters.put(NOTIFICATION_ID, claimAuthorizeRequest.getNotificationID());
      namedParameterJdbcTemplate
          .update(ClaimAuthorizeDAOQueries.CLAIM_AUTHORIZE_DELETE_APPROVAL_QUERY, namedParameters);
      updateReason(claimAuthorizeRequest.getNotificationID(),
          claimAuthorizeRequest.getActionRemark());
      namedParameters.put(NOTIFICATION_TXN_TYPE, OperationTypeEnum.DELETE.toString());
      ClaimAuthorizeResponse notifiableClaimAuditBean = fetchNotifiableClaimById(namedParameters);
      notifiableClaimAuditBean.setUpdateReason(claimAuthorizeRequest.getActionRemark());
      notifiableClaimAuditBean.setNotificationStatus(NotificationStatusEnum.REJECTED.toString());
      notifiableClaimAuditBean.setNotificationTxnType(OperationTypeEnum.DELETE.toString());
      notifiableClaimAuditBean.setIsActive("Y");
      // Audit Claim logs
      String createdBy = namedParameterJdbcTemplate.queryForObject(ClaimAuthorizeDAOQueries.GET_CREATED_BY_FOR_CLAIM, new MapSqlParameterSource(NOTIFICATION_ID, claimAuthorizeRequest.getNotificationID()), String.class);
      insertClaimAudit(notifiableClaimAuditBean);
      responseObject.put(SUCCESS_MESSAGE, "Authorization for Claim delete Rejected successful");
      Map<String, Object> model = new HashMap<>();
	  model.put("reason", claimAuthorizeRequest.getActionRemark());
     mailService.sendEmail(createdBy,OperationTypeEnum.DELETE_REJECT.toString(), model);
    } catch (Exception e) {
      LOGGER.error("Exception occurred : [{}]", e.getMessage());
      LOGGER.error("Exception at deleteClaimReject in ClaimsAuthorizeDAO : {}",
          claimAuthorizeRequest);
      throw new ResourceUpdationFailedException();
    }
    return responseObject;
  }

  private void updateReason(String notificationId, String updateReason) {
    LOGGER.debug("Updating reason for editing the claim...");
    namedParameterJdbcTemplate.update(ClaimAuthorizeDAOQueries.UPDATE_REMARK_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId).addValue("updateReason",
            updateReason));
  }

  private ClaimAuthorizeResponse fetchNotifiableClaimById(Map<String, Object> namedParameters) {
    LOGGER.debug("Fetching claim by notificationId...");
    return namedParameterJdbcTemplate
        .query(ClaimAuthorizeDAOQueries.FETCH_APPROVAL_CLAIM_AUDIT_QUERY, namedParameters,
            claimAuthorizeResponseRowMapper)
        .get(0);
  }

  private void insertClaimAudit(ClaimAuthorizeResponse notifiableClaimAuditBean) {
    LOGGER.debug("Inserting claim details in audit...");
    notifiableClaimAuditBean.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
    notifiableClaimAuditBean.setClaimReasonCode(notifiableClaimAuditBean.getClaimReason().stream()
        .map(ImpairmentCodeBean::getCode).collect(Collectors.joining(",")));
    namedParameterJdbcTemplate.update(ClaimAuthorizeDAOQueries.INSERT_NOTIFIABLE_CLAIM_AUDIT_QUERY,
        new BeanPropertySqlParameterSource(notifiableClaimAuditBean));
  }

  private String getTransTypeNotifiableImpairmentId(String notificationId) {
    return namedParameterJdbcTemplate.queryForObject(
        ClaimAuthorizeDAOQueries.FETCH_TRANSCATION_TYPE_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId), String.class);
  }
}
