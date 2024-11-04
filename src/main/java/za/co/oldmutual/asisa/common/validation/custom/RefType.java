package za.co.oldmutual.asisa.common.validation.custom;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;

@Documented
@Constraint(validatedBy = RefTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RefType {

  String message() default "Invalid Reference Type";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  RefTypeEnum value() default RefTypeEnum.EMPTY;
}
