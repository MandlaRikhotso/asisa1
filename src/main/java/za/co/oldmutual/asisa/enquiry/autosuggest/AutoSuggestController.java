package za.co.oldmutual.asisa.enquiry.autosuggest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;

@RestController
@RequestMapping("/api/autosuggest")
@PostAuthorize(value="hasAnyAuthority(@roles.enquiry)")
public class AutoSuggestController {

  @Autowired
  private AutoSuggestDAO autoSuggestDAO;

  private static final Logger LOGGER = LoggerFactory.getLogger(AutoSuggestController.class);

  @ApiOperation(
      value = "Returns list of SA identityNumber starting with the given ID Number prefix")
  @GetMapping(value = "/identityNumber/{identityNumber}")
  public ResponseEntity<Object> getIdentityNumbers(
      @PathVariable("identityNumber") String identityNumber) throws ResourceNotFoundException {
    LOGGER.info("Initiating IdentityNumber search with Identity Number [{}]", identityNumber);
    return new ResponseEntity<>(autoSuggestDAO.autoSuggestByIdentityNumber(identityNumber),
        HttpStatus.OK);
  }

  @ApiOperation(value = "Returns list of surnames starting with the given surnames prefix")
  @GetMapping(value = "/surname/{surname}")
  public ResponseEntity<Object> getSurnames(@PathVariable("surname") String surname)
      throws ResourceNotFoundException,SpecialCharactersFoundException {
    LOGGER.info("Initiating Surname search with Surname [{}]", surname);
    return new ResponseEntity<>(autoSuggestDAO.autoSuggestBySurname(surname), HttpStatus.OK);
  }

}
