package za.co.oldmutual.asisa.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessages {

  private String code;

  private String field;

  private String level;

  private String message;

  private String moreInfo;
}
