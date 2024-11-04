package za.co.oldmutual.asisa.authorize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import za.co.oldmutual.asisa.authorize.mapper.ImpairmentAuthorizeResponseRowMapper;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.common.bean.NotificationStatusEnum;
import za.co.oldmutual.asisa.common.bean.OperationTypeEnum;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.CustomBeanPropertyRowMapper;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentAuditBean;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

@Component
public class ImpairmentAuthorizeDao {

  @Autowired
  private NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  ReferenceDataCache referenceDataCache;

  @Autowired
  InsuredPersonRowMapper insuredPersonRowMapper;

  @Autowired
  ImpairmentAuthorizeResponseRowMapper impairmentAuthorizeResponseRowMapper;

  @Autowired
  UserLoginIdentifier userLoginIdentifier;
  
  @Autowired
  MailService mailService;

  private static final Logger LOGGER = LoggerFactory.getLogger(ImpairmentAuthorizeDao.class);
  private static final String SUCCESS_MESSAGE = "successMessage";
  private static final String NOTIFICATION_ID = "notificationID";

  public Object getAllPendingImpairments() throws ResourceNotFoundException {
    try {
      return namedParameterJdbcTemplate.query(
          ImpairmentAuthorizeDaoQueries.IMPAIRMENT_AUTHORIZE_REQUEST_QUERY,
          impairmentAuthorizeResponseRowMapper);
    } catch (Exception e) {
      LOGGER.error("Fetching pending impairments failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  public Object getImpairmentsForNotifiableImpairmentId(String notificationId)
      throws ResourceNotFoundException {
    try {
      Map<String, Object> impairmentDetails = new HashMap<>();
      impairmentDetails.put("note", getNoteByNotificationId(notificationId));
      impairmentDetails.put("transType", getTransTypeNotifiableImpairmentId(notificationId));
      impairmentDetails.put("insuredPerson",
          getInsuredPersonByNotifiableImpairmentId(notificationId));
      impairmentDetails.put("old", getOldImpairmentsForNotifiableImpairmentId(notificationId));
      impairmentDetails.put("new", getNewImpairmentsForNotifiableImpairmentId(notificationId));
      return impairmentDetails;
    } catch (Exception e) {
      LOGGER.error("Fetching impairment details failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  private List<NoteBean> getNoteByNotificationId(String notificationId) {
    return namedParameterJdbcTemplate.query(
        ImpairmentAuthorizeDaoQueries.FIND_NOTE_BY_NOTIFICATION_ID_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
        new CustomBeanPropertyRowMapper<NoteBean>(NoteBean.class));
  }

  private String getTransTypeNotifiableImpairmentId(String notificationId) {
    return namedParameterJdbcTemplate.queryForObject(
        ImpairmentAuthorizeDaoQueries.FETCH_NOTIFICATION_TRANSACTION_TYPE_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId), String.class);
  }

  public Object authorizeUpdate(AuthorizeRequest approvedImpairmentRequest)
      throws ResourceUpdationFailedException {
    Map<String, Object> responseObject = new HashMap<>();
    try {
      if (approvedImpairmentRequest.getAction().equals(NotificationStatusEnum.APPROVE.toString())) {
        LOGGER.debug("Approving Update impairment request with details : {}",
            approvedImpairmentRequest);
        updateNotification(approvedImpairmentRequest, NotificationStatusEnum.ACTIVE.toString(), "Y",
            OperationTypeEnum.UPDATE.toString());
        updateReason(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getActionRemark());
        insertNotifiableImpairmentForAudit(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getTransType(), NotificationStatusEnum.APPROVED.toString(),
            "Y");
        responseObject.put(SUCCESS_MESSAGE, "Impairment updation approved successfully ");
      } else if (approvedImpairmentRequest.getAction()
          .equals(NotificationStatusEnum.REJECT.toString())) {
        LOGGER.debug("Rejecting Update impairment request with details : {}",
            approvedImpairmentRequest);
        updateNotification(approvedImpairmentRequest, NotificationStatusEnum.ACTIVE.toString(), "Y",
            OperationTypeEnum.UPDATE.toString());
        updateReason(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getActionRemark());
        String createdBy = revertOldImpairmentOnReject(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getActionRemark());
        insertNotifiableImpairmentForAudit(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getTransType(), NotificationStatusEnum.REJECTED.toString(),
            "Y");
        responseObject.put(SUCCESS_MESSAGE, "Impairment updation rejected successfully ");
        Map<String, Object> model = new HashMap<>();
  	  model.put("reason", approvedImpairmentRequest.getActionRemark());
       mailService.sendEmail(createdBy, OperationTypeEnum.UPDATE_REJECT.toString(), model);
      } else {
        LOGGER.error("Exception occurred : Invalid data received..");
        throw new InvalidDataException();
      }
      return responseObject;
    } catch (Exception e) {
      LOGGER.error("Exception Occurred at authorizeUpdate in ImpairmentAuthorizeDao : {} ",
          e.getMessage());
      throw new ResourceUpdationFailedException();
    }
  }

  public Object authorizeDelete(AuthorizeRequest approvedImpairmentRequest)
      throws ResourceUpdationFailedException {
    Map<String, Object> responseObject = new HashMap<>();
    try {
      if (approvedImpairmentRequest.getAction().equals(NotificationStatusEnum.APPROVE.toString())) {
        LOGGER.debug("Approving delete impairment request with details : {}",
            approvedImpairmentRequest);
        updateNotification(approvedImpairmentRequest, NotificationStatusEnum.ACTIVE.toString(), "N",
            OperationTypeEnum.DELETE.toString());
        updateReason(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getActionRemark());
        insertNotifiableImpairmentForAudit(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getTransType(), NotificationStatusEnum.APPROVED.toString(),
            "N");
        responseObject.put(SUCCESS_MESSAGE, "Impairment deletion approved successfully ");
      } else if (approvedImpairmentRequest.getAction()
          .equals(NotificationStatusEnum.REJECT.toString())) {
        LOGGER.debug("Rejecting delete impairment request with details : {}",
            approvedImpairmentRequest);
        updateNotification(approvedImpairmentRequest, NotificationStatusEnum.ACTIVE.toString(), "Y",
            OperationTypeEnum.DELETE.toString());
        updateReason(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getActionRemark());
        String createdBy = namedParameterJdbcTemplate.queryForObject(ImpairmentAuthorizeDaoQueries.GET_CREATED_BY_FOR_IMPAIRMENT, new MapSqlParameterSource(NOTIFICATION_ID, approvedImpairmentRequest.getNotificationID()), String.class);
        insertNotifiableImpairmentForAudit(approvedImpairmentRequest.getNotificationID(),
            approvedImpairmentRequest.getTransType(), NotificationStatusEnum.REJECTED.toString(),
            "Y");
        responseObject.put(SUCCESS_MESSAGE, "Impairment deletion rejected successfully ");
        Map<String, Object> model = new HashMap<>();
  	  model.put("reason", approvedImpairmentRequest.getActionRemark());
       mailService.sendEmail(createdBy, OperationTypeEnum.DELETE_REJECT.toString(), model);
      } else {
        LOGGER.error("Exception occurred : Invalid data received..");
        throw new InvalidDataException();
      }
      return responseObject;
    } catch (Exception e) {
      LOGGER.error("Exception Occurred at authorizeDelete in ImpairmentAuthorizeDao : {}",
          e.getMessage());
      throw new ResourceUpdationFailedException();
    }
  }

  private InsuredPersonBean getInsuredPersonByNotifiableImpairmentId(String notificationId) {
    return namedParameterJdbcTemplate
        .query(ImpairmentAuthorizeDaoQueries.FETCH_INSURED_PERSON_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            new InsuredPersonRowMapper(referenceDataCache))
        .get(0);
  }

  private ImpairmentAuthorizeResponse getOldImpairmentsForNotifiableImpairmentId(
      String notificationId) {
    return namedParameterJdbcTemplate
        .query(ImpairmentAuthorizeDaoQueries.FETCH_OLD_NOTIFIABLE_IMPAIRMENT_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            impairmentAuthorizeResponseRowMapper)
        .get(0);
  }

  private ImpairmentAuthorizeResponse getNewImpairmentsForNotifiableImpairmentId(
      String notificationId) {
    return namedParameterJdbcTemplate
        .query(ImpairmentAuthorizeDaoQueries.FETCH_NEW_NOTIFIABLE_IMPAIRMENT_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            impairmentAuthorizeResponseRowMapper)
        .get(0);
  }

  private void updateNotification(AuthorizeRequest approvedImpairmentRequest,
      String notificationStatusCode, String isActive, String transType) {
    LOGGER.debug("Updating notification..");
    namedParameterJdbcTemplate.update(ImpairmentAuthorizeDaoQueries.UPDATE_NOTIFICATION_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, approvedImpairmentRequest.getNotificationID())
            .addValue("notificationStatusCode", notificationStatusCode)
            .addValue("isActive", isActive).addValue("transType", transType));
  }

  private String revertOldImpairmentOnReject(String notificationId, String updateReason) {
    LOGGER.debug("Reverting old impairment details on rejection..");
    ImpairmentAuthorizeResponse impairmentAuthorizeDetailRequestRowMappers =
        namedParameterJdbcTemplate.query(ImpairmentAuthorizeDaoQueries.IMPAIRMENT_AUDIT_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            impairmentAuthorizeResponseRowMapper).get(0);
    namedParameterJdbcTemplate.update(
        ImpairmentAuthorizeDaoQueries.REVERT_OLD_NOTIFIABLE_IMPAIRMENT_QUERY,
        new MapSqlParameterSource("timeSignal",
            impairmentAuthorizeDetailRequestRowMappers.getTimeSignal())
                .addValue("readings", impairmentAuthorizeDetailRequestRowMappers.getReadings())
                .addValue("updateReason", updateReason).addValue(NOTIFICATION_ID, notificationId));
    return impairmentAuthorizeDetailRequestRowMappers.getCreatedBy();
  }

  private void updateReason(String notificationId, String updateReason) {
    LOGGER.debug("Updating reason fo editing impairment..");
    namedParameterJdbcTemplate.update(ImpairmentAuthorizeDaoQueries.UPDATE_REMARK_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationId).addValue("updateReason",
            updateReason));
  }

  private void insertNotifiableImpairmentForAudit(String notificationId, String transType,
      String notifiableStatusCode, String isActive) {
    LOGGER.debug("Inserting NotifiableImpairment in Audit..");
    ImpairmentAuthorizeResponse notifiableImpairmentBean =
        namedParameterJdbcTemplate.query(ImpairmentAuthorizeDaoQueries.FETCH_IMPAIRMENT_QUERY,
            new MapSqlParameterSource(NOTIFICATION_ID, notificationId),
            impairmentAuthorizeResponseRowMapper).get(0);
    NotifiableImpairmentAuditBean notifiableImpairmentAuditBean =
        new NotifiableImpairmentAuditBean();
    notifiableImpairmentAuditBean.setNotificationTxnType(transType);
    notifiableImpairmentAuditBean.setIsActive(isActive);
    notifiableImpairmentAuditBean.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
    notifiableImpairmentAuditBean.setCreatedDate(new Date());
    notifiableImpairmentAuditBean.setAstuteRefNo("");
    notifiableImpairmentAuditBean.setNotifiableStatusCode(notifiableStatusCode);
    notifiableImpairmentAuditBean.setNotificationID(notifiableImpairmentBean.getNotificationID());
    notifiableImpairmentAuditBean.setTimeSignal(notifiableImpairmentBean.getTimeSignal());
    notifiableImpairmentAuditBean.setReadings(notifiableImpairmentBean.getReadings());
    notifiableImpairmentAuditBean.setUpdateReason(notifiableImpairmentBean.getReasonForEdit());
    notifiableImpairmentAuditBean.setImpairment(notifiableImpairmentBean.getImpairment());
    notifiableImpairmentAuditBean
        .setSpecialInvestigation(notifiableImpairmentBean.getSpecialInvestigation());
    notifiableImpairmentAuditBean.setSymbol(notifiableImpairmentBean.getSymbol());
    notifiableImpairmentAuditBean
        .setSpecialInvestigationcode(notifiableImpairmentBean.getSpecialInvestigationcode());
    notifiableImpairmentAuditBean.setSymbolcode(notifiableImpairmentBean.getSymbolcode());
    namedParameterJdbcTemplate.update(
        ImpairmentAuthorizeDaoQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY,
        new BeanPropertySqlParameterSource(notifiableImpairmentAuditBean));
  }
}
