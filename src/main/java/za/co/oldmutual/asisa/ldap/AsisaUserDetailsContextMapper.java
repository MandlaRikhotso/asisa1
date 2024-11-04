package za.co.oldmutual.asisa.ldap;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.util.StringUtils;

public class AsisaUserDetailsContextMapper extends LdapUserDetailsMapper {

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		UserDetails details = super.mapUserFromContext(ctx, username.toUpperCase(), authorities);

		return new AsisaUserDetails((LdapUserDetails) details, ctx.getStringAttribute("givenname"),
				ctx.getStringAttribute("sn"), ctx.getStringAttribute("mail"), ctx.getStringAttribute("department"),
				extractManagerName(ctx.getStringAttribute("manager")));
	}

	private String extractManagerName(String inputString) {
		try {
			
			return StringUtils.split(inputString, ",")[0].substring(3);
		} catch (Exception e) {
			return inputString;
		}
	}

}
