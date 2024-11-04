package za.co.oldmutual.asisa.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GlobalErrorBean {

  private String code;

  private String message;
}
