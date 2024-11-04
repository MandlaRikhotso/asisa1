package za.co.oldmutual.asisa.claims;

import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;

@Api(value = "Claims Controller")
@RestController
@RequestMapping("/api/claims")
@PostAuthorize(value="hasAnyAuthority(@roles.notifiableClaim)")
public class NotifiableClaimsController {
  private static final Logger LOGGER = LoggerFactory.getLogger(NotifiableClaimsController.class);

  @Autowired
  NotifiableClaimsDAO notifiableClaimsDAO;

  @Autowired
  NotifiableClaimRequestValidator notifiableClaimRequestValidator;

  @InitBinder("NotifiableClaimsRequest")
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(notifiableClaimRequestValidator);
  }

  @ApiOperation(
      value = " Create new notifiable claims to an existing insured person or new insured person")
  @PostMapping("/notifiableClaims")
  public ResponseEntity<Object> addNotifiableClaim(
      @Valid @RequestBody NotifiableClaimsRequest notifiableClaimsRequest)
      throws ResourceUpdationFailedException {
    LOGGER.info("Adding new claims with these details {}", notifiableClaimsRequest);
    Map<String, Object> responseObject =
        notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequest);
    return new ResponseEntity<>(responseObject, HttpStatus.OK);
  }

  @ApiOperation(value = " Update an existing notifiable claim")
  @PutMapping("/notifiableClaim")
  public ResponseEntity<Object> updateNotifiableClaim(
      @Valid @RequestBody UpdateNotifiableClaimRequest updateNotifiableClaimRequest)
      throws ResourceUpdationFailedException {
    LOGGER.info("Updating existing claim with these details {}", updateNotifiableClaimRequest);
    Map<String, Object> responseObject =
        notifiableClaimsDAO.updateNotifiableClaim(updateNotifiableClaimRequest);
    return new ResponseEntity<>(responseObject, HttpStatus.OK);
  }

}
