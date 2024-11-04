package za.co.oldmutual.asisa.authorize;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import java.nio.charset.Charset;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.claims.NotifiableClaimsDAO;
import za.co.oldmutual.asisa.claims.NotifiableClaimsRequest;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.RefDataDAO;

public class ClaimAuthorizeControllerTest extends AbstractTest {

  @Mock
  RefDataDAO refDataDAO;

  @Mock
  NotifiableClaimsDAO notifiableClaimsDAO;

  @Mock
  ClaimAuthorizeDAO claimAuthorizeDao;

  @Mock
  NotifiableClaimsRequest notifiableClaimsRequest;

  @Mock
  AuthorizeRequest approvedClaimRequest;

  @InjectMocks
  ClaimAuthorizeController claimAuthorizeController;

  public static final MediaType APPLICATION_JSON_UTF8 =
      new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  Map<String, Object> responseObject = new HashMap<>();
  List<ClaimAuthorizeResponse> dummyClaimsForAuthorization;
  ClaimAuthorizeResponse claimAuthorizeResponseDetailOld;
  ClaimAuthorizeResponse claimAuthorizeResponseDetailNew;

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    dummyClaimsForAuthorization = new ArrayList<>();
    dummyClaimsForAuthorization.add(CommonTestData.getDummyClaimAuthorizationResponse());
    claimAuthorizeResponseDetailOld = CommonTestData.getDummyClaimAuthorizationResponseDetail();
    claimAuthorizeResponseDetailNew = CommonTestData.getDummyClaimAuthorizationResponseDetail();
    claimAuthorizeResponseDetailNew.setDha1663Number("b123456");
  }

  @Test
  public void invalidApproveRequest() throws Exception {

    AuthorizeRequest authorizeClaimRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
    authorizeClaimRequest.setAction("APPROVE123");
    authorizeClaimRequest.setActionRemark("Approving Claim Changes");
    responseObject.put("successMessage", "Authorization for Claim Update successfull");
    Mockito.when(claimAuthorizeDao.updateClaimApproved(Mockito.eq(authorizeClaimRequest)))
        .thenReturn(responseObject);
    try {
      claimAuthorizeController.approveClaimUpdate(authorizeClaimRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(InvalidDataException.class);
    }

  }

  @Test
  public void approveClaimUpdate() throws Exception {

    AuthorizeRequest authorizeClaimRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
    authorizeClaimRequest.setAction("APPROVE");
    authorizeClaimRequest.setActionRemark("Approving Claim Changes");
    responseObject.put("successMessage", "Authorization for Claim Update successfull");
    approvedClaimRequest.setTransType("UPDATE");
    Mockito.when(claimAuthorizeDao.updateClaimApproved(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        claimAuthorizeController.approveClaimUpdate(authorizeClaimRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void rejectClaimUpdate() throws Exception {
    AuthorizeRequest authorizeClaimRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
    authorizeClaimRequest.setAction("REJECT");
    authorizeClaimRequest.setActionRemark("Approving Claim Changes");
    responseObject.put("successMessage", "Authorization for Claim Reject successfull");
    approvedClaimRequest.setTransType("UPDATE");
    Mockito.when(claimAuthorizeDao.updateClaimReject(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        claimAuthorizeController.approveClaimUpdate(authorizeClaimRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void approveClaimDelete() throws Exception {

    AuthorizeRequest authorizeClaimRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
    authorizeClaimRequest.setAction("APPROVE");
    authorizeClaimRequest.setActionRemark("Approving Claim Changes");
    responseObject.put("successMessage", "Authorization for Claim Delete successfull");
    approvedClaimRequest.setTransType("DELETE");

    Mockito.when(claimAuthorizeDao.deleteClaimApproved(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        claimAuthorizeController.approveClaimUpdate(authorizeClaimRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void rejectClaimDelete() throws Exception {

    AuthorizeRequest authorizeClaimRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
    authorizeClaimRequest.setAction("REJECT");
    authorizeClaimRequest.setActionRemark("Approving Claim Changes");
    responseObject.put("successMessage", "Authorization for Claim delete Rejected successfull");
    approvedClaimRequest.setTransType("DELETE");

    Mockito.when(claimAuthorizeDao.deleteClaimReject(Mockito.any(AuthorizeRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        claimAuthorizeController.approveClaimUpdate(authorizeClaimRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void getAllPendingClaims() throws Exception {
    Mockito.when(claimAuthorizeDao.displayClaimTransaction())
        .thenReturn(dummyClaimsForAuthorization);
    ResponseEntity<Object> result = claimAuthorizeController.getAllClaims();
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(dummyClaimsForAuthorization.toString(), content);
  }


  @Test
  public void getAllPendingClaimAndImpairmentCount() throws Exception {
    Mockito.when(claimAuthorizeDao.displayTransactionCount()).thenReturn(1);
    ResponseEntity<Object> result = claimAuthorizeController.getAllAuthorizePendingCount();
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    int content = (int) result.getBody();
    assertEquals(1, content);
  }

  @Test
  public void getPendingClaimDetails() throws Exception {
    String notificationID = "3c65e70b-bba3-4f2a-9a41-2fc13edd05af";
    Map<String, Object> response = new HashMap<>();
    response.put("transType", "UPDATE");
    response.put("insuredPerson", CommonTestData.getDummyInsuredPersonBeanResponse());
    response.put("old", claimAuthorizeResponseDetailOld);
    response.put("new", claimAuthorizeResponseDetailNew);
    Mockito.when(claimAuthorizeDao.findClaimApprovalById(notificationID)).thenReturn(response);
    ResponseEntity<Object> result = claimAuthorizeController.getClaimsForClaimId(notificationID);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(response.toString(), content);
  }
}
