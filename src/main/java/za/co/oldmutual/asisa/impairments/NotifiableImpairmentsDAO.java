package za.co.oldmutual.asisa.impairments;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.common.bean.NotificationStatusEnum;
import za.co.oldmutual.asisa.common.bean.OperationTypeEnum;
import za.co.oldmutual.asisa.common.dao.InsuredPersonDAO;
import za.co.oldmutual.asisa.common.dao.NoteDAO;
import za.co.oldmutual.asisa.common.dao.NotificationDAO;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;

@Component
public class NotifiableImpairmentsDAO {

  private static final Logger logger = LoggerFactory.getLogger(NotifiableImpairmentsDAO.class);

  @Autowired
  NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  IPEnquiryDAO insuredPersonEnquiryDAO;

  @Autowired
  InsuredPersonDAO insuredPersonDAO;

  @Autowired
  NotificationDAO notificationDAO;

  @Autowired
  ReferenceDataCache referenceDataCache;

  @Autowired
  NotifiableImpairmentRowMapper notifiableImpairmentBeanRowMapper;

  @Autowired
  NoteDAO noteDAO;

  @Autowired
  UserLoginIdentifier userLoginIdentifier;
  
  @Autowired
 MailService mailService;
  
  @Autowired
  RefDataDAO refDataDAO;

  private String insuredPersonID;

  private static final String SUCCESS_MESSAGE = "successMessage";

  private static final String NOTIFICATION_ID = "notificationID";

  public Map<String, Object> addNotifiableImpairments(
      NotifiableImpairmentsRequest notifiableImpairmentsRequest)
      throws ResourceUpdationFailedException {
    Map<String, Object> responseObject = new HashMap<>();
    boolean insuredPersonExist = true;

    // 1.Check IP exists or need to add new
    try {
      logger.debug("Initiating search for InsuredPerson...");
      insuredPersonID = insuredPersonEnquiryDAO.getInsuredPersonID(
          insuredPersonDAO.getIPSearchCriteria(notifiableImpairmentsRequest.getInsuredPerson()));
      String source=refDataDAO.getOffice("7:57");
      if (insuredPersonID.equalsIgnoreCase("0")) {
        insuredPersonExist = false;
        logger.debug("InsuredPerson not found");
        notifiableImpairmentsRequest.getInsuredPerson().setSource(source);
        insuredPersonID =
            insuredPersonDAO.insertInsuredPerson(notifiableImpairmentsRequest.getInsuredPerson());
      }
      // 2. insert notifications, notes, scratch pad and impairment's
     
          if (!(notifiableImpairmentsRequest.getNotifications().get(0).getNotifiableImpairments()
                  .isEmpty())) {
        	  if(validateImpairments(notifiableImpairmentsRequest)) {
                logger.debug("InsuredPerson already exists..");
                notifiableImpairmentsRequest.getNotifications().forEach(notification -> {
                  notification.setInsuredPersonID(insuredPersonID);
                  for (NotifiableImpairmentBean notifiableImpairment : notification
                      .getNotifiableImpairments()) {
                    notification.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
                    notification.setNotificationSourceCode(source);
                    notification.setNotificationStatusCode(NotificationStatusEnum.ACTIVE.toString());
                    notification.setNotificationTxnType(OperationTypeEnum.CREATE.toString());
                    notification.setNotificationTypeCode("IMPAIRMENT");
                    String notificationID = notificationDAO.insertImpairmentNotification(notification);
                    notifiableImpairment.setNotificationID(notificationID);
                    insertNotifiableImpairment(notifiableImpairment);
                    addNotifiableImpairmentForAudit(notifiableImpairment,
                        OperationTypeEnum.CREATE.toString(), NotificationStatusEnum.ACTIVE.toString(), "Y");
                    insertNote(notification.getNotes(), notificationID, insuredPersonID);
                  }
                });
                responseObject.put(SUCCESS_MESSAGE, "Impairments saved successfully");
          }
          else {
        	  responseObject.put(SUCCESS_MESSAGE, "Cannot add impairments greater than 9") ; 
          }
              } else {

                notifiableImpairmentsRequest.getNotifications()
                    .forEach(notification -> notification.getNotes().forEach(note -> {
                      note.setPersonID(insuredPersonID);
                      noteDAO.insertScratchpad(note);
                    }));
                responseObject.put(SUCCESS_MESSAGE, (insuredPersonExist) ? "Scratchpad saved successfully"
                    : "Insured Person saved successfully");
              }
         
    
      return responseObject;
    } catch (Exception e) {
      logger.error("Exception Occurred while adding NotifiableImpairment : {} ", e.getMessage());
      logger.error("action = addNotifiableImpairments in NotifiableImpairmentsDao ");
      throw new ResourceUpdationFailedException();
    }
  }
  
  private void insertNote(List<NoteBean> notes,String notificationID, String insuredPersonID) {
	  for (NoteBean note : notes) {
          note.setNotificationID(notificationID);
          note.setPersonID(insuredPersonID);
          noteDAO.insertNote(note);
        }
  }
  
  private boolean validateImpairments(NotifiableImpairmentsRequest notifiableImpairmentsRequest) {
		boolean status = true;
		for(ImpairmentNotificationBean notification : notifiableImpairmentsRequest.getNotifications()) {
			if (!notification.getNotifiableImpairments().isEmpty()
					&& notification.getNotifiableImpairments().size() > 9) {
				status = false;

			}
		}
		return status;
	}

  @Transactional
  public Map<String, Object> updateNotifiableImpairment(
      UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest) 
      throws ResourceUpdationFailedException {
    logger.debug("Checking if request is for Update or Delete");
    Map<String, Object> responseObject = new HashMap<>();
    try {
      String notificationTxnTypeCode;
      String notificationStatusCode;
      String isActive;
      String createdBy;
      String insuredPersonId = insuredPersonEnquiryDAO.getInsuredPersonByNotificationID(updateNotifiableImpairmentRequest.getNotificationID());
      NotifiableImpairmentBean notifiableImpairmentBean =
          fetchExistingNotifiableImpairment(updateNotifiableImpairmentRequest.getNotificationID());
      if (!StringUtils.isEmpty(notifiableImpairmentBean)) {   	  
        if (updateNotifiableImpairmentRequest.isDelete()) {
          logger.debug("Deleting NotifiableClaim with details [{}]",
              updateNotifiableImpairmentRequest);
          notificationTxnTypeCode = OperationTypeEnum.DELETE.toString();
          notificationStatusCode = NotificationStatusEnum.APPROVAL_PENDING.toString();
          isActive = "N";
          createdBy = userLoginIdentifier.fetchUserDetails();
          notificationDAO.updateNotification(updateNotifiableImpairmentRequest.getNotificationID(),
              notificationTxnTypeCode, notificationStatusCode, createdBy, isActive);
          notifiableImpairmentBean.setUpdateReason(updateNotifiableImpairmentRequest.getUpdateReason());
          return updateDeleteCommonFunctionality(updateNotifiableImpairmentRequest, responseObject,
  				notificationTxnTypeCode, notificationStatusCode, isActive, insuredPersonId, notifiableImpairmentBean);
        } else {
        	if((updateNotifiableImpairmentRequest.getReadings().equals(notifiableImpairmentBean.getReadings()))
      		        && (updateNotifiableImpairmentRequest.getTimeSignal().equals(notifiableImpairmentBean.getTimeSignal())))
      		         {
        		if(!(updateNotifiableImpairmentRequest.getNotes().isEmpty())) {
        		  	insertNote(updateNotifiableImpairmentRequest.getNotes(), updateNotifiableImpairmentRequest.getNotificationID(), insuredPersonId);
        		  
        		  responseObject.put(SUCCESS_MESSAGE, "Notes Or Scratchpad inserted successfully!");
        		  return responseObject;
        		}
        		responseObject.put(SUCCESS_MESSAGE, "Value for Timesignal or Readings is not changed");
      		  return responseObject;
        	  }else {
          logger.debug("Updating NotifiableImpairment with details [{}]",
              updateNotifiableImpairmentRequest);
          notificationTxnTypeCode = OperationTypeEnum.UPDATE.toString();
          notificationStatusCode = NotificationStatusEnum.ACTIVE.toString();
          isActive = "Y";
          createdBy = userLoginIdentifier.fetchUserDetails();
          notificationStatusCode =
              setValuesForAuthorization(updateNotifiableImpairmentRequest, notificationTxnTypeCode,
                  notificationStatusCode, isActive, notifiableImpairmentBean, createdBy);
          
          return updateDeleteCommonFunctionality(updateNotifiableImpairmentRequest, responseObject,
  				notificationTxnTypeCode, notificationStatusCode, isActive, insuredPersonId, notifiableImpairmentBean);
        }
       } 
      
    }else {
        throw new ResourceNotFoundException();
      }
    } catch (Exception e) {
      logger.error(
          "Exception occured at updateNotifiableImpairment in NotifiableImpairmentDAO : {}",
          e.getMessage());
      throw new ResourceUpdationFailedException();
    }
  }

private Map<String, Object> updateDeleteCommonFunctionality(
		UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest, Map<String, Object> responseObject,
		String notificationTxnTypeCode, String notificationStatusCode, String isActive, String insuredPersonId,
		NotifiableImpairmentBean notifiableImpairmentBean) throws InvalidDataException {
	updateExistingNotifiableImpairment(updateNotifiableImpairmentRequest);
	addNotifiableImpairmentForAudit(notifiableImpairmentBean, notificationTxnTypeCode,
	    notificationStatusCode, isActive);

	insertNote(updateNotifiableImpairmentRequest.getNotes(), updateNotifiableImpairmentRequest.getNotificationID(), insuredPersonId);
	responseObject = responseOnUpdateOrDelete(updateNotifiableImpairmentRequest, notificationTxnTypeCode, responseObject);
	return responseObject;
}
  
  private String setValuesForAuthorization(
      UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest,
      String notificationTxnTypeCode, String notificationStatusCode, String isActive,
      NotifiableImpairmentBean notifiableImpairmentBean, String createdBy) {

    if (checkForImpairmentForAuthorization(updateNotifiableImpairmentRequest,
        notifiableImpairmentBean)) {
      notifiableImpairmentBean.setReadings(updateNotifiableImpairmentRequest.getReadings());
      notifiableImpairmentBean.setTimeSignal(updateNotifiableImpairmentRequest.getTimeSignal());
      notifiableImpairmentBean.setUpdateReason(updateNotifiableImpairmentRequest.getUpdateReason());
      notificationStatusCode = NotificationStatusEnum.APPROVAL_PENDING.toString();
      notificationDAO.updateNotification(updateNotifiableImpairmentRequest.getNotificationID(),
          notificationTxnTypeCode, notificationStatusCode, createdBy, isActive);
    }
    return notificationStatusCode;
  }

  private boolean checkForImpairmentForAuthorization(
      UpdateNotifiableImpairmentRequest newImpairment, NotifiableImpairmentBean oldImpairment) {
    logger.debug("Checking if fields updated for Authorization...");
    return !((newImpairment.getReadings().equals(oldImpairment.getReadings()))
        && (newImpairment.getTimeSignal().equals(oldImpairment.getTimeSignal())));
  }

  private void insertNotifiableImpairment(NotifiableImpairmentBean notifiableImpairment) {
    logger.debug("Inserting NotifiableImpairment with details [{}]", notifiableImpairment);
    notifiableImpairment.setSpecialInvestigationcode(notifiableImpairment.getSpecialInvestigation()
        .stream().map(LifeSpecBean::getCode).collect(Collectors.joining(",")));
    notifiableImpairment.setSymbolcode(notifiableImpairment.getSymbol().stream()
        .map(LifeSymbolBean::getCode).collect(Collectors.joining(",")));
    namedParameterJdbcTemplate.update(
        NotifiableImpairmentsDAOQueries.INSERT_NOTIFIABLE_IMPAIRMENT_QUERY,
        new BeanPropertySqlParameterSource(notifiableImpairment));
  }

  private Map<String, Object> responseOnUpdateOrDelete(UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest, 
		  String notificationTxnTypeCode, Map<String, Object> responseObject) throws InvalidDataException {
	  if (notificationTxnTypeCode.equals(OperationTypeEnum.UPDATE.toString())) {
          responseObject.put(SUCCESS_MESSAGE, "Impairments updated successfully");
          Map<String, Object> model = new HashMap<>();
			model.put("reason", updateNotifiableImpairmentRequest.getUpdateReason());
			mailService.sendEmail("XY58013",OperationTypeEnum.UPDATE.toString(),
					model);
          return responseObject;
        } else if (notificationTxnTypeCode.equals(OperationTypeEnum.DELETE.toString())) {
          responseObject.put(SUCCESS_MESSAGE, "Impairments deleted successfully");
          Map<String, Object> model = new HashMap<>();
			model.put("reason", updateNotifiableImpairmentRequest.getUpdateReason());
			mailService.sendEmail("XY58013",OperationTypeEnum.DELETE.toString(),
					model);
          return responseObject;
        } else {
          throw new InvalidDataException();
        }
  }
  
  private void updateExistingNotifiableImpairment(
      UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest) {
    final String updateNotifiableImpairmentQuery;
    try {
      if (updateNotifiableImpairmentRequest.isDelete()) {
        updateNotifiableImpairmentQuery = NotifiableImpairmentsDAOQueries.UPDATE_REASON_QUERY;
      } else {
        updateNotifiableImpairmentQuery =
            NotifiableImpairmentsDAOQueries.UPDATE_NOTIFIABLE_IMPAIRMENT_QUERY;
      }
      namedParameterJdbcTemplate.update(updateNotifiableImpairmentQuery,
          new BeanPropertySqlParameterSource(updateNotifiableImpairmentRequest));
    } catch (DataAccessException e) {
      logger.error(
          "Exception occured at updateNotifiableImpairment in NotifiableImpairmentDAO : {}",
          updateNotifiableImpairmentRequest);
    }
  }

  private void addNotifiableImpairmentForAudit(NotifiableImpairmentBean notifiableImpairmentBean,
      String transType, String notifiableStatusCode, String isActive) {
    logger.debug("Inserting NotifiableImpairmentForAudit...");
    try {
      NotifiableImpairmentAuditBean notifiableImpairmentAuditBean =
          new NotifiableImpairmentAuditBean();
      notifiableImpairmentAuditBean.setNotificationTxnType(transType);
      notifiableImpairmentAuditBean.setIsActive(isActive);
      notifiableImpairmentAuditBean.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
      notifiableImpairmentAuditBean.setCreatedDate(new Date());
      notifiableImpairmentAuditBean.setAstuteRefNo("");
      notifiableImpairmentAuditBean.setNotifiableStatusCode(notifiableStatusCode);
      notifiableImpairmentAuditBean.setNotificationID(notifiableImpairmentBean.getNotificationID());
      notifiableImpairmentAuditBean.setImpairment(notifiableImpairmentBean.getImpairment());
      notifiableImpairmentAuditBean.setTimeSignal(notifiableImpairmentBean.getTimeSignal());
      notifiableImpairmentAuditBean.setReadings(notifiableImpairmentBean.getReadings());
      notifiableImpairmentAuditBean.setUpdateReason(notifiableImpairmentBean.getUpdateReason());     
      notifiableImpairmentAuditBean
          .setSpecialInvestigationcode(notifiableImpairmentBean.getSpecialInvestigationcode());
      notifiableImpairmentAuditBean.setSymbolcode(notifiableImpairmentBean.getSymbolcode());
      namedParameterJdbcTemplate.update(
          NotifiableImpairmentsDAOQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY,
          new BeanPropertySqlParameterSource(notifiableImpairmentAuditBean));
    } catch (DataAccessException e) {
      logger.error(
          "Exception occured at inserting NotifiableImpairmentForAudit in NotifiableImpairmentDao : {}",
          notifiableImpairmentBean);
    }

  }

  private NotifiableImpairmentBean fetchExistingNotifiableImpairment(String notificationID) {
    return namedParameterJdbcTemplate.query(NotifiableImpairmentsDAOQueries.FETCH_DETAIL_QUERY,
        new MapSqlParameterSource(NOTIFICATION_ID, notificationID),
        notifiableImpairmentBeanRowMapper).get(0);
  }

  public boolean isInsuredPersonExists(InsuredPersonBean insuredPersonBean)
      throws ResourceNotFoundException {
    try {
      return insuredPersonEnquiryDAO
          .isInsuredPersonExists(insuredPersonDAO.getIPSearchCriteria(insuredPersonBean));
    } catch (Exception e) {
      logger.error("action= insuredPersonExists DAO,insuredPersonExists = {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }
}
