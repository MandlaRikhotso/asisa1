
package za.co.oldmutual.asisa.claims;

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
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.GenderBean;
import za.co.oldmutual.asisa.refdata.bean.TitleBean;


public class NotifiableClaimsControllerTest extends AbstractTest {
  @Mock
  RefDataDAO refDataDAO;

  @Mock
  ReferenceDataCache referenceDataCache;

  @Mock
  NotifiableClaimsDAO notifiableClaimsDAO;

  @InjectMocks
  NotifiableClaimsController notifiableClaimsController;

  Map<String, Object> responseObject = new HashMap<>();
  NotifiableClaimsRequest notifiableClaimsRequest;
  InsuredPersonBean insuredPersonBean;

  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    List<ClaimNotificationBean> notificationBeanList = new ArrayList<>();
    insuredPersonBean = CommonTestData.getDummyInsuredPersonBeanRequest();
    ClaimNotificationBean ClaimNotificationBean =
        CommonTestData.getDummyClaimNotificationBeanRequest();
    notificationBeanList.add(ClaimNotificationBean);
    notifiableClaimsRequest = new NotifiableClaimsRequest();
    notifiableClaimsRequest.setInsuredPerson(insuredPersonBean);
    notifiableClaimsRequest.setNotifications(notificationBeanList);
    responseObject.put("status", HttpStatus.OK);
    Mockito.when(referenceDataCache.isCodeValid("3", RefTypeEnum.TITLE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("4", RefTypeEnum.IDTYPE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("1", RefTypeEnum.GENDER)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("10", RefTypeEnum.CLAIM_CAUSE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("16", RefTypeEnum.POLICY_TYPE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("10", RefTypeEnum.POLICY_TYPE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("4", RefTypeEnum.CLAIM_STATUS)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("AD01", RefTypeEnum.IMPAIRMENT_CODE)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("10", RefTypeEnum.PAYMENT_METHOD)).thenReturn(true);
    Mockito.when(referenceDataCache.isCodeValid("1", RefTypeEnum.CLAIM_CATEGORY)).thenReturn(true);
    responseObject.put("status", HttpStatus.OK);
  }

  @Test
  public void addNotifiableClaimTest() throws Exception {
    responseObject.put("successMessage", "Claims added successfully");
    Mockito
        .when(notifiableClaimsDAO.addNotifiableClaims(Mockito.any(NotifiableClaimsRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        notifiableClaimsController.addNotifiableClaim(notifiableClaimsRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void updateNotifiableClaimTest() throws Exception {
    UpdateNotifiableClaimRequest updateNotifiableClaimRequest =
        CommonTestData.getDummyUpdateClaimDetailRequest();
    responseObject.put("successMessage", "Claims updated successfully");
    Mockito
        .when(notifiableClaimsDAO
            .updateNotifiableClaim(Mockito.any(UpdateNotifiableClaimRequest.class)))
        .thenReturn(responseObject);
    ResponseEntity<Object> result =
        notifiableClaimsController.updateNotifiableClaim(updateNotifiableClaimRequest);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void addNotifiableClaimForInvalidDOBTest() throws Exception {
    notifiableClaimsRequest.getInsuredPerson().setDateOfBirth("18/15/2019");
    try {
      notifiableClaimsController.addNotifiableClaim(notifiableClaimsRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(ResourceUpdationFailedException.class);
    }
  }

  @Test
  public void addNotifiableClaimForInvalidGenderTest() throws Exception {
    GenderBean gender = new GenderBean();
    gender.setCode("5");
    notifiableClaimsRequest.getInsuredPerson().setGender(gender);
    try {
      notifiableClaimsController.addNotifiableClaim(notifiableClaimsRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(ResourceUpdationFailedException.class);
    }
  }

  @Test
  public void addNotifiableClaimForInvalidTitleTest() throws Exception {
    TitleBean title = new TitleBean();
    title.setCode("17");
    notifiableClaimsRequest.getInsuredPerson().setTitle(title);
    try {
      notifiableClaimsController.addNotifiableClaim(notifiableClaimsRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(ResourceUpdationFailedException.class);
    }
  }

  @Test
  public void addNotifiableClaimForInvalidSurnameTest() throws Exception {
    notifiableClaimsRequest.getInsuredPerson().setSurname(
        "3111111333545457577777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777");
    try {
      notifiableClaimsController.addNotifiableClaim(notifiableClaimsRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(ResourceUpdationFailedException.class);
    }
  }

  @Test
  public void addNotifiableClaimForInvalidGivenName1Test() throws Exception {
    notifiableClaimsRequest.getInsuredPerson().setGivenName1("");
    try {
      notifiableClaimsController.addNotifiableClaim(notifiableClaimsRequest);
    } catch (Exception e) {
      assertThatExceptionOfType(ResourceUpdationFailedException.class);
    }
  }
}
