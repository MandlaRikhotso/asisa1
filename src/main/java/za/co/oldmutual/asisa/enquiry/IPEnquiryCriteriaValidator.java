package za.co.oldmutual.asisa.enquiry;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class IPEnquiryCriteriaValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return IPEnquiryCriteriaBean.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    IPEnquiryCriteriaBean criteriaBean = (IPEnquiryCriteriaBean) target;
    if ((!(StringUtils.hasText(criteriaBean.getIdentityType().getCode())
        && StringUtils.hasText(criteriaBean.getIdentityNumber())))
        && (!(StringUtils.hasText(criteriaBean.getSurname())
            && StringUtils.hasText(criteriaBean.getDateOfBirth())))) {
      {
        errors.reject("Minimum Enquiry Criteria Required",
            "At least one combination ([ID Type and ID] OR [Surname and Data Of Birth]) required for enquiry");
      }
    }
  }

}
