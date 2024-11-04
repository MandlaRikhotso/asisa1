package za.co.oldmutual.asisa.enquiries;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.text.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.custom.RefTypeValidator;
import za.co.oldmutual.asisa.enquiry.IPEnquiryController;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaBean;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaValidator;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;

public class IPEnquiryCustomizedValidatorTest extends AbstractTest {
  @MockBean
  RefTypeValidator refTypeValidator;

  @MockBean
  ReferenceDataCache referenceDataCache;

  @MockBean
  RefDataDAO refDataDAO;

  @MockBean
  IPEnquiryDAO iPEnquiryDAO;

  @MockBean
  IPEnquiryController iPEnquiryController;

  @InjectMocks
  IPEnquiryCriteriaValidator validator;
  IPEnquiryCriteriaBean iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
  IdentityTypeBean identityTypeBean = new IdentityTypeBean();
  public Errors errors;

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    errors = new BeanPropertyBindingResult(iPEnquiryCriteriaBean, "iPEnquiryCriteriaBean");
    Mockito.when(referenceDataCache.isCodeValid("4", RefTypeEnum.IDTYPE)).thenReturn(true);
  }

  @Test
  public void NotNullTest() {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("18/01/2019");
    iPEnquiryCriteriaBean.setSurname("Surname");
    iPEnquiryCriteriaBean.setIdentityNumber("123450000");
    identityTypeBean.setCode("4");
    identityTypeBean.setDescription("ID NUMBER");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void NotNullTestForSurnameAndDateOfBirth() {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("18/01/2019");
    iPEnquiryCriteriaBean.setSurname("Surname");
    iPEnquiryCriteriaBean.setIdentityNumber("");
    identityTypeBean.setCode("");
    identityTypeBean.setDescription("");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void NotNullTestForIdTypeAndIdNumber() {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("");
    iPEnquiryCriteriaBean.setSurname("");
    iPEnquiryCriteriaBean.setIdentityNumber("123450000");
    identityTypeBean.setCode("4");
    identityTypeBean.setDescription("ID NUMBER");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    assertFalse(errors.hasErrors());
  }

  @Test
  public void NullTestForIdTypeNullAndIdNumber() throws Exception {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("");
    iPEnquiryCriteriaBean.setSurname("");
    iPEnquiryCriteriaBean.setIdentityNumber("123450000");
    identityTypeBean.setCode("");
    identityTypeBean.setDescription("");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    validator.validate(iPEnquiryCriteriaBean, errors);
    assertTrue(errors.hasErrors());
  }

  @Test
  public void NullTestForIdTypeAndIdNumberNull() throws Exception {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("");
    iPEnquiryCriteriaBean.setSurname("");
    iPEnquiryCriteriaBean.setIdentityNumber("");
    identityTypeBean.setCode("4");
    identityTypeBean.setDescription("ID NUMBER");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    validator.validate(iPEnquiryCriteriaBean, errors);
    assertTrue(errors.hasErrors());
  }

  @Test
  public void NullTestForSurnameNullAndDateOfBirth() throws Exception {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("18/01/2019");
    iPEnquiryCriteriaBean.setSurname("");
    iPEnquiryCriteriaBean.setIdentityNumber("");
    identityTypeBean.setCode("");
    identityTypeBean.setDescription("");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    validator.validate(iPEnquiryCriteriaBean, errors);
    assertTrue(errors.hasErrors());
  }

  @Test
  public void NullTestForSurnameAndDateOfBirthNull() throws Exception {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("");
    iPEnquiryCriteriaBean.setSurname("Surname");
    iPEnquiryCriteriaBean.setIdentityNumber("");
    identityTypeBean.setCode("");
    identityTypeBean.setDescription("");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    validator.validate(iPEnquiryCriteriaBean, errors);
    assertTrue(errors.hasErrors());
  }

  @Test
  public void enquiryTestForNull() throws Exception {
    iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
    iPEnquiryCriteriaBean.setDateOfBirth("");
    iPEnquiryCriteriaBean.setSurname("");
    iPEnquiryCriteriaBean.setIdentityNumber("");
    identityTypeBean.setCode("");
    identityTypeBean.setDescription("");
    iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
    validator.validate(iPEnquiryCriteriaBean, errors);
    assertTrue(errors.hasErrors());
  }
}

