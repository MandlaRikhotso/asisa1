package za.co.oldmutual.asisa.refdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.GenderBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;
import za.co.oldmutual.asisa.refdata.bean.TitleBean;

public class RefDataControllerTest extends AbstractTest {

  @Mock
  private RefDataDAO refDataDAO;

  @InjectMocks
  RefDataController refDataController;
  
  @Mock
  private ReferenceDataCache referenceDataCache;

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
  }

  @Test
  public void getClaimCategories() throws Exception {
    List<ClaimCategoryBean> responseObject = CommonTestData.getDummyClaimCategories();
    Mockito.when(refDataDAO.getClaimCategories()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getClaimCategories();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getClaimStatuses() throws Exception {
    List<ClaimStatusBean> responseObject = CommonTestData.getDummyClaimStatuses();
    Mockito.when(refDataDAO.getClaimStatuses()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getClaimStatuses();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getClaimCauses() throws Exception {
    List<ClaimCauseBean> responseObject = CommonTestData.getDummyClaimCauses();
    Mockito.when(refDataDAO.getClaimCauses()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getClaimCauses();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getImpairmentCodes() throws Exception {
    List<ImpairmentCodeBean> responseObject = CommonTestData.getDummyImpairmentCodes();
    Mockito.when(refDataDAO.getImpairmentCodes()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getImpairmentCodes();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getPaymentMethods() throws Exception {
    List<PaymentMethodBean> responseObject = CommonTestData.getDummyPaymentMethods();
    Mockito.when(refDataDAO.getPaymentMethods()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getPaymentMethods();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getPolicyBenefits() throws Exception {
    List<PolicyTypeBean> responseObject = CommonTestData.getDummyPolicyBenefits();
    Mockito.when(refDataDAO.getPolicyTypes()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getPolicyTypes();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getSpecialInvestigations() throws Exception {
    List<LifeSpecBean> responseObject = CommonTestData.getDummySpecialInvestigations();
    Mockito.when(refDataDAO.getLifeSpecs()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getLifeSpecs();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getSymbols() throws Exception {
    List<LifeSymbolBean> responseObject = CommonTestData.getDummySymbols();
    Mockito.when(refDataDAO.getLifeSymbols()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getLifeSymbols();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getIdentityTypes() throws Exception {
    List<IdentityTypeBean> responseObject = CommonTestData.getDummyIdentityTypes();
    Mockito.when(refDataDAO.getIdentityTypes()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getIdentityTypes();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getTitles() throws Exception {
    List<TitleBean> responseObject = CommonTestData.getDummyTitles();
    Mockito.when(refDataDAO.getTitles()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getTitles();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }

  @Test
  public void getGenders() throws Exception {
    List<GenderBean> responseObject = CommonTestData.getDummyGenders();
    Mockito.when(refDataDAO.getGenders()).thenReturn(responseObject);
    ResponseEntity<Map<String, Object>> result = refDataController.getGenders();
    String content = result.getBody().toString();
    assertTrue(content != null);
  }
  
  @Test
  public void getReadings() throws Exception {
	Map<String, Object> responseObject = new HashMap<>();
	responseObject.put("readings", "0CM.0KG");
	Mockito.when( referenceDataCache.populateDescFromCode("E22.0", RefTypeEnum.READINGS)).thenReturn("0CM.0KG");
	ResponseEntity<Map<String, Object>> result =  refDataController.getReadingsCriteria("E22.0");
    assertEquals(responseObject.get("readings"),result.getBody().get("readings"));
  }

 
}
