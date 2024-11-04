package za.co.oldmutual.asisa.iphistory;

import java.util.List;
import org.springframework.lang.Nullable;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;

@Getter
@Setter
public class IPHistoryBean {

  private InsuredPersonBean insuredPerson;

  @Nullable
  private List<IPImpairmentHistoryBean> impairmentHistory;

  @Nullable
  private List<IPClaimHistoryBean> claimHistory;

  private List<IPNoteHistoryBean> noteHistory;

  private List<IPNoteHistoryBean> scratchpadHistory;
}
