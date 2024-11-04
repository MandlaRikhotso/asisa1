package za.co.oldmutual.asisa.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractNotificationBean {

  @JsonIgnore
  private String notificationID;

  @JsonIgnore
  private String insuredPersonID;

  @JsonIgnore
  private String notificationTypeCode;

  @JsonIgnore
  private String notificationStatusCode;

  @JsonIgnore
  private String notificationSourceCode;

  @JsonIgnore
  private String notificationTxnType;

  @JsonIgnore
  private String createdBy;
}
