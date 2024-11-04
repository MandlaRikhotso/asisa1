package za.co.oldmutual.asisa.common.bean;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.WhiteSpaceRemovalDeserializer;
import za.co.oldmutual.asisa.common.validation.custom.DateFormat;
import za.co.oldmutual.asisa.refdata.bean.GenderBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.TitleBean;

@Getter
@Setter

public class InsuredPersonBean {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String personID;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Size(min = 0, max = 60, message = "Surname should be less than 60")
  private String surname;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @DateFormat(message = "dateOfBirth must be in dd/MM/YYYY format")
  private String dateOfBirth;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private GenderBean gender;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Size(min = 0, max = 20, message = "identityNumber should be less than 20")
  private String identityNumber;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private IdentityTypeBean identityType;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Valid
  private TitleBean title;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Size(min = 2, max = 20, message = "givenName1 should be atleast 2 and less than 20")
  private String givenName1;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String givenName2;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String givenName3;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String addressLine1;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String addressLine2;

  @JsonDeserialize(using = WhiteSpaceRemovalDeserializer.class)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String addressLine3;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer postalCode;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private boolean perfectMatch;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String createdBy;
  
  @JsonIgnore
  private String source;
}
