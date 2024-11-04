package za.co.oldmutual.asisa.enquiries;

import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.enquiry.autosuggest.AutoSuggestController;
import za.co.oldmutual.asisa.enquiry.autosuggest.AutoSuggestDAO;
import za.co.oldmutual.asisa.refdata.RefDataDAO;


public class AutoSuggestControllerTest extends AbstractTest {
  @Mock
  private RefDataDAO refDataDAO;

  @Mock
  private AutoSuggestDAO autoSuggestDAO;

  @InjectMocks
  AutoSuggestController autoSuggestController;

  List<String> responseObject;

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException {
    super.setUp();
  }

  @Test
  public void getIdentityNumbers() throws Exception {
    String identityNumber = "123";
    responseObject = CommonTestData.getDummyAutosuggestIdentityNumberResponse();
    Mockito.when(autoSuggestDAO.autoSuggestByIdentityNumber(Mockito.anyString()))
        .thenReturn(responseObject);
    ResponseEntity<Object> result = autoSuggestController.getIdentityNumbers(identityNumber);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }

  @Test
  public void getSurnames() throws Exception {
    String surname = "Kum";
    responseObject = CommonTestData.getDummyAutosuggestSurnameResponse();
    Mockito.when(autoSuggestDAO.autoSuggestBySurname(Mockito.anyString()))
        .thenReturn(responseObject);
    ResponseEntity<Object> result = autoSuggestController.getSurnames(surname);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(responseObject.toString(), content);
  }
}
