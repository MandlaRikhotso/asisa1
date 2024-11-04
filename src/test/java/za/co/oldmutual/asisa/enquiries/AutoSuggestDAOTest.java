package za.co.oldmutual.asisa.enquiries;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.enquiry.autosuggest.AutoSuggestDAO;
import za.co.oldmutual.asisa.enquiry.autosuggest.AutoSuggestDAOQueries;


public class AutoSuggestDAOTest extends AbstractTest {
  @Mock
  NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @InjectMocks
  AutoSuggestDAO autoSuggestDAO;

  List<String> responseObject;

  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException {
    super.setUp();
  }

  @Test
  public void getIdentityNumbers() throws Exception {
    String identityNumber = "123";
    responseObject = CommonTestData.getDummyAutosuggestIdentityNumberResponse();

    Mockito
        .when(namedParameterJdbcTemplate.queryForList(
            Mockito.eq(AutoSuggestDAOQueries.FETCH_IDENTITY_NUMBER_QUERY),
            Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class)))
        .thenReturn(responseObject);
    List<String> listOFIdentityNumber = autoSuggestDAO.autoSuggestByIdentityNumber(identityNumber);
    assertEquals(responseObject, listOFIdentityNumber);
  }

  @Test
  public void getSurnames() throws Exception {
    String surname = "Kum";
    responseObject = CommonTestData.getDummyAutosuggestSurnameResponse();

    Mockito
        .when(
            namedParameterJdbcTemplate.queryForList(Mockito.eq(AutoSuggestDAOQueries.FETCH_SURNAME_QUERY),
                Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class)))
        .thenReturn(responseObject);
    List<String> listOFSurname = autoSuggestDAO.autoSuggestBySurname(surname);
    assertEquals(responseObject, listOFSurname);
  }

  @Test
  public void getSurnameFailedException() throws Exception {
    String surname = "%";
    try {
      autoSuggestDAO.autoSuggestBySurname(surname);
    } catch (Exception e) {
      assertThatExceptionOfType(ResourceNotFoundException.class);
    }
  }
  
  @Test
  public void getIdentityNumberFailedException() throws Exception {
	    String identityNumber = "$#";
	    try {
	      autoSuggestDAO.autoSuggestByIdentityNumber(identityNumber);
	    } catch (Exception e) {
	      assertThatExceptionOfType(ResourceNotFoundException.class);
	    }
	  }
}
