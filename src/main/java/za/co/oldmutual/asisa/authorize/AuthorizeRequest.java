package za.co.oldmutual.asisa.authorize;

import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizeRequest {

  @ApiModelProperty(value = "Approve / Reject")
  @Size(min = 1, max = 20, message = "action value must be either Approve / Reject")
  private String action;

  @ApiModelProperty(value = "Original Claim Id / Impairment Id")
  @Size(min = 1, max = 40, message = "referenceId must be atleast 1 and less than 40")
  private String notificationID;

  @Size(max = 800, message = "actionRemark must be less than 800")
  private String actionRemark;

  @ApiModelProperty(value = "Update / Delete")
  @Size(min = 1, max = 20, message = "transType value must be either Approve / Reject")
  private String transType;

}
