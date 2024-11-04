package za.co.oldmutual.asisa.authorize;

import java.util.HashMap;
import java.util.Map;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.InvalidDataException;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;

@Api(value = "NotifiableClaimBean Authorize Controller")
@RestController
@RequestMapping("/api/claims")
@PostAuthorize(value = "hasAnyAuthority(@roles.authorizeClaim)")
public class ClaimAuthorizeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClaimAuthorizeController.class);

  @Autowired
  ClaimAuthorizeDAO claimAuthorizeDAO;

  @ApiOperation(value = "Display All Claims pending for authorization")
  @GetMapping("/authorize")
  public ResponseEntity<Object> getAllClaims() throws ResourceNotFoundException {
    LOGGER.info("Initiating request to display all authorized pending claims");
    return new ResponseEntity<>(claimAuthorizeDAO.displayClaimTransaction(), HttpStatus.OK);
  }

  @ApiOperation(value = "Display Count of All Claims and All Impairments pending for authorization")
  @GetMapping("/authorizecount")
  public ResponseEntity<Object> getAllAuthorizePendingCount() throws ResourceNotFoundException {
    LOGGER.info(
        "Initiating request to display count of all authorized pending claims and impairments ");
    return new ResponseEntity<>(claimAuthorizeDAO.displayTransactionCount(), HttpStatus.OK);
  }

  @ApiOperation(value = "Display both old and updated NotifiableClaimBean for authorization action")
  @GetMapping("/authorize/{notifiableClaimID}")
  public ResponseEntity<Object> getClaimsForClaimId(
      @PathVariable(value = "notifiableClaimID", required = true) @Valid String notifiableClaimID)
      throws ResourceNotFoundException {
    LOGGER.info("Initiating request to display authorized claim detail with ID: [{}]",
        notifiableClaimID);
    Map<String, Object> resposneMap = claimAuthorizeDAO.findClaimApprovalById(notifiableClaimID);
    return new ResponseEntity<>(resposneMap, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Approve NotifiableClaimBean - Updated NotifiableClaimBean to be approved for an Insured InsuredPersonBean")
  @PostMapping("/approval")
  public ResponseEntity<Object> approveClaimUpdate(
      @Valid @RequestBody AuthorizeRequest approvedClaimRequest)
      throws ResourceUpdationFailedException, InvalidDataException {
    LOGGER.info("Initiating request for action on authorized claim with Request detail: [{}]",
        approvedClaimRequest);
    Map<String, Object> responseObject = new HashMap<>();
    switch (approvedClaimRequest.getAction().toUpperCase()) {
      case "APPROVE":
        // NotifiableClaimBean update Approved
        if (approvedClaimRequest.getTransType().equalsIgnoreCase("UPDATE")) {
          responseObject = claimAuthorizeDAO.updateClaimApproved(approvedClaimRequest);
        }
        // NotifiableClaimBean Delete Approved
        else if (approvedClaimRequest.getTransType().equalsIgnoreCase("DELETE")) {
          responseObject = claimAuthorizeDAO.deleteClaimApproved(approvedClaimRequest);
        }
        break;
      // NotifiableClaimBean Authorization Rejected
      case "REJECT":
        // NotifiableClaimBean update Rejected
        if (approvedClaimRequest.getTransType().equalsIgnoreCase("UPDATE")) {
          responseObject = claimAuthorizeDAO.updateClaimReject(approvedClaimRequest);
        }
        // NotifiableClaimBean Delete Rejected
        else if (approvedClaimRequest.getTransType().equalsIgnoreCase("DELETE")) {
          responseObject = claimAuthorizeDAO.deleteClaimReject(approvedClaimRequest);
        }
        break;
      default:
        LOGGER.error("Exception occurred : Invalid data received..");
        throw new InvalidDataException();
    }
    return new ResponseEntity<>(responseObject, HttpStatus.OK);
  }

  public ResponseEntity<Map<String, Object>> responseObject(Map<String, Object> responseObject) {
    return new ResponseEntity<>(responseObject, HttpStatus.OK);
  }
}
