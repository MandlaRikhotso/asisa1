package za.co.oldmutual.asisa.user;

public class UserDAOQueries {

	private UserDAOQueries() {
	}

	public static final String INSERT_USER_QUERY = "INSERT INTO LC_USER ( OM_USER_ID, ROLE_CODE, BUSINESS_UNIT, "
			+ " IS_ACTIVE , CREATED_DATE) VALUES (:omUserId, :roleCode, :businessUnit, 'Y', CURRENT_TIMESTAMP)";

	public static final String UPDATE_USER_QUERY = "UPDATE LC_USER SET BUSINESS_UNIT= :businessUnit,"
			+ " ROLE_CODE=:roleCode WHERE UPPER(OM_USER_ID)=UPPER(:omUserId)";

	public static final String DELETE_LC_USER_QUERY = "UPDATE LC_USER SET IS_ACTIVE='N' WHERE UPPER(OM_USER_ID)= UPPER(:omUserId) ";

	public static final String DELETE_USER_QUERY = "UPDATE LC_USER SET IS_ACTIVE='N' WHERE UPPER(OM_USER_ID)=UPPER(:omUserId) AND ROLE_CODE= :roleCode ";

	public static final String GET_USER_QUERY = "SELECT ROLE_CODE FROM LC_USER WHERE UPPER(OM_USER_ID) = UPPER(:omUserId) ";

	public static final String LOAD_USER_QUERY = "SELECT OMUSERROLE.ACTIVITY_CODE FROM LC_USER OMUSER"
			+ "   JOIN LC_USER_ROLE_ACTIVITY OMUSERROLE ON OMUSER.ROLE_CODE = OMUSERROLE.ROLE_CODE WHERE UPPER(OMUSER.OM_USER_ID)= UPPER(:omUserId) ";

	public static final String GET_USER_ROLE_QUERY = "SELECT ROLE_CODE FROM LC_USER WHERE UPPER(OM_USER_ID) = UPPER(:username) ";
	
	public static final String GET_USER_RIGHTS_QUERY = "SELECT ACTIVITY_CODE FROM LC_USER_ROLE_ACTIVITY WHERE ROLE_CODE = :role ";

}
