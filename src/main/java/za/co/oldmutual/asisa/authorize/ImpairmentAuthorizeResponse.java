package za.co.oldmutual.asisa.authorize;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Getter
@Setter
public class ImpairmentAuthorizeResponse extends AuthorizeResponse {

  private ImpairmentCodeBean impairment;

  private String timeSignal;

  private String readings;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String notificationStatus;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private int notifiableImpairmentID;

  private String notificationID;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<LifeSpecBean> specialInvestigation;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<LifeSymbolBean> symbol;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String createdBy;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String createdDate;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String lifeOffice;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String policyNumber;
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PolicyTypeBean policyBenefit;

  private String reasonForEdit;

  @JsonIgnore
  private String specialInvestigationcode;

  @JsonIgnore
  private String symbolcode;
}
