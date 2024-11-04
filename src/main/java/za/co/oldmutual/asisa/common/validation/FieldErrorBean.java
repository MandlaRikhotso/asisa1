package za.co.oldmutual.asisa.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldErrorBean {

  private String code;

  private String field;

  private String rejectedValue;

  private String message;
}
