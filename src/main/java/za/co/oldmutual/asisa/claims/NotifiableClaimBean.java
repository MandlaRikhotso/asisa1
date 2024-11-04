package za.co.oldmutual.asisa.claims;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.common.validation.custom.DateFormat;
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;

@Getter
@Setter
public class NotifiableClaimBean {

	private String notificationID;

	@JsonIgnore
	private Integer notifiableClaimID;

	@Valid
	private ClaimCategoryBean claimCategory;

	@DateFormat(message = "eventDate must be in YYYY-MM-DD format")
	private String eventDate;

	@Valid
	private ClaimCauseBean eventCause;

	private String eventDeathPlace;

	private String eventDeathCertificateNo;

	private String dha1663Number;

	@Valid
	private List<ImpairmentCodeBean> claimReason;

	@Valid
	private ClaimStatusBean claimStatus;

	@Valid
	private PaymentMethodBean paymentMethod;

	private String updateReason;

	@JsonIgnore
	private String claimCategoryCode;

	private String claimReasonCode;
}
