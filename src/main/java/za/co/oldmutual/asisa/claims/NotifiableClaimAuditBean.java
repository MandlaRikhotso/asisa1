package za.co.oldmutual.asisa.claims;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifiableClaimAuditBean extends NotifiableClaimBean {

  private String notificationStatus;

  private String notificationSource;

  private String notificationTxnType;

  private String updateReason;

  private String sentToAstute;

  private String astuteRefNo;

  private String isActive;

  private String createdBy;

  private Date createdDate;

  private int notifiableClaimRefID;

}
