package za.co.oldmutual.asisa.authorize;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;

@RestController
@RequestMapping("/api/impairments")
@PostAuthorize(value = "hasAnyAuthority(@roles.authorizeImpairment)")
public class ImpairmentAuthorizeController {

  @Autowired
  ImpairmentAuthorizeDao impairmentAuthorizeDao;

  private static final Logger logger = LoggerFactory.getLogger(ImpairmentAuthorizeController.class);

  @ApiOperation(value = "Display All Impairments for Authorization")
  @GetMapping("/authorize")
  public ResponseEntity<Object> getAllImpairments() throws ResourceNotFoundException {
    logger.info("Initiating request to display all authorized pending impairments");
    return new ResponseEntity<>(impairmentAuthorizeDao.getAllPendingImpairments(), HttpStatus.OK);
  }

  @ApiOperation(
      value = "Display both old and updated NotifiableImpairmentBean for authorization action")
  @GetMapping("/authorize/{notifiableImpairmentID}")
  public ResponseEntity<Object> getImpairmentsForImpairmentId(
      @PathVariable(value = "notifiableImpairmentID",
          required = true) @Valid String notifiableImpairmentID)
      throws ResourceNotFoundException {
    logger.info("Initiating request to display authorized impairment detail with ID: [{}]",
        notifiableImpairmentID);
    return new ResponseEntity<>(
        impairmentAuthorizeDao.getImpairmentsForNotifiableImpairmentId(notifiableImpairmentID),
        HttpStatus.OK);
  }

  @ApiOperation(value = "Updated Impairments With Authorization Approve/Reject")
  @PostMapping("/approval")
  public ResponseEntity<Object> approveImpairmentUpdate(
      @RequestBody AuthorizeRequest approvedImpairmentRequest)
      throws ResourceUpdationFailedException, InvalidDataException {
    logger.info("Initiating request for action on authorized impairment with Request detail: [{}]",
        approvedImpairmentRequest);
    if (approvedImpairmentRequest.getTransType().equals("UPDATE")) {
      return new ResponseEntity<>(impairmentAuthorizeDao.authorizeUpdate(approvedImpairmentRequest),
          HttpStatus.OK);
    } else if (approvedImpairmentRequest.getTransType().equals("DELETE")) {
      return new ResponseEntity<>(impairmentAuthorizeDao.authorizeDelete(approvedImpairmentRequest),
          HttpStatus.OK);
    } else {
      logger.error("Exception occured : Invalid data received..");
      throw new InvalidDataException();
    }
  }
}
