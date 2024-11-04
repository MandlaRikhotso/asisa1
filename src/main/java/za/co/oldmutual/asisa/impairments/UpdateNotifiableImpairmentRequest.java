package za.co.oldmutual.asisa.impairments;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.NoteBean;

@Getter
@Setter
public class UpdateNotifiableImpairmentRequest extends NotifiableImpairmentBean {

  private boolean isDelete = false;

  private List<NoteBean> notes;
}
