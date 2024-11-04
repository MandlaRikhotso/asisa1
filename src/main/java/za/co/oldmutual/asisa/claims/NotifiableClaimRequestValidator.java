package za.co.oldmutual.asisa.claims;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class NotifiableClaimRequestValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return NotifiableClaimsRequest.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    NotifiableClaimsRequest notifiableClaimsRequest = (NotifiableClaimsRequest) target;
    validatingNotifiableClaimsRequest(errors, notifiableClaimsRequest);
  }

  private void validatingNotifiableClaimsRequest(Errors errors,
      NotifiableClaimsRequest notifiableClaimsRequest) {
    notifiableClaimsRequest.getNotifications()
        .forEach(notificationBean -> validatingNotifiableClaim(errors, notificationBean));
  }

  private void validatingNotifiableClaim(Errors errors, ClaimNotificationBean notificationBean) {
    notificationBean.getNotifiableClaims().forEach(notifiableClaim -> {
      if (StringUtils.hasText(notificationBean.getPolicyBenefit().getCode())
          && (notificationBean.getPolicyBenefit().getCode().equals("1")
              || notificationBean.getPolicyBenefit().getCode().equals("35"))
          && (StringUtils.isEmpty(notifiableClaim.getEventDeathPlace()))) {
        errors.reject("Place Of Death is Required");
      }

      if (StringUtils.hasText(notificationBean.getPolicyBenefit().getCode())) {
        if (!notificationBean.getPolicyBenefit().getCode().equals("411")
            && StringUtils.isEmpty(notifiableClaim.getClaimReason())) {
          errors.reject("Claim Reason is Required");
        } else if (notificationBean.getPolicyBenefit().getCode().equals("411")
            && !StringUtils.isEmpty(notifiableClaim.getClaimReason())) {
          errors.reject(
              "If Claim Type 'RETRENCHMENT' is selected, then Claim Reason(s) will NOT be able to be captured");
        }
      }
    });
  }
}
