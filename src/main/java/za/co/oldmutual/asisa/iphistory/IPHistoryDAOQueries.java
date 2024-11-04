package za.co.oldmutual.asisa.iphistory;

public class IPHistoryDAOQueries {

  private IPHistoryDAOQueries() {

  }

  public static final String FETCH_INSURED_PERSON_DETAILS_QUERY = "SELECT * FROM LC_INSURED_PERSON "
      + "WHERE IDENTITY_NUMBER_TYPE_CODE=:identityTypeCode AND IDENTITY_NUMBER= :identityNumber AND PERSON_ID= :personId";



  public static final String FETCH_IMPAIRMENT_DETAILS_QUERY =
      "SELECT  IMPAIRMENT.* ,OMUSER.ROLE_CODE,OMUSER.BUSINESS_UNIT,NOTIF.NOTIFICATION_ID, "
          + "NOTIF.CREATED_BY,NOTIF.NOTIFICATION_STATUS_CODE,NOTIF.CREATED_DATE, "
          + "NOTIF.NOTIFICATION_SOURCE,NOTIF.POLICY_REF_NUMBER,NOTIF.POLICY_TYPE_CODE "
          + "FROM LC_NOTIFIABLE_IMPAIRMENT IMPAIRMENT "
          + "INNER JOIN LC_NOTIFICATION NOTIF ON IMPAIRMENT.NOTIFICATION_ID = NOTIF.NOTIFICATION_ID "
          + "INNER JOIN LC_USER OMUSER ON " + " NOTIF.CREATED_BY=OMUSER.OM_USER_ID "
          + "WHERE NOTIF.PERSON_ID = :personId AND "
          + "NOTIF.IS_ACTIVE='Y' ORDER BY CREATED_DATE DESC, "
          + "NOTIFICATION_SOURCE,IMPAIRMENT_CODE";


  public static final String FETCH_CLAIM_DETAILS_QUERY =
      "SELECT  CLAIM.* ,OMUSER.ROLE_CODE, OMUSER.BUSINESS_UNIT, NOTIF.CREATED_BY ,NOTIF.NOTIFICATION_ID, "
          + " NOTIF.NOTIFICATION_STATUS_CODE,NOTIF.CREATED_DATE,NOTIF.NOTIFICATION_SOURCE,NOTIF.POLICY_REF_NUMBER, "
          + " NOTIF.POLICY_TYPE_CODE FROM LC_NOTIFIABLE_CLAIM CLAIM "
          + "INNER JOIN LC_NOTIFICATION NOTIF ON CLAIM.NOTIFICATION_ID = NOTIF.NOTIFICATION_ID JOIN LC_USER OMUSER ON "
          + "NOTIF.CREATED_BY=OMUSER.OM_USER_ID "
          + "WHERE NOTIF.PERSON_ID = :personId AND NOTIF.IS_ACTIVE = 'Y' "
          + "ORDER BY CREATED_DATE DESC, NOTIFICATION_SOURCE,POLICY_TYPE_CODE";

  public static final String FETCH_NOTE_DETAILS_QUERY =
      "SELECT OMUSER.BUSINESS_UNIT, OMUSER.ROLE_CODE AS ROLE, OMUSER.OM_USER_ID AS OMUSERID, "
          + "NOTE.NOTE_ID, NOTE.NOTE_TEXT, NOTE.IS_SCRATCHPAD AS SCRATCHPAD, "
          + "NOTIF.NOTIFICATION_ID AS NOTIFICATIONID,NOTIF.POLICY_REF_NUMBER AS POLICYNUMBER,NOTIF.CREATED_BY,NOTE.CREATED_DATE AS DATE_CREATED,NOTIF.NOTIFICATION_SOURCE AS NOTIFICATIONSOURCE "
          + "FROM LC_NOTE NOTE, LC_NOTIFICATION NOTIF,LC_USER OMUSER WHERE "
          + "NOTE.NOTIFICATION_ID = NOTIF.NOTIFICATION_ID  AND NOTIF.CREATED_BY=OMUSER.OM_USER_ID "
          + "AND NOTIF.PERSON_ID =:personId AND NOTE.IS_SCRATCHPAD = 'N' "
          + "AND NOTIF.IS_ACTIVE = 'Y' ORDER BY NOTE.CREATED_DATE DESC, NOTIF.NOTIFICATION_SOURCE";

  public static final String FETCH_SCRATCHPAD_DETAILS_QUERY =
      "SELECT OMUSER.BUSINESS_UNIT, OMUSER.ROLE_CODE AS ROLE, OMUSER.OM_USER_ID AS OMUSERID,"
          + " NOTE.NOTE_ID, NOTE.NOTE_TEXT, NOTE.IS_SCRATCHPAD AS SCRATCHPAD, NOTE.CREATED_DATE AS DATE_CREATED, PERSON.CREATED_BY,NOTIF.POLICY_REF_NUMBER AS POLICYNUMBER "
          + " FROM LC_NOTE NOTE LEFT JOIN LC_NOTIFICATION NOTIF ON "
          + " NOTE.NOTIFICATION_ID = NOTIF.NOTIFICATION_ID "
          + " JOIN LC_INSURED_PERSON PERSON ON  NOTE.PERSON_ID = PERSON.PERSON_ID "
          + " JOIN LC_USER OMUSER ON PERSON.CREATED_BY = OMUSER.OM_USER_ID "
          + " WHERE NOTE.PERSON_ID = :personId AND NOTE.IS_SCRATCHPAD = 'Y' "
          + " ORDER BY NOTE.CREATED_DATE DESC, OMUSER.BUSINESS_UNIT, OMUSER.ROLE_CODE ";

}