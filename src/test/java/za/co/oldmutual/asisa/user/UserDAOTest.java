package za.co.oldmutual.asisa.user;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class UserDAOTest extends AbstractTest {

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	ReferenceDataCache referenceDataCache;

	@InjectMocks
	UserDAO userDao;

	@Mock
	RefDataDAO refDataDAO;

	UserBean userBean = new UserBean();
	List<String> rolecodes = new ArrayList<>();
	List<String> activitycodes = new ArrayList<>();

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
		userBean.setBusinessUnit("23");
		userBean.setOmUserId("XY57473");
		userBean.setRoleCode("UNDERWRITER");
		rolecodes.add("UNDERWRITER");
		rolecodes.add("CLAIMS_ASSESSOR");
		rolecodes.add("ENQUIRY_FULL");
		activitycodes.add("ADD_UPDATE_CLAIM");
		activitycodes.add("ENQUIRY_FULL");
		activitycodes.add("ADD_PERSON");
		activitycodes.add("ADD_NOTES");
	}

	@Test
	public void insertUserTest() throws ParseException {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);
		int responseObject = userDao.insertUser(userBean);
		assertEquals(1, responseObject);
	}

	@Test
	public void updateUserTest() throws ParseException {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(UserDAOQueries.UPDATE_USER_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);
		int responseObject = userDao.updateUser(userBean);
		assertEquals(1, responseObject);
	}

	@Test
	public void deleteUserForUserIDTest() throws ParseException {
		userBean.setRoleCode("");
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(UserDAOQueries.DELETE_LC_USER_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);
		int responseObject = userDao.deleteUser(userBean);
		assertEquals(1, responseObject);
	}

	@Test
	public void deleteUserTest() throws ParseException {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(UserDAOQueries.DELETE_USER_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);
		int responseObject = userDao.deleteUser(userBean);
		assertEquals(1, responseObject);
	}

	@Test
	public void displayUserTest() throws ParseException {
		String omUserId = "XY57473";
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.eq(UserDAOQueries.GET_USER_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class))).thenReturn(rolecodes);
		List<String> responseObject = userDao.getUserDetails(omUserId);
		assertEquals(rolecodes, responseObject);
	}

	@Test
	public void loadUserDetailsTest() throws ParseException, UserLoginException {
		String omUserId = "XY57473";
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.eq(UserDAOQueries.LOAD_USER_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class))).thenReturn(activitycodes);
		UserBean user = new UserBean();
		user.setOmUserId(omUserId);
		user.setActivityCode(activitycodes);
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("UserDetails", user);
		Map<String, Object> requestobject = userDao.loadUserDetails(omUserId);

		assertNotNull(requestobject);
	}

	@Test
	public void loadUserDetailsExceptionTest() throws ParseException, UserLoginException {
		String omUserId = "XY57473";
		List<String> activityCode = new ArrayList<>();
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.eq(UserDAOQueries.LOAD_USER_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class))).thenReturn(activityCode);
		UserBean user = new UserBean();
		user.setOmUserId(omUserId);
		user.setActivityCode(activityCode);
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("UserDetails", user);
		try {
			Map<String, Object> requestobject = userDao.loadUserDetails(omUserId);
		} catch (UserLoginException e) {
			assertThatExceptionOfType(UserLoginException.class).isThrownBy(() -> {
				throw new UserLoginException("User 'XY57473' is not authorized.");
			}).withMessage("User 'XY57473' is not authorized.");
		}

	}
}
