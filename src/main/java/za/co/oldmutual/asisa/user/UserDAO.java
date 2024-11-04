package za.co.oldmutual.asisa.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import za.co.oldmutual.asisa.refdata.RefDataDAO;

@Component
public class UserDAO {

	@Autowired
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Autowired
	RefDataDAO refDataDAO;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	private static final String OM_USER_ID = "omUserId";
	
	private static final String ROLE_CODE = "roleCode";

	public int insertUser(UserBean userBean) {
		LOGGER.info("Intiating user creation {}", userBean);
		return namedParameterJdbcTemplate.update(UserDAOQueries.INSERT_USER_QUERY,
				new MapSqlParameterSource(OM_USER_ID,userBean.getOmUserId().toUpperCase())
				.addValue(ROLE_CODE, userBean.getRoleCode())
				.addValue("businessUnit", refDataDAO.getOffice(userBean.getBusinessUnit()))
				);
	}

	public int updateUser(UserBean userBean) {
		return namedParameterJdbcTemplate.update(UserDAOQueries.UPDATE_USER_QUERY,
				new MapSqlParameterSource(OM_USER_ID, userBean.getOmUserId())
						.addValue("businessUnit", userBean.getBusinessUnit())
						.addValue(ROLE_CODE, userBean.getRoleCode()));
	}

	public int deleteUser(UserBean userBean) {
		if (userBean.getRoleCode().isEmpty()) {
			return namedParameterJdbcTemplate.update(UserDAOQueries.DELETE_LC_USER_QUERY,
					new MapSqlParameterSource(OM_USER_ID, userBean.getOmUserId().toUpperCase()));
		} else {

			return namedParameterJdbcTemplate.update(UserDAOQueries.DELETE_USER_QUERY,
					new MapSqlParameterSource(OM_USER_ID, userBean.getOmUserId().toUpperCase()).addValue(ROLE_CODE,
							userBean.getRoleCode()));
		}
	}

	public List<String> getUserDetails(String omUserId) {
		return namedParameterJdbcTemplate.queryForList(UserDAOQueries.GET_USER_QUERY,
				new MapSqlParameterSource(OM_USER_ID, omUserId), String.class);
	}

	public Map<String, Object> loadUserDetails(String omUserId) throws UserLoginException {
		Map<String, Object> responseObject = new HashMap<>();
		List<String> activityCodes = namedParameterJdbcTemplate.queryForList(UserDAOQueries.LOAD_USER_QUERY,
				new MapSqlParameterSource(OM_USER_ID, omUserId), String.class);
		UserBean user = new UserBean();
		user.setOmUserId(omUserId);
		user.setActivityCode(activityCodes);
		if (!activityCodes.isEmpty()) {
			responseObject.put("UserDetails", user);
			return responseObject;
		} else {
			throw new UserLoginException("User '" + omUserId + "' is not authorized.");
		}
	}

}
