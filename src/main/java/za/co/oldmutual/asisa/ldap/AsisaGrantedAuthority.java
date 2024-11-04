package za.co.oldmutual.asisa.ldap;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

public class AsisaGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final String role;

	private final List<String> activityCode;
	
	
	public AsisaGrantedAuthority(String role, List<String> activityCode) {
		Assert.hasText(role, "A granted authority textual representation is required");
		this.role = role;
		this.activityCode = activityCode;
		
	}

	@Override
	public String getAuthority() {
		return role;
	}

	public List<String> getActivityCode() {
		return activityCode;
	}

	@Override
	public String toString() {
		return "The " + this.role + " has rights: " + this.activityCode;
	}
}
