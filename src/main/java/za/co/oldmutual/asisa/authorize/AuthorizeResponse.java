package za.co.oldmutual.asisa.authorize;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;

@Getter
@Setter
public class AuthorizeResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String identityNumber;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private IdentityTypeBean identityType;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String notificationID;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String policyNumber;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String transType;
}
