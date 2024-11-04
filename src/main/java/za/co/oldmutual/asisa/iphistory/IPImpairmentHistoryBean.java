package za.co.oldmutual.asisa.iphistory;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Getter
@Setter
public class IPImpairmentHistoryBean {

  protected Integer notifiableImpairmentID;

  private ImpairmentCodeBean impairment;

  private String timeSignal;

  private String readings;

  private List<LifeSpecBean> specialInvestigation;

  private List<LifeSymbolBean> symbol;

  protected String updateReason;

  private String policyNumber;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PolicyTypeBean policyBenefit;

  private String dateCreated;

  private String notificationSource;

  private String notificationStatus;

  private String createdBy;

  private String userRole;

  private String businessUnit;

  private String notificationID;
}
