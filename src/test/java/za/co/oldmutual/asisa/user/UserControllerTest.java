package za.co.oldmutual.asisa.user;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.ldap.AsisaUserDetails;

public class UserControllerTest extends AbstractTest {

  @Mock
  UserDAO userDao;

  @InjectMocks
  UserController userController;
  Map<String, Object> responseObject = new HashMap<>();
  UserBean userBean = new UserBean();
  
  @Mock
  LdapUserDetails userDetails;

  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();

    userBean.setBusinessUnit("Personal Finance");
    userBean.setOmUserId("XY57473");
    userBean.setRoleCode("UNDERWRITER");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void createAsisaUserTest() throws Exception {
    Mockito.when(userDao.insertUser(Mockito.any(UserBean.class))).thenReturn(1);
    ResponseEntity<Object> result = userController.createAsisaUser(userBean);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    Map<String, Object> content = (Map<String, Object>) result.getBody();
    assertEquals("User registered successfully", content.get("successMessage"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void updateAsisaUserTest() throws Exception {
    Mockito.when(userDao.updateUser(Mockito.any(UserBean.class))).thenReturn(1);
    ResponseEntity<Object> result = userController.updateAsisaUser(userBean);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    Map<String, Object> content = (Map<String, Object>) result.getBody();
    assertEquals("User updated successfully", content.get("successMessage"));
  }

  @Test
  public void getUserDetailsTest() throws Exception {
    String omUserId = "XY57473";
    List<String> rolecodes = new ArrayList<>();
    rolecodes.add("UNDERWRITER");
    rolecodes.add("CLAIMS_ASSESSOR");
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put(omUserId, rolecodes);
    Mockito.when(userDao.getUserDetails(Mockito.anyString())).thenReturn(rolecodes);
    ResponseEntity<Object> result = userController.getUserDetails(omUserId);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(resultMap.toString(), content);
  }
  
  @Test
  public void getUserDetailsExceptionTest() throws InvalidDataException {
    String omUserId = "0000";		
    ResponseEntity<Object> result = userController.getUserDetails(omUserId);
    assertThatExceptionOfType(InvalidDataException.class);  
  }
  
  @Test
  public void loginAsisaUserTest() throws UserLoginException { 	  	  	 
	  Authentication authentication = mock(Authentication.class);
      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      SecurityContextHolder.setContext(securityContext);
      AsisaUserDetails asisaUserDetails = new AsisaUserDetails(userDetails, "name", "surname", "abc@oldmutual.com", "department", "manager");
      Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(asisaUserDetails);          
      ResponseEntity<Object> obj = userController.loginAsisaUser();
      assertEquals(asisaUserDetails ,obj.getBody());  
  }
}
