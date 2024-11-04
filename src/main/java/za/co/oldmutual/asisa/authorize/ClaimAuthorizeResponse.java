package za.co.oldmutual.asisa.authorize;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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
public class ClaimAuthorizeResponse extends AuthorizeResponse {

	private List<ImpairmentCodeBean> claimReason;

	private String dha1663Number;

	private ClaimCauseBean causeOfEvent;

	@ApiModelProperty("On UI it represents Claim Policy Type")
	private PolicyTypeBean policyBenefit;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ClaimStatusBean claimStatus;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ClaimCategoryBean claimCategory;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String notificationID;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String policyNumber;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String createdDate;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String createdBy;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String lifeOffice;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String eventDate;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String placeOfDeath;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String deathCertificateNumber;

	private String updateReason;

	@JsonIgnore
	private String claimCategoryCode;

	@JsonIgnore
	private String claimReasonCode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String notificationStatus;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String notificationSource;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String notificationTxnType;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private PaymentMethodBean paymentMethod;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String astuteRefNo;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String isActive;
}
