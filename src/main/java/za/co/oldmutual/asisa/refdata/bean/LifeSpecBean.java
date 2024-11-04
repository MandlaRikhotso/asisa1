package za.co.oldmutual.asisa.refdata.bean;

import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.custom.RefType;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;

@Getter
@Setter
public class LifeSpecBean {

  @RefType(value = RefTypeEnum.LIFE_SPEC, message = "Special Investigation Code must be valid")
  private String code;

  private String description;
}
