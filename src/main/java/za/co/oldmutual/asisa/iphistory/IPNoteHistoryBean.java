package za.co.oldmutual.asisa.iphistory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.oldmutual.asisa.common.bean.NoteBean;

@Getter
@Setter
@NoArgsConstructor
public class IPNoteHistoryBean extends NoteBean {

  private int noteId;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String policyNumber;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String notificationSource;

  private String dateCreated;

  private String createdBy;

  private String omUserId;

  private String businessUnit;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String notificationID;

  private String role;
}
