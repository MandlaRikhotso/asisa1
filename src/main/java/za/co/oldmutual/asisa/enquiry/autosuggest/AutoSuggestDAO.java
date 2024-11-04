package za.co.oldmutual.asisa.enquiry.autosuggest;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;

@Component
public class AutoSuggestDAO {

  @Autowired
  private NamedParameterJdbcOperations namedParameterJdbcTemplate;

  private static final Logger LOGGER = LoggerFactory.getLogger(AutoSuggestDAO.class);

  public List<String> autoSuggestByIdentityNumber(String identityNumber)
      throws ResourceNotFoundException {
    try {
      return namedParameterJdbcTemplate.queryForList(AutoSuggestDAOQueries.FETCH_IDENTITY_NUMBER_QUERY,
          new MapSqlParameterSource("identityNumber", identityNumber + "%"), String.class);
    } catch (Exception e) {
      LOGGER.error("Fetching of IdentityNumber failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  public List<String> autoSuggestBySurname(String surname) throws ResourceNotFoundException,SpecialCharactersFoundException {
	  Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
		
	  try {
	    	
	  if (regex.matcher(surname).find()) {
		  throw new SpecialCharactersFoundException();
	     	  }
	  else {
		  return namedParameterJdbcTemplate.queryForList(AutoSuggestDAOQueries.FETCH_SURNAME_QUERY,
		          new MapSqlParameterSource("surname", surname + "%"), String.class);
	  }
    } catch (Exception e) {
    	if(e.getClass().getName().contains("SpecialCharactersFoundException")) {
    		LOGGER.error("Special character found : {}", e.getMessage());
    		throw new SpecialCharactersFoundException();
    	}else {
    	
      LOGGER.error("Fetching of Surname failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
    }

  }
 

}
