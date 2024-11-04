package za.co.oldmutual.asisa.claims;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import za.co.oldmutual.asisa.authorize.ClaimAuthorizeController;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.common.bean.NotificationStatusEnum;
import za.co.oldmutual.asisa.common.bean.OperationTypeEnum;
import za.co.oldmutual.asisa.common.dao.InsuredPersonDAO;
import za.co.oldmutual.asisa.common.dao.NoteDAO;
import za.co.oldmutual.asisa.common.dao.NotificationDAO;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;

@Component
public class NotifiableClaimsDAO {

	private static final Logger logger = LoggerFactory.getLogger(ClaimAuthorizeController.class);

	@Autowired
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Autowired
	IPEnquiryDAO insuredPersonEnquiryDAO;

	@Autowired
	InsuredPersonDAO insuredPersonDAO;

	@Autowired
	NotificationDAO notificationDAO;

	@Autowired
	NoteDAO noteDAO;

	@Autowired
	MailService mailService;

	@Autowired
	UserLoginIdentifier userLoginIdentifier;

	private static final String SUCCESS_MESSAGE = "successMessage";

	private String insuredPersonID;
		
	@Autowired
	NotifiableClaimBeanRowMapper notifiableClaimBeanRowMapper;
	
	@Autowired
	RefDataDAO refDataDAO;

	@Transactional
	public Map<String, Object> addNotifiableClaims(NotifiableClaimsRequest notifiableClaimsRequest)
			throws ResourceUpdationFailedException {
		Map<String, Object> responseObject = new HashMap<>();
		boolean insuredPersonExist = true;
		try {
			logger.debug("Initiating search for InsuredPerson...");
			insuredPersonID = insuredPersonEnquiryDAO.getInsuredPersonID(
					insuredPersonDAO.getIPSearchCriteria(notifiableClaimsRequest.getInsuredPerson()));
			String source = refDataDAO.getOffice("98:57");
			if (insuredPersonID.equalsIgnoreCase("0")) {
				insuredPersonExist = false;
				logger.debug("InsuredPerson not found");
				notifiableClaimsRequest.getInsuredPerson().setSource(source);
				insuredPersonID = insuredPersonDAO.insertInsuredPerson(notifiableClaimsRequest.getInsuredPerson());
			}
			addClaimFunctionality(notifiableClaimsRequest, responseObject, insuredPersonExist,source);
		} catch (Exception e) {
			logger.error("Exception Occurred while adding NotifiableClaim  : {}", e.getMessage());
			logger.error("action = addNotifiableClaims in NotifiableClaimsDao with = {}", notifiableClaimsRequest);

			throw new ResourceUpdationFailedException();
		}
		return responseObject;
	}

	private void addClaimFunctionality(NotifiableClaimsRequest notifiableClaimsRequest,
			Map<String, Object> responseObject, boolean insuredPersonExist,String source) {
		if (!(notifiableClaimsRequest.getNotifications().get(0).getNotifiableClaims().isEmpty())) {
			logger.debug("InsuredPerson already exists..");
			for (NotifiableClaimBean notifiableClaimBean : notifiableClaimsRequest.getNotifications().get(0).getNotifiableClaims()) {
				checkMandatoryFields(notifiableClaimsRequest, notifiableClaimBean, responseObject, source);
			}
		} else {

			notifiableClaimsRequest.getNotifications().forEach(notification -> notification.getNotes().forEach(note -> {
				note.setPersonID(insuredPersonID);
				noteDAO.insertScratchpad(note);
			}));

			responseObject.put(SUCCESS_MESSAGE,
					(insuredPersonExist) ? "Scratchpad saved successfully" : "Insured Person saved successfully");
		}
	}

	private void checkMandatoryFields(NotifiableClaimsRequest notifiableClaimsRequest, NotifiableClaimBean notifiableClaimBean, Map<String, Object> responseObject, String source) {
		if ((!notifiableClaimsRequest.getNotifications().get(0).getPolicyBenefit().getCode().isEmpty())
				&& (!notifiableClaimBean.getClaimCategory().getCode().isEmpty())
				&& (!notifiableClaimBean.getEventDate().isEmpty())
				&& (!notifiableClaimsRequest.getNotifications().get(0).getPolicyNumber().isEmpty())) {
			if ((notifiableClaimsRequest.getNotifications().get(0).getPolicyBenefit().getDescription()
					.equalsIgnoreCase("RISK/DEATH BENEFIT"))
					|| (notifiableClaimsRequest.getNotifications().get(0).getPolicyBenefit().getDescription()
							.equalsIgnoreCase("FUNERAL POLICY"))
					|| (notifiableClaimsRequest.getNotifications().get(0).getPolicyBenefit().getDescription()
							.equalsIgnoreCase("OTHER"))) {

				if ((!notifiableClaimBean.getEventDeathPlace().isEmpty())
						&& (!notifiableClaimBean.getEventCause().getCode().isEmpty())) {
					addClaim(notifiableClaimsRequest, responseObject,source);
				} else {
					responseObject.put(SUCCESS_MESSAGE,
							"Validation failed for mandatory fields for RISK/DEATH BENEFIT, FUNERAL POLICY and OTHER ");
				}

			} else {
				addClaim(notifiableClaimsRequest, responseObject,source);
			}
		} else {
			responseObject.put(SUCCESS_MESSAGE, "Validation failed for mandatory fields");
		}
	}
	
	private void addClaim(NotifiableClaimsRequest notifiableClaimsRequest, Map<String, Object> responseObject,String source) {
		for (ClaimNotificationBean notification : notifiableClaimsRequest.getNotifications()) {
			notification.setInsuredPersonID(insuredPersonID);
			for (NotifiableClaimBean notifiableClaim : notification.getNotifiableClaims()) {
				notification.setInsuredPersonID(insuredPersonID);
				notification.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
				notification.setNotificationSourceCode(source);
				notification.setNotificationStatusCode(NotificationStatusEnum.ACTIVE.toString());
				notification.setNotificationTxnType(OperationTypeEnum.CREATE.toString());
				notification.setNotificationTypeCode("CLAIM");
				String notificationID = notificationDAO.insertClaimNotification(notification);
				notifiableClaim.setNotificationID(notificationID);
				insertNotifiableClaim(notifiableClaim);
				insertNotifiableClaimForAudit(notifiableClaim, OperationTypeEnum.CREATE.toString(),
						NotificationStatusEnum.ACTIVE.toString(), "Y");
				for (NoteBean note : notification.getNotes()) {
					note.setNotificationID(notificationID);
					note.setPersonID(insuredPersonID);
					noteDAO.insertNote(note);
				}
			}
		}
		responseObject.put(SUCCESS_MESSAGE, "Claims added successfully");
	}

	@Transactional
	public Map<String, Object> updateNotifiableClaim(UpdateNotifiableClaimRequest updateNotifiableClaimRequest)
			throws ResourceUpdationFailedException {
		logger.debug("Checking if request is for Update or Delete");
		String reasonCode = updateNotifiableClaimRequest.getClaimReason().stream().map(ImpairmentCodeBean::getCode)
				.collect(Collectors.joining(","));
		updateNotifiableClaimRequest.setClaimReasonCode(reasonCode);
		Map<String, Object> responseObject = new HashMap<>();
		String notificationTxnTypeCode;
		String notificationStatusCode;
		String isActive;
		String createdBy;
		NotifiableClaimBean existingNotifiableClaimBean = null;
		if (updateNotifiableClaimRequest.isDelete()) {
			return deleteNotifiableClaim(updateNotifiableClaimRequest);
		} else {
			// 1.calculate/populate notification TRX type, Status and isActive
			notificationTxnTypeCode = OperationTypeEnum.UPDATE.toString();
			notificationStatusCode = NotificationStatusEnum.ACTIVE.toString();
			isActive = "Y";
			createdBy = userLoginIdentifier.fetchUserDetails();
			// 2. update notification, notifiableImpairment and add notes/scratchpad
			try {
				logger.debug("Updating NotifiableClaim..");
				existingNotifiableClaimBean = fetchNewNotifiableClaim(updateNotifiableClaimRequest).get(0);

				
				if (!StringUtils.isEmpty(existingNotifiableClaimBean)) {
boolean authorizeStatus=checkForAuthorization(updateNotifiableClaimRequest, false,
		existingNotifiableClaimBean);
                    if(authorizeStatus) {
					notificationStatusCode = NotificationStatusEnum.APPROVAL_PENDING.toString();
                    }
                    
					boolean unAuthorizeStatus=checkForUnauthorizedFieldUpdation(updateNotifiableClaimRequest,  existingNotifiableClaimBean);
					if(authorizeStatus && unAuthorizeStatus ) {
						updateClaimFunctionality(updateNotifiableClaimRequest, notificationTxnTypeCode,
								notificationStatusCode, isActive, createdBy, existingNotifiableClaimBean);
						responseObject.put(SUCCESS_MESSAGE, "Claims updated successfully");
						}
					
					else if(authorizeStatus || unAuthorizeStatus ) {
						updateClaimFunctionality(updateNotifiableClaimRequest, notificationTxnTypeCode,
								notificationStatusCode, isActive, createdBy, existingNotifiableClaimBean);
						responseObject.put(SUCCESS_MESSAGE, "Claims updated successfully");
						}
					else {
						if(!(updateNotifiableClaimRequest.getNotes().isEmpty())) {
							insertNote(updateNotifiableClaimRequest);
							responseObject.put(SUCCESS_MESSAGE, "Notes Or Scratchpad inserted successfully!");
						}
						responseObject.put(SUCCESS_MESSAGE, "Value for update claim is not changed");
					}
					
				} else {
					throw new ResourceNotFoundException();
				}
				if (notificationStatusCode.equals(NotificationStatusEnum.APPROVAL_PENDING.toString())) {
					Map<String, Object> model = new HashMap<>();
					model.put("reason", updateNotifiableClaimRequest.getUpdateReason());
					mailService.sendEmail("XY57850", OperationTypeEnum.UPDATE.toString(),
							model);
				}
			} catch (Exception e) {
				logger.error("Exception Occurred while updatingNotifiableClaim : [{}]", e.getMessage());
				logger.error("action = updateNotifiableClaim in NotifiableClaimsDao : {}",
						updateNotifiableClaimRequest);
				throw new ResourceUpdationFailedException();
			}
			return responseObject;
		}
	}

	private void updateClaimFunctionality(UpdateNotifiableClaimRequest updateNotifiableClaimRequest,
			String notificationTxnTypeCode, String notificationStatusCode, String isActive, String createdBy,
			NotifiableClaimBean existingNotifiableClaimBean) {
		updateExistingNotifiableClaim(updateNotifiableClaimRequest);
		updateNotifiableClaimRequest.setNotificationID(existingNotifiableClaimBean.getNotificationID());
		setValuesForAuthorization(updateNotifiableClaimRequest, existingNotifiableClaimBean);
		notificationDAO.updateNotification(updateNotifiableClaimRequest.getNotificationID(),
				notificationTxnTypeCode, notificationStatusCode, createdBy, isActive);
		insertNotifiableClaimForAudit(existingNotifiableClaimBean, notificationTxnTypeCode,
				notificationStatusCode, isActive);
		insertNote(updateNotifiableClaimRequest);
		
	}

	public void insertNote(UpdateNotifiableClaimRequest updateNotifiableClaimRequest) {
		for (NoteBean note : updateNotifiableClaimRequest.getNotes()) {
			note.setNotificationID(updateNotifiableClaimRequest.getNotificationID());
			note.setPersonID(insuredPersonEnquiryDAO
					.getInsuredPersonByNotificationID(updateNotifiableClaimRequest.getNotificationID()));
			noteDAO.insertNote(note);
		}
	}
	
	private void setValuesForAuthorization(UpdateNotifiableClaimRequest updateNotifiableClaimRequest,
			NotifiableClaimBean existingNotifiableClaimBean) {
		logger.debug("Setting updated values for authorization..");
		existingNotifiableClaimBean.setUpdateReason(updateNotifiableClaimRequest.getUpdateReason());
		existingNotifiableClaimBean.setEventDate(updateNotifiableClaimRequest.getEventDate());
		existingNotifiableClaimBean.setEventDeathPlace(updateNotifiableClaimRequest.getEventDeathPlace());
		existingNotifiableClaimBean.setEventCause(updateNotifiableClaimRequest.getEventCause());
		existingNotifiableClaimBean
				.setEventDeathCertificateNo(updateNotifiableClaimRequest.getEventDeathCertificateNo());
		existingNotifiableClaimBean.setDha1663Number(updateNotifiableClaimRequest.getDha1663Number());
		existingNotifiableClaimBean.setClaimReason(updateNotifiableClaimRequest.getClaimReason());
		existingNotifiableClaimBean.setClaimReasonCode(updateNotifiableClaimRequest.getClaimReasonCode());
		existingNotifiableClaimBean.setClaimStatus(updateNotifiableClaimRequest.getClaimStatus());
		existingNotifiableClaimBean.setPaymentMethod(updateNotifiableClaimRequest.getPaymentMethod());
	}
	
	private boolean checkForUnauthorizedFieldUpdation(UpdateNotifiableClaimRequest updateNotifiableClaimRequest,
			   NotifiableClaimBean existingNotifiableClaimBean) {
			 boolean result = false;
			 if(!updateNotifiableClaimRequest.getClaimReason().isEmpty())
			 {
			  
			  Collections.sort((List) updateNotifiableClaimRequest.getClaimReason());
			  Collections.sort((List) existingNotifiableClaimBean.getClaimReason());
			 }
			 if(!StringUtils.isEmpty(existingNotifiableClaimBean.getEventCause().getCode()) && !StringUtils.isEmpty(updateNotifiableClaimRequest.getEventCause().getCode())) {
			  if(existingNotifiableClaimBean.getEventCause().getCode().equals(updateNotifiableClaimRequest.getEventCause().getCode()))
			  {
			   result=false;
			  }
			  else
			  {
			   result=true;
			  }
			 }
			 if(!StringUtils.isEmpty(existingNotifiableClaimBean.getClaimStatus().getCode()) && !StringUtils.isEmpty(updateNotifiableClaimRequest.getClaimStatus().getCode())) {
			  if(existingNotifiableClaimBean.getClaimStatus().getCode().equals(updateNotifiableClaimRequest.getClaimStatus().getCode()))
			  {
			   result=false;
			  }
			  else
			  {
			   result=true;
			  }
			 }
			 if(!StringUtils.isEmpty(existingNotifiableClaimBean.getPaymentMethod().getCode()) && !StringUtils.isEmpty(updateNotifiableClaimRequest.getPaymentMethod().getCode())) {
			  if(existingNotifiableClaimBean.getPaymentMethod().getCode().equals(updateNotifiableClaimRequest.getPaymentMethod().getCode()))
			  {
			   result=false;
			  }
			  else
			  {
			   result=true;
			  }
			 }
			 
			 if(updateNotifiableClaimRequest.getClaimReason().equals(existingNotifiableClaimBean.getClaimReason())) {
			  result = true;
			 }
			 return result;

			}

	private Boolean checkForAuthorization(UpdateNotifiableClaimRequest updateNotifiableClaimRequest,
			boolean notificationStatus, NotifiableClaimBean existingNotifiableClaimBean) throws ParseException {
		logger.debug("Checking if update requires Authorization...");
		boolean status = false;

		if (!(StringUtils.isEmpty(existingNotifiableClaimBean.getEventDate())
				&& StringUtils.isEmpty(updateNotifiableClaimRequest.getEventDate()))) {
			if (!StringUtils.isEmpty(existingNotifiableClaimBean.getEventDate())) {
				if (!updateNotifiableClaimRequest.getEventDate().equals(formatDate(
						new SimpleDateFormat("yyyy-MM-dd").parse(existingNotifiableClaimBean.getEventDate())))) {
					status = true;
				}
			} else {
				status = true;
			}
		}

		if (status
				|| (!updateNotifiableClaimRequest.getEventDeathPlace()
						.equals(existingNotifiableClaimBean.getEventDeathPlace()))
				|| (!updateNotifiableClaimRequest.getEventDeathCertificateNo()
						.equals(existingNotifiableClaimBean.getEventDeathCertificateNo()))
				|| (!updateNotifiableClaimRequest.getDha1663Number()
						.equals(existingNotifiableClaimBean.getDha1663Number()))
				|| (!updateNotifiableClaimRequest.getClaimReasonCode()
						.equals(existingNotifiableClaimBean.getClaimReasonCode()))) {
			notificationStatus = true;
		}

		return notificationStatus;
	}

	
	@Transactional
	public Map<String, Object> deleteNotifiableClaim(UpdateNotifiableClaimRequest updateNotifiableClaimRequest) {
		logger.debug("Deleting NotifiableClaim with details [{}]", updateNotifiableClaimRequest);
		Map<String, Object> responseObject = new HashMap<>();
		String notificationTxnTypeCode;
		String notificationStatusCode;
		String isActive;
		String notificationID = "";
		String createdBy;
		// 1.calculate/populate notification TRX type, Status and isActive
		notificationTxnTypeCode = OperationTypeEnum.DELETE.toString();
		notificationStatusCode = NotificationStatusEnum.APPROVAL_PENDING.toString();
		isActive = "N";
		createdBy = userLoginIdentifier.fetchUserDetails();
		// 2. delete notification, notifiableImpairment and add notes/scratchpad
		try {
			List<NotifiableClaimBean> claimList = fetchNewNotifiableClaim(updateNotifiableClaimRequest);
			if (!claimList.isEmpty()) {
				notificationID = claimList.get(0).getNotificationID();
			}
			notificationDAO.updateNotification(updateNotifiableClaimRequest.getNotificationID(),
					notificationTxnTypeCode, notificationStatusCode, createdBy, isActive);
			updateExistingNotifiableClaim(updateNotifiableClaimRequest);
			NotifiableClaimBean notifiableClaimBean = claimList.get(0);
			notifiableClaimBean.setUpdateReason(updateNotifiableClaimRequest.getUpdateReason());
			insertNotifiableClaimForAudit(notifiableClaimBean, notificationTxnTypeCode, notificationStatusCode,
					isActive);
			responseObject.put(SUCCESS_MESSAGE, "Claims deleted successfully");
			if (notificationStatusCode.equals(NotificationStatusEnum.APPROVAL_PENDING.toString())) {
				Map<String, Object> model = new HashMap<>();
				model.put("reason", updateNotifiableClaimRequest.getUpdateReason());
				mailService.sendEmail("XY57850",OperationTypeEnum.DELETE.toString(), model);
			}
		} catch (Exception e) {
			logger.info("Exception Occurred as {}", e.getMessage());
			logger.info("action = updateNotifiableClaim NotifiableClaimsDao = {}", notificationID);
			logger.error("Exception Occurred while deleting NotifiableClaim : {} ", e.getMessage());
			logger.error("action = deleteNotifiableClaim in NotifiableClaimsDao with notificationId = {}",
					notificationID);
		}
		return responseObject;
	}

	private List<NotifiableClaimBean> fetchNewNotifiableClaim(
			UpdateNotifiableClaimRequest updateNotifiableClaimRequest) {
		return namedParameterJdbcTemplate.query(NotifiableClaimsDAOQueries.FETCH_NOTIFIABLE_CLAIM_QUERY,
				new MapSqlParameterSource("notificationID", updateNotifiableClaimRequest.getNotificationID()),
				notifiableClaimBeanRowMapper);

	}

	private void insertNotifiableClaim(NotifiableClaimBean notifiableClaim) {
		logger.debug("Inserting NotifiableClaim with details [{}]", notifiableClaim);
		notifiableClaim.setClaimReasonCode(notifiableClaim.getClaimReason().stream().map(ImpairmentCodeBean::getCode)
				.collect(Collectors.joining(",")));
		notifiableClaim.setClaimCategoryCode(notifiableClaim.getClaimCategory().getCode());
		namedParameterJdbcTemplate.update(NotifiableClaimsDAOQueries.INSERT_NOTIFIABLE_CLAIM_QUERY,
				new BeanPropertySqlParameterSource(notifiableClaim));
	}

	private void updateExistingNotifiableClaim(UpdateNotifiableClaimRequest updateNotifiableClaimRequest) {
		logger.debug("Updating NotifiableClaim with details [{}]", updateNotifiableClaimRequest);
		String updateNotifiableClaimQuery;
		if (updateNotifiableClaimRequest.isDelete()) {
			updateNotifiableClaimQuery = NotifiableClaimsDAOQueries.UPDATE_REASON_QUERY;
		} else {
			updateNotifiableClaimQuery = NotifiableClaimsDAOQueries.UPDATE_NOTIFIABLE_CLAIM_QUERY;

		}
		namedParameterJdbcTemplate.update(updateNotifiableClaimQuery,
				new BeanPropertySqlParameterSource(updateNotifiableClaimRequest));
	}

	private void insertNotifiableClaimForAudit(NotifiableClaimBean notifiableClaim, String notificationTxnTypeCode,
			String notificationStatusCode, String isActive) {
		logger.debug("Inserting NotifiableClaimForAudit...");
		NotifiableClaimAuditBean notifiableClaimAuditBean = new NotifiableClaimAuditBean();
		notifiableClaimAuditBean.setNotificationStatus(notificationStatusCode);
		notifiableClaimAuditBean.setNotificationSource("OM");
		notifiableClaimAuditBean.setNotificationTxnType(notificationTxnTypeCode);
		notifiableClaimAuditBean.setUpdateReason(notifiableClaim.getUpdateReason());
		notifiableClaimAuditBean.setAstuteRefNo("");
		notifiableClaimAuditBean.setIsActive(isActive);
		notifiableClaimAuditBean.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
		notifiableClaimAuditBean.setCreatedDate(new Date());
		notifiableClaimAuditBean.setNotificationID(notifiableClaim.getNotificationID());
		notifiableClaimAuditBean.setClaimCategory(notifiableClaim.getClaimCategory());
		if (!notifiableClaim.getEventDate().equals(""))
			notifiableClaimAuditBean.setEventDate(notifiableClaim.getEventDate());

		notifiableClaimAuditBean.setEventCause(notifiableClaim.getEventCause());
		notifiableClaimAuditBean.setEventDeathPlace(notifiableClaim.getEventDeathPlace());
		notifiableClaimAuditBean.setEventDeathCertificateNo(notifiableClaim.getEventDeathCertificateNo());
		notifiableClaimAuditBean.setDha1663Number(notifiableClaim.getDha1663Number());
		notifiableClaimAuditBean.setClaimReason(notifiableClaim.getClaimReason());
		notifiableClaimAuditBean.setClaimStatus(notifiableClaim.getClaimStatus());
		notifiableClaimAuditBean.setPaymentMethod(notifiableClaim.getPaymentMethod());
		notifiableClaimAuditBean.setUpdateReason(notifiableClaim.getUpdateReason());
		notifiableClaimAuditBean.setClaimCategoryCode(notifiableClaim.getClaimCategoryCode());
		notifiableClaimAuditBean.setClaimReasonCode(notifiableClaim.getClaimReasonCode());
		if (notificationTxnTypeCode.contentEquals(OperationTypeEnum.DELETE.toString())) {
			namedParameterJdbcTemplate.update(NotifiableClaimsDAOQueries.INSERT_NOTIFIABLE_CLAIM_AUDIT_QUERY_FOR_DELETE,
					new BeanPropertySqlParameterSource(notifiableClaimAuditBean));
		} else {
			namedParameterJdbcTemplate.update(NotifiableClaimsDAOQueries.INSERT_NOTIFIABLE_CLAIM_AUDIT_QUERY,
					new BeanPropertySqlParameterSource(notifiableClaimAuditBean));
		}
	}

	public boolean isInsuredPersonExists(InsuredPersonBean insuredPersonBean) throws ResourceNotFoundException {
		return insuredPersonEnquiryDAO.isInsuredPersonExists(insuredPersonDAO.getIPSearchCriteria(insuredPersonBean));
	}

	public static String formatDate(Date date) {
		if (date != null) {
			String pattern = "dd/MM/yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.format(date);
		}
		return "";
	}
	public boolean checkForValues(String oldValue,String newValue)
	{
		boolean status=true;
		if(oldValue!=null && newValue!=null)
		{
			if(oldValue.equals(newValue))
			{
				status=false;
			}
			else
			{
				status=true;
			}
		}
		return status;
	}
}
