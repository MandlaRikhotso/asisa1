package za.co.oldmutual.asisa.iphistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;

@Api(value = "IP History Controller")
@RestController
@RequestMapping("/api/iphistory")
@PostAuthorize(value="hasAnyAuthority(@roles.ipHistory)")
public class IPHistoryController {

  @Autowired
  IPHistoryDAO insuredPersonHistoryDAO;

  private static final Logger LOGGER = LoggerFactory.getLogger(IPHistoryController.class);

  @ApiOperation(value = "Returns History of Perfect Match")
  @PostMapping("/{identityType}/{identityNumber}/{personId}")
  public ResponseEntity<IPHistoryBean> history(
      @PathVariable("identityType") String identityTypeCode,
      @PathVariable("identityNumber") String identityNumber,
      @PathVariable("personId") String personId) throws ResourceNotFoundException {
    LOGGER.info(
        "Initiating InsuredPersonBean history with Identity Number Type Code & Identity Number [{},{}]",
        identityTypeCode, identityNumber);
    return new ResponseEntity<>(insuredPersonHistoryDAO
        .getInsuredPersonsHistory(identityTypeCode, identityNumber, personId), HttpStatus.OK);
  }

}
