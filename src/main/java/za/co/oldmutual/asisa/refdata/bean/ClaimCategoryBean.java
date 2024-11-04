package za.co.oldmutual.asisa.refdata.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.custom.RefType;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;

@Getter
@Setter
public class ClaimCategoryBean {

  @RefType(value = RefTypeEnum.CLAIM_CATEGORY, message = "Claim Category Code must be valid")
  private String code;

  private String description;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String claimType;

}
