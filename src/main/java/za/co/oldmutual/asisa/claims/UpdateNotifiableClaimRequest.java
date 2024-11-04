package za.co.oldmutual.asisa.claims;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.NoteBean;

@Setter
@Getter
public class UpdateNotifiableClaimRequest extends NotifiableClaimBean {

  private boolean isDelete;

  private List<NoteBean> notes;
}
