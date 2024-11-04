package za.co.oldmutual.asisa.common.dao;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import za.co.oldmutual.asisa.claims.ClaimNotificationBean;
import za.co.oldmutual.asisa.common.bean.AbstractNotificationBean;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.impairments.ImpairmentNotificationBean;

@Component
public class NotificationDAO {

  private static final Logger logger = LoggerFactory.getLogger(NotificationDAO.class);

  @Autowired
  NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  UserLoginIdentifier userLoginIdentifier;

  public String insertImpairmentNotification(ImpairmentNotificationBean notification) {
    return insertNotification(notification);
  }

  public String insertClaimNotification(ClaimNotificationBean notification) {
    return insertNotification(notification);
  }

  private String insertNotification(AbstractNotificationBean notification) {
    logger.info("Start of Insertion of Notification with details[{}]", notification);
    String notificationID = UUID.randomUUID().toString().toUpperCase();
    notification.setNotificationID(notificationID);
    notification.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
    namedParameterJdbcTemplate.update(CommonQueries.INSERT_NOTIFICATION_QUERY,
        new BeanPropertySqlParameterSource(notification));
    return notificationID;
  }

  public void updateNotification(String notificationID, String notificationTxnTypeCode,
      String notificationStatusCode, String createdBy, String isActive) {
    logger.debug("Updating notification...");
    namedParameterJdbcTemplate.update(CommonQueries.UPDATE_NOTIFICATION_QUERY,
        new MapSqlParameterSource("notificationID", notificationID)
            .addValue("notificationTxnTypeCode", notificationTxnTypeCode)
            .addValue("notificationStatusCode", notificationStatusCode)
            .addValue("createdBy", createdBy).addValue("isActive", isActive));
  }

}
