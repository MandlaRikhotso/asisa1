package za.co.oldmutual.asisa.impairments;

import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;

@Getter
@Setter
public class NotifiableImpairmentsRequest {

  @Valid
  private List<ImpairmentNotificationBean> notifications;

  @Valid
  private InsuredPersonBean insuredPerson;
}
