package za.co.oldmutual.asisa.impairments;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;

@Api(value = "IMPAIRMENT CONTROLLER")
@RestController
@RequestMapping("/api/impairments")
@PostAuthorize(value="hasAnyAuthority(@roles.notifiableImpairment)")
public class NotifiableImpairmentsController {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(NotifiableImpairmentsController.class);

  @Autowired
  NotifiableImpairmentsDAO notifiableImpairmentsDAO;

  @ApiOperation(
      value = " Create new notifiable impairments to an existing insured person or new insured person")
  @PostMapping("/notifiableImpairments")
  public ResponseEntity<Object> addNotifiableImpairments(
      @Valid @RequestBody NotifiableImpairmentsRequest notifiableImpairmentsRequest)
      throws ResourceUpdationFailedException {
    LOGGER.info("Adding new impairments with these details {}", notifiableImpairmentsRequest);
    return new ResponseEntity<>(
        notifiableImpairmentsDAO.addNotifiableImpairments(notifiableImpairmentsRequest),
        HttpStatus.OK);
  }

  @ApiOperation(value = " Update an existing notifiable impairment")
  @PutMapping("/notifiableImpairment")
  public ResponseEntity<Object> updateNotifiableImpairment(
      @Valid @RequestBody UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest)
      throws ResourceUpdationFailedException {
    LOGGER.info("Updating existing impairment with these details {}",
        updateNotifiableImpairmentRequest);
    return new ResponseEntity<>(
        notifiableImpairmentsDAO.updateNotifiableImpairment(updateNotifiableImpairmentRequest),
        HttpStatus.OK);
  }

}
