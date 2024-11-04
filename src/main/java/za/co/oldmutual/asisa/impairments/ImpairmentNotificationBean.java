package za.co.oldmutual.asisa.impairments;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.springframework.lang.Nullable;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.AbstractNotificationBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Getter
@Setter
public class ImpairmentNotificationBean extends AbstractNotificationBean {

  @Size(min = 1, max = 20, message = "policy number must be atleast 1 and less than 20")
  @Nullable
  private String policyNumber;

  @Valid
  private PolicyTypeBean policyBenefit;

  @Valid
  private List<NoteBean> notes;

  @Valid
  private List<NotifiableImpairmentBean> notifiableImpairments;
}
