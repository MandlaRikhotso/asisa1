package za.co.oldmutual.asisa.user;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBean {

  @NotBlank
  @Size(min = 0, max = 20, message = "OM User ID should be less than 20")
  private String omUserId;

  @NotBlank
  @Size(min = 0, max = 20, message = "roleCode should be less than 20")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String roleCode;

  @NotBlank
  @Size(min = 0, max = 20, message = "businessUnit should be less than 20")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String businessUnit;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String> activityCode;
}

