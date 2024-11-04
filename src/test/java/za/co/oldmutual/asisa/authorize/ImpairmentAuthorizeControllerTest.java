package za.co.oldmutual.asisa.authorize;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
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
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;


public class ImpairmentAuthorizeControllerTest extends AbstractTest {


  @Mock
  ImpairmentAuthorizeDao impairmentAuthorizeDao;

  @InjectMocks
  ImpairmentAuthorizeController impairmentAuthorizeController;

  List<ImpairmentAuthorizeResponse> dummyImpairmentsForAuthorization;
  ImpairmentAuthorizeResponse impairmentAuthorizeDetailResponseOld;
  ImpairmentAuthorizeResponse impairmentAuthorizeDetailResponseNew;
  InsuredPersonBean insuredPersonBean;
  Map<String, Object> dummyResponseObject;

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    dummyImpairmentsForAuthorization = new ArrayList<>();
    ImpairmentAuthorizeResponse impairmentAuthorizeResponse =
        CommonTestData.getDummyAuthorizationResponse();
    dummyImpairmentsForAuthorization.add(impairmentAuthorizeResponse);
    impairmentAuthorizeDetailResponseOld =
        CommonTestData.getDummyImpairmentAuthorizeDetailResponse();
    impairmentAuthorizeDetailResponseOld.setReadings("12-999");
    insuredPersonBean = CommonTestData.getDummyInsuredPersonBeanResponse();

  }


  @SuppressWarnings("unchecked")
  @Test
  public void authorizeUpdateApproval() throws Exception {
    dummyResponseObject = new HashMap<>();
    dummyResponseObject.put("successMessage", "Impairment updation approved successfully ");
    AuthorizeRequest approvedRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
    approvedRequest.setAction("APPROVE");
    approvedRequest.setActionRemark("Approving");

    Mockito.when(impairmentAuthorizeDao.authorizeUpdate(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(dummyResponseObject);
    ResponseEntity<Object> result =
        impairmentAuthorizeController.approveImpairmentUpdate(approvedRequest);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());

    String content = result.getBody().toString();
    assertEquals(dummyResponseObject.toString(), content);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void authorizeUpdateReject() throws Exception {

    dummyResponseObject = new HashMap<>();
    dummyResponseObject.put("successMessage", "Impairment updation rejected successfully ");
    AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
    approvedImpairmentRequest.setAction("REJECT");
    approvedImpairmentRequest.setActionRemark("Rejecting");
    Mockito.when(impairmentAuthorizeDao.authorizeUpdate(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(dummyResponseObject);
    ResponseEntity<Object> result =
        impairmentAuthorizeController.approveImpairmentUpdate(approvedImpairmentRequest);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());

    String content = result.getBody().toString();
    assertEquals(dummyResponseObject.toString(), content);

  }

  @SuppressWarnings("unchecked")
  @Test
  public void authorizeDeleteApproval() throws Exception {

    dummyResponseObject = new HashMap<>();
    dummyResponseObject.put("successMessage", "Impairment deletion approved successfully ");
    AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
    approvedImpairmentRequest.setAction("APPROVE");
    approvedImpairmentRequest.setActionRemark("Approving");
    Mockito.when(impairmentAuthorizeDao.authorizeDelete(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(dummyResponseObject);
    ResponseEntity<Object> result =
        impairmentAuthorizeController.approveImpairmentUpdate(approvedImpairmentRequest);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());

    String content = result.getBody().toString();
    assertEquals(dummyResponseObject.toString(), content);

  }

  @SuppressWarnings("unchecked")
  @Test
  public void authorizeDeleteReject() throws Exception {

    dummyResponseObject = new HashMap<>();
    dummyResponseObject.put("successMessage", "Impairment deletion rejected successfully ");
    AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
    approvedImpairmentRequest.setAction("REJECT");
    approvedImpairmentRequest.setActionRemark("Rejecting");
    Mockito.when(impairmentAuthorizeDao.authorizeDelete(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(dummyResponseObject);
    ResponseEntity<Object> result =
        impairmentAuthorizeController.approveImpairmentUpdate(approvedImpairmentRequest);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());

    String content = result.getBody().toString();
    assertEquals(dummyResponseObject.toString(), content);
  }

  @Test
  public void authorizeInvalidData() throws Exception {

    AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
    approvedImpairmentRequest.setTransType("");

    try {
      impairmentAuthorizeController.approveImpairmentUpdate(approvedImpairmentRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(InvalidDataException.class);
    }

  }

  @Test
  public void getAllPendingImpairments() throws Exception {

    Mockito.when(impairmentAuthorizeDao.getAllPendingImpairments())
        .thenReturn(dummyImpairmentsForAuthorization);
    ResponseEntity<Object> result = impairmentAuthorizeController.getAllImpairments();

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());

    String content = result.getBody().toString();
    assertEquals(dummyImpairmentsForAuthorization.toString(), content);
  }

  @Test
  public void getImpairmentsForNotificationId() throws Exception {
    String notificationID = "3c65e70b-bba3-4f2a-9a41-2fc13edd05af";
    Map<String, Object> impairmentDetails = new HashMap<>();
    impairmentDetails.put("transType", "UPDATE");

    impairmentDetails.put("insuredPerson", insuredPersonBean);
    impairmentDetails.put("old", impairmentAuthorizeDetailResponseOld);
    impairmentDetails.put("new", impairmentAuthorizeDetailResponseNew);
    Mockito.when(impairmentAuthorizeDao.getImpairmentsForNotifiableImpairmentId(notificationID))
        .thenReturn(impairmentDetails);
    ResponseEntity<Object> result =
        impairmentAuthorizeController.getImpairmentsForImpairmentId(notificationID);

    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());

    String content = result.getBody().toString();
    assertEquals(impairmentDetails.toString(), content);
  }
}
