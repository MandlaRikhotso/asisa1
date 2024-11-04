package za.co.oldmutual.asisa.impairments;

import java.util.List;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;

@Getter
@Setter

public class NotifiableImpairmentBean {

  private String notificationID;

  protected Integer notifiableImpairmentID;

  @Valid
  private ImpairmentCodeBean impairment;

  @Valid
  private String timeSignal;

  private String readings;

  @Valid
  private List<LifeSpecBean> specialInvestigation;

  @Valid
  private List<LifeSymbolBean> symbol;

  protected String updateReason;

  @JsonIgnore
  private String specialInvestigationcode;

  @JsonIgnore
  private String symbolcode;
}
