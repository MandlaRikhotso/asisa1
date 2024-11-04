package za.co.oldmutual.asisa;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration("roles")
public class AuthorizationConfig {

	private static final String CLAIMS_ASSESSOR_AUTH="CLAIMS_ASSESSOR_AUTH";
	
	private static final String UNDERWRITER_AUTH="UNDERWRITER_AUTH";
	
	private String[] common = { "ENQUIRY","ENQUIRY_FULL","UNDERWRITER","CLAIMS_ASSESSOR",CLAIMS_ASSESSOR_AUTH, UNDERWRITER_AUTH};
	
	private List<String> authorizeClaim = Arrays.asList(CLAIMS_ASSESSOR_AUTH,UNDERWRITER_AUTH);
	
	private List<String>authorizeImpairment = Arrays.asList(CLAIMS_ASSESSOR_AUTH,UNDERWRITER_AUTH);
	
	private List<String> notifiableClaim = Arrays.asList(CLAIMS_ASSESSOR_AUTH,"CLAIMS_ASSESSOR");
	
	private List<String> enquiry = Arrays.asList(common);
	
	private List<String> notifiableImpairment = Arrays.asList(UNDERWRITER_AUTH,"UNDERWRITER");
	
	private List<String> ipHistory = Arrays.asList(common);
	
	private List<String> refData = Arrays.asList(common);
	
	private List<String> users = Arrays.asList(common);
	
}
