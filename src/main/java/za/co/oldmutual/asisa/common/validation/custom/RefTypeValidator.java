package za.co.oldmutual.asisa.common.validation.custom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class RefTypeValidator implements ConstraintValidator<RefType, String> {

  private RefTypeEnum refType;

  @Autowired
  ReferenceDataCache referenceDataCache;

  @Override
  public void initialize(RefType constraint) {
    refType = constraint.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext cxt) {
    if (!StringUtils.hasText(value)) {
      return true;
    } else {
      return referenceDataCache.isCodeValid(value, refType);
    }
  }

}
