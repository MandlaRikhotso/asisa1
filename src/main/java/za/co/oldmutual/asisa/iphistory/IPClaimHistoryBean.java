package za.co.oldmutual.asisa.iphistory;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Getter
@Setter
public class IPClaimHistoryBean {

  private Integer notifiableClaimID;

  private String policyNumber;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PolicyTypeBean claimType;

  private String dateCreated;

  private String notificationSource;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ClaimCategoryBean> claimCategory;

  private String eventDate;

  private ClaimCauseBean eventCause;

  private String eventDeathPlace;

  private String eventDeathCertificateNo;

  private String dha1663Number;

  private List<ImpairmentCodeBean> claimReason;

  private ClaimStatusBean claimStatus;

  private PaymentMethodBean paymentMethod;

  private String updateReason;

  private String notificationStatus;

  private String createdBy;

  private String userRole;

  private String businessUnit;

  private String notificationID;
}
