package za.co.oldmutual.asisa.common.dao;


public class CommonQueries {

  private CommonQueries() {

  }

  public static final String INSERT_INSURED_PERSON =
      "INSERT INTO LC_INSURED_PERSON (PERSON_ID,IDENTITY_NUMBER_TYPE_CODE, IDENTITY_NUMBER, DATE_OF_BIRTH, "
          + "GENDER, TITLE, SURNAME, GIVEN_NAME_1, GIVEN_NAME_2, GIVEN_NAME_3, ADDRESS_LINE_1, ADDRESS_LINE_2, ADDRESS_LINE_3, "
          + "POSTAL_CODE, CREATED_BY, CREATED_DATE, SOURCE) "
          + "VALUES (:personID,:identityType.code, :identityNumber, to_date(:dateOfBirth,'dd/MM/yyyy'), "
          + ":gender.code, :title.code, :surname, :givenName1, :givenName2, :givenName3, :addressLine1, :addressLine2, :addressLine3, "
          + ":postalCode, :createdBy, CURRENT_TIMESTAMP, :source) ";

  public static final String INSERT_NOTE_QUERY =
      "INSERT INTO LC_NOTE (NOTIFICATION_ID, NOTE_TEXT , IS_SCRATCHPAD, CREATED_DATE,PERSON_ID) "
          + "VALUES (:notificationID, :noteText, :scratchpad, CURRENT_TIMESTAMP, :personID)";

  public static final String INSERT_SCRATCHPAD_QUERY =
      "INSERT INTO LC_NOTE (PERSON_ID, NOTE_TEXT , IS_SCRATCHPAD, CREATED_DATE) "
          + "VALUES (:personID, :noteText, :scratchpad, CURRENT_TIMESTAMP)";

  public static final String INSERT_NOTIFICATION_QUERY =
      "INSERT INTO LC_NOTIFICATION (NOTIFICATION_ID,NOTIFICATION_SOURCE, NOTIFICATION_TYPE_CODE, NOTIFICATION_TXN_TYPE, "
          + "NOTIFICATION_STATUS_CODE, PERSON_ID, POLICY_REF_NUMBER, POLICY_TYPE_CODE, CREATED_BY, CREATED_DATE, IS_ACTIVE) "
          + "VALUES (:notificationID,:notificationSourceCode, :notificationTypeCode, :notificationTxnType, "
          + ":notificationStatusCode, :insuredPersonID, :policyNumber, :policyBenefit.code, :createdBy, CURRENT_TIMESTAMP,'Y')";

  public static final String UPDATE_NOTIFICATION_QUERY =
      "UPDATE LC_NOTIFICATION SET NOTIFICATION_TXN_TYPE = :notificationTxnTypeCode, NOTIFICATION_STATUS_CODE = :notificationStatusCode,"
          + "CREATED_BY = :createdBy, CREATED_DATE = CURRENT_TIMESTAMP, IS_ACTIVE = :isActive WHERE NOTIFICATION_ID = :notificationID ";

}
