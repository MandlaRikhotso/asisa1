package za.co.oldmutual.asisa.refdata.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.custom.RefType;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;

@Getter
@Setter
public class ImpairmentCodeBean {

  @RefType(value = RefTypeEnum.IMPAIRMENT_CODE, message = "Impairment Code must be valid")
  private String code;

  private String description;

  @JsonIgnore
  private String impairmentCodeGroupId;
}
