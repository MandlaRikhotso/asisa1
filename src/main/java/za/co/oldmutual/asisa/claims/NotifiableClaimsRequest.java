package za.co.oldmutual.asisa.claims;

import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;

@Getter
@Setter
public class NotifiableClaimsRequest {

  @Valid
  private List<ClaimNotificationBean> notifications;

  @Valid
  private InsuredPersonBean insuredPerson;
}
