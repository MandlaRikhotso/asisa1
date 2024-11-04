package za.co.oldmutual.asisa.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsDAO;
import za.co.oldmutual.asisa.ldap.AsisaUserDetails;

@Api(value = "USER CONTROLLER")
@RestController
@RequestMapping("/api/admin")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserDAO userDAO;

	@Autowired
	NotifiableImpairmentsDAO notifiableImpairmentsDAO;

	@ResponseBody
	@GetMapping("/getUserAuthorities")
	@PostAuthorize(value="hasAnyAuthority(@roles.users)")
	public ResponseEntity<Object> loginAsisaUser() throws UserLoginException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AsisaUserDetails userDetails = (AsisaUserDetails) authentication.getPrincipal();
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
		
	}

	@ApiOperation(value = "Create new Asisa User")
	@PostMapping("/createAsisaUser")
	@PostAuthorize(value="hasAnyAuthority('USER_ADMIN')")
	public ResponseEntity<Object> createAsisaUser(@Valid @RequestBody UserBean userBean) {
		logger.info("Initiating creation of Asisa User  details {}", userBean);
		Map<String, Object> responseObject = new HashMap<>();
		userDAO.insertUser(userBean);
		responseObject.put("successMessage", "User registered successfully");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

	@ApiOperation(value = "Get details of Asisa User")
	@PostMapping("/getUserDetails")
	@PostAuthorize(value="hasAnyAuthority('USER_ADMIN')")
	public ResponseEntity<Object> getUserDetails(@Valid @RequestBody String omUserId) throws InvalidDataException {
		logger.info("Fetching details of  Asisa User  details for the given OmUSerId {}", omUserId);
		List<String> resultList = null;
		String uid = omUserId.replaceAll("[\\[\\](){}]", "");
		if (!StringUtils.trimAllWhitespace(uid).equals("") && !uid.isEmpty()) {
			resultList = userDAO.getUserDetails(omUserId);
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put(omUserId, resultList);
			return new ResponseEntity<>(resultMap, HttpStatus.OK);
		}
		throw new InvalidDataException();
	}

	@ApiOperation(value = "Update Asisa User")
	@PostMapping("/updateAsisaUser")
	@PostAuthorize(value="hasAnyAuthority('USER_ADMIN')")
	public ResponseEntity<Object> updateAsisaUser(@Valid @RequestBody UserBean userBean) {
		logger.info("Initiating updation of Asisa User  details {}", userBean);
		Map<String, Object> responseObject = new HashMap<>();
		userDAO.updateUser(userBean);
		responseObject.put("successMessage", "User updated successfully");
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

}
