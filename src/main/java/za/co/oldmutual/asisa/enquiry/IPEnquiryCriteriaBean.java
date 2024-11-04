package za.co.oldmutual.asisa.enquiry;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.WhiteSpaceRemovalDeserializer;
import za.co.oldmutual.asisa.common.validation.custom.DateFormat;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;

@Getter
@Setter
public class IPEnquiryCriteriaBean {

  @JsonDeserialize(using=WhiteSpaceRemovalDeserializer.class)
  @Size(min = 0, max = 20, message = "identityNumber should be less than 20")
  private String identityNumber;

  
  @Valid
  private IdentityTypeBean identityType;

  @JsonDeserialize(using=WhiteSpaceRemovalDeserializer.class)
  @Size(min = 0, max = 60, message = "Surname should be less than 60")
  private String surname;

  @JsonDeserialize(using=WhiteSpaceRemovalDeserializer.class)
  @DateFormat(message = "dateOfBirth must be in dd/MM/yyyy format")
  private String dateOfBirth;
}
