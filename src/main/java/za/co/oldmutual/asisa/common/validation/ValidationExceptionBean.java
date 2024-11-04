package za.co.oldmutual.asisa.common.validation;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ValidationExceptionBean {

  private List<GlobalErrorBean> globalErrors;

  private List<FieldErrorBean> fieldErrors;
}
