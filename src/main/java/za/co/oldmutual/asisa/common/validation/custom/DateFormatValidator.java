package za.co.oldmutual.asisa.common.validation.custom;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {
  @Override
  public void initialize(DateFormat dateFormat) {
    // Do nothing
  }

  @Override
  public boolean isValid(String date, ConstraintValidatorContext cxt) {
    boolean isValid = false;
    String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
    Pattern dateFormatPattern = Pattern.compile(regex);
    if (!StringUtils.hasText(date)) {
      isValid = true;
    }
    if (dateFormatPattern.matcher(date).matches()) {
      isValid = true;
    }
    return isValid;
  }

}
