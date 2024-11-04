package za.co.oldmutual.asisa.impairment;

import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.custom.RefTypeValidator;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsController;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsDAO;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsRequest;
import za.co.oldmutual.asisa.impairments.UpdateNotifiableImpairmentRequest;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class NotifiableImpairmentsControllerTest extends AbstractTest {
  @MockBean
  RefTypeValidator refTypeValidator;

  @Mock
  RefDataDAO refDataDAO;

  @Mock
  NotifiableImpairmentsDAO notifiableImpairmentsDAO;

  @Mock
  ReferenceDataCache referenceDataCache;

  @InjectMocks
  NotifiableImpairmentsController notifiableImpairmentsController;

  NotifiableImpairmentsRequest notifiableImpairmentsRequest;
  UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest;
  Errors errors;

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    notifiableImpairmentsRequest = CommonTestData.getDummyNotifiableImpairmentsRequest();
    updateNotifiableImpairmentRequest = CommonTestData.getDummyUpdateNotifiableImpairmentRequest();
    Mockito.when(referenceDataCache.isCodeValid("1", RefTypeEnum.TITLE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("1", RefTypeEnum.IDTYPE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("1", RefTypeEnum.GENDER)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("AD01", RefTypeEnum.IMPAIRMENT_CODE)).thenReturn(true);
  }

  @Test
  public void addImpairment() throws Exception {
    Map<String, Object> responseObject = new HashMap<>();
    responseObject.put("successMessage", "Impairments saved successfully");
    Mockito
        .when(notifiableImpairmentsDAO
            .addNotifiableImpairments(Mockito.any(NotifiableImpairmentsRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        notifiableImpairmentsController.addNotifiableImpairments(notifiableImpairmentsRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void updateImpairment() throws Exception {
    Map<String, Object> responseObject = new HashMap<>();
    responseObject.put("successMessage", "Impairments updated successfully");
    Mockito
        .when(notifiableImpairmentsDAO
            .updateNotifiableImpairment(Mockito.any(UpdateNotifiableImpairmentRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result = notifiableImpairmentsController
        .updateNotifiableImpairment(updateNotifiableImpairmentRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void deleteImpairment() throws Exception {
    updateNotifiableImpairmentRequest = new UpdateNotifiableImpairmentRequest();
    updateNotifiableImpairmentRequest.setDelete(true);
    updateNotifiableImpairmentRequest.setNotifiableImpairmentID(5);
    updateNotifiableImpairmentRequest.setNotificationID("412b0fc5-8b22-4426-a93b-4a180cc5d969");
    updateNotifiableImpairmentRequest.setUpdateReason("updatingggg");
    Map<String, Object> responseObject = new HashMap<>();
    responseObject.put("successMessage", "Impairments deleted successfully");
    Mockito
        .when(notifiableImpairmentsDAO
            .updateNotifiableImpairment(Mockito.any(UpdateNotifiableImpairmentRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result = notifiableImpairmentsController
        .updateNotifiableImpairment(updateNotifiableImpairmentRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }
}
