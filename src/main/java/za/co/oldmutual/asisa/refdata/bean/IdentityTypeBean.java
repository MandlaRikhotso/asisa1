package za.co.oldmutual.asisa.refdata.bean;

import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.custom.RefType;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;

@Getter
@Setter
public class IdentityTypeBean {

  @RefType(value = RefTypeEnum.IDTYPE, message = "Identity Type must be valid")
  private String code;

  private String description;
}
