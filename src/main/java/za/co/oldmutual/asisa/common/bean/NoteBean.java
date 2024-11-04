package za.co.oldmutual.asisa.common.bean;

import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoteBean {

  @JsonIgnore
  private String notificationID;

  @JsonIgnore
  private String personID;

  @Size(max = 800, message = "note text must be less than 800")
  private String noteText;

  private String scratchpad;
}
