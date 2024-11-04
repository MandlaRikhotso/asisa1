package za.co.oldmutual.asisa.claims;

public class NotifiableClaimsDAOQueries {

  private NotifiableClaimsDAOQueries() {

  }

  public static final String FETCH_NOTIFIABLE_CLAIM_QUERY =
      "select CLAIM.* FROM LC_NOTIFIABLE_CLAIM CLAIM WHERE CLAIM.NOTIFICATION_ID= :notificationID";

  public static final String INSERT_NOTIFIABLE_CLAIM_QUERY =
      "INSERT INTO LC_NOTIFIABLE_CLAIM (NOTIFICATION_ID, CLAIM_CATEGORY_CODE, "
          + "EVENT_DATE, EVENT_CAUSE_CODE,  EVENT_DEATH_PLACE, EVENT_DEATH_CERTIFICATE_NO, DHA1663_NUMBER, "
          + "CLAIM_REASON_CODES, CLAIM_STATUS_CODE, PAYMENT_METHOD_CODE) "
          + "VALUES (:notificationID, :claimCategoryCode, to_date(:eventDate,'dd/MM/yyyy'), :eventCause.code, :eventDeathPlace, "
          + ":eventDeathCertificateNo, :dha1663Number, :claimReasonCode, :claimStatus.code, :paymentMethod.code)";

  public static final String UPDATE_REASON_QUERY =
      "UPDATE LC_NOTIFIABLE_CLAIM SET REASON_FOR_EDIT = :updateReason "
          + "WHERE NOTIFICATION_ID = :notificationID";

  public static final String UPDATE_NOTIFIABLE_CLAIM_QUERY =
      "UPDATE LC_NOTIFIABLE_CLAIM SET EVENT_DATE = to_date(:eventDate,'dd/MM/yyyy'), EVENT_CAUSE_CODE = :eventCause.code, "
          + "EVENT_DEATH_PLACE = :eventDeathPlace, EVENT_DEATH_CERTIFICATE_NO = :eventDeathCertificateNo, "
          + "DHA1663_NUMBER = :dha1663Number, CLAIM_REASON_CODES = :claimReasonCode, "
          + "CLAIM_STATUS_CODE = :claimStatus.code, PAYMENT_METHOD_CODE = :paymentMethod.code, REASON_FOR_EDIT = :updateReason"
          + " WHERE NOTIFICATION_ID = :notificationID";

  public static final String INSERT_NOTIFIABLE_CLAIM_AUDIT_QUERY =
      "INSERT INTO LC_NOTIFIABLE_CLAIM_AUDIT (NOTIFICATION_ID, CLAIM_CATEGORY_CODE, "
          + "EVENT_DATE, EVENT_CAUSE_CODE, EVENT_DEATH_PLACE, EVENT_DEATH_CERTIFICATE_NO, DHA1663_NUMBER, "
          + "CLAIM_REASON_CODES, CLAIM_STATUS_CODE, PAYMENT_METHOD_CODE, REASON_FOR_EDIT, NOTIFICATION_STATUS_CODE, NOTIFICATION_TXN_TYPE, ASTUTE_REF_NO, CREATED_BY, CREATED_DATE, IS_ACTIVE) "
          + "VALUES (:notificationID, :claimCategoryCode, to_date(:eventDate,'dd/MM/yyyy'), :eventCause.code, :eventDeathPlace, "
          + ":eventDeathCertificateNo, :dha1663Number, :claimReasonCode, :claimStatus.code, :paymentMethod.code, :updateReason, :notificationStatus, :notificationTxnType, :astuteRefNo, :createdBy, CURRENT_TIMESTAMP, :isActive)";

  public static final String INSERT_NOTIFIABLE_CLAIM_AUDIT_QUERY_FOR_DELETE =
      "INSERT INTO LC_NOTIFIABLE_CLAIM_AUDIT (NOTIFICATION_ID, CLAIM_CATEGORY_CODE, "
          + "EVENT_DATE, EVENT_CAUSE_CODE, EVENT_DEATH_PLACE, EVENT_DEATH_CERTIFICATE_NO, DHA1663_NUMBER , "
          + "CLAIM_REASON_CODES, CLAIM_STATUS_CODE, PAYMENT_METHOD_CODE, REASON_FOR_EDIT, NOTIFICATION_STATUS_CODE, NOTIFICATION_TXN_TYPE, ASTUTE_REF_NO, CREATED_BY, CREATED_DATE, IS_ACTIVE) "
          + "VALUES (:notificationID, :claimCategoryCode, to_date(:eventDate,'YYYY-MM-dd'), :eventCause.code, :eventDeathPlace, "
          + ":eventDeathCertificateNo, :dha1663Number, :claimReasonCode, :claimStatus.code, :paymentMethod.code, :updateReason, :notificationStatus, :notificationTxnType, :astuteRefNo, :createdBy, CURRENT_TIMESTAMP, :isActive)";
}
