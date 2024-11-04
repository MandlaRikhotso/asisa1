package za.co.oldmutual.asisa.claims;

import static org.junit.Assert.assertTrue;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.custom.RefTypeValidator;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class NotifiableClaimRequestValidatorTest extends AbstractTest {
  @MockBean
  RefDataDAO refDataDAO;

  @InjectMocks
  NotifiableClaimRequestValidator notifiableClaimRequestValidator;

  @MockBean
  ReferenceDataCache referenceDataCache;

  @MockBean
  RefTypeValidator refTypeValidator;

  @MockBean
  NotifiableClaimsDAO notifiableClaimsDAO;

  @MockBean
  NotifiableClaimsController notifiableClaimsController;

  public Errors errors;
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
    errors = new BeanPropertyBindingResult(notifiableClaimsRequest, "notifiableClaimsRequest");
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
  public void policyBenifitValidityTest() throws Exception {
    notifiableClaimsRequest.getNotifications().get(0).getPolicyBenefit().setCode("1");
    notifiableClaimsRequest.getNotifications().get(0).getNotifiableClaims().get(0)
        .setEventDeathPlace("");
    notifiableClaimsRequest.getNotifications().get(0).getNotifiableClaims().get(0).getClaimReason()
        .get(0).setCode("");
    notifiableClaimRequestValidator.validate(notifiableClaimsRequest, errors);
    assertTrue(errors.hasErrors());
  }
}
