package za.co.oldmutual.asisa.impairments;

public class NotifiableImpairmentsDAOQueries {

  private NotifiableImpairmentsDAOQueries() {

  }

  public static final String FETCH_IMPAIRMENT_QUERY =
      "SELECT READINGS,TIME_SIGNAL FROM LC_NOTIFIABLE_IMPAIRMENT WHERE NOTIFICATION_ID = :notificationID";

  public static final String INSERT_NOTIFIABLE_IMPAIRMENT_QUERY =
      "INSERT INTO LC_NOTIFIABLE_IMPAIRMENT (NOTIFICATION_ID, IMPAIRMENT_CODE , READINGS, "
          + " TIME_SIGNAL ,LIFE_SPEC_CODES, LIFE_SYMBOL_CODES) "
          + "VALUES (:notificationID, :impairment.code, :readings,"
          + " :timeSignal, :specialInvestigationcode, :symbolcode)";

  public static final String UPDATE_REASON_QUERY =
      "UPDATE LC_NOTIFIABLE_IMPAIRMENT SET REASON_FOR_EDIT = :updateReason "
          + "WHERE NOTIFICATION_ID = :notificationID";

  public static final String UPDATE_NOTIFIABLE_IMPAIRMENT_QUERY =
      "UPDATE LC_NOTIFIABLE_IMPAIRMENT SET TIME_SIGNAL = :timeSignal, READINGS = :readings, REASON_FOR_EDIT = :updateReason "
          + "WHERE NOTIFICATION_ID = :notificationID";

  public static final String INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY =
      "INSERT INTO LC_NOTIFIABLE_IMPAIRMENT_AUDIT (NOTIFICATION_ID, IMPAIRMENT_CODE ,  READINGS,"
          + " TIME_SIGNAL, LIFE_SPEC_CODES, LIFE_SYMBOL_CODES, "
          + "REASON_FOR_EDIT, NOTIFICATION_STATUS_CODE, NOTIFICATION_TXN_TYPE, ASTUTE_REF_NO, CREATED_BY, CREATED_DATE, IS_ACTIVE) "
          + "VALUES (:notificationID, :impairment.code, :readings, :timeSignal,  :specialInvestigationcode, :symbolcode, "
          + " :updateReason, :notifiableStatusCode, :notificationTxnType, :astuteRefNo, :createdBy, CURRENT_TIMESTAMP, :isActive)";

  public static final String FETCH_DETAIL_QUERY =
      "SELECT IMPAIRMENT_CODE,READINGS,TIME_SIGNAL,LIFE_SPEC_CODES,LIFE_SYMBOL_CODES,NOTIFICATION_ID,REASON_FOR_EDIT,NOTIFIABLE_IMPAIRMENT_ID"
          + " FROM LC_NOTIFIABLE_IMPAIRMENT WHERE NOTIFICATION_ID = :notificationID";
}
