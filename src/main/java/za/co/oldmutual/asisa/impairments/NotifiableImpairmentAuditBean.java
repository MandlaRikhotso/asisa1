package za.co.oldmutual.asisa.impairments;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifiableImpairmentAuditBean extends NotifiableImpairmentBean {

  private long notifiableImpairmentAuditId;

  private String uuidNumber;

  private String notifiableStatusCode;

  private String notificationTxnType;

  private String sentToAstute;

  private String astuteRefNo;

  private String createdBy;

  private Date createdDate;

  private String isActive = "N";
}
