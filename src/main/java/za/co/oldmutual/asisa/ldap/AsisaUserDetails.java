package za.co.oldmutual.asisa.ldap;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AsisaUserDetails implements LdapUserDetails {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	@JsonIgnore
	private LdapUserDetails details;

	private String givenName;

	private String surname;

	private String emailID;

	private String department;

	private String manager;

	public AsisaUserDetails(LdapUserDetails details, String givenName, String surname, String emailID,
			String department, String manager) {
		this.details = details;
		this.givenName = givenName;
		this.surname = surname;
		this.emailID = emailID;
		this.department = department;
		this.manager = manager;
	}

	public boolean isEnabled() {
		return details.isEnabled();
	}

	public String getDn() {
		return details.getDn();
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return details.getAuthorities();
	}

	public String getPassword() {
		return details.getPassword();
	}

	public String getUsername() {
		return details.getUsername();
	}

	public boolean isAccountNonExpired() {
		return details.isAccountNonExpired();
	}

	public boolean isAccountNonLocked() {
		return details.isAccountNonLocked();
	}

	public boolean isCredentialsNonExpired() {
		return details.isCredentialsNonExpired();
	}

	@Override
	public void eraseCredentials() {
		details.eraseCredentials();
	}

}
