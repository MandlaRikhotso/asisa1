package za.co.oldmutual.asisa.ldap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.util.StringUtils;

import za.co.oldmutual.asisa.user.UserDAOQueries;

/**
 * Customized class logic taken from ActiveDirectoryLdapAuthenticationProvider
 * Please Refer -
 * org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider
 */
public final class ActiveDirectoryAuthenticationProvider extends AbstractLdapAuthenticationProvider {
	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([0-9a-f]{3,4}).*");

	private static final String[] returnAttributes = { "sAMAccountName", "givenName", "sn", "name", "title", "cn", "mail",
			"mobile", "manager", "company", "memberof", "department" };

	private final String domain;

	private NamedParameterJdbcOperations namedParameterJdbcTemplate;

	private Map<String, Object> contextEnvironmentProperties = new HashMap<>();

	// Only used to allow tests to substitute a mock LdapContext
	ContextFactory contextFactory = new ContextFactory();

	private static final int USERNAME_NOT_FOUND = 0x525;
	private static final int INVALID_PASSWORD = 0x52e;
	private static final int NOT_PERMITTED = 0x530;
	private static final int PASSWORD_EXPIRED = 0x532;
	private static final int ACCOUNT_DISABLED = 0x533;
	private static final int ACCOUNT_EXPIRED = 0x701;
	private static final int PASSWORD_NEEDS_RESET = 0x773;
	private static final int ACCOUNT_LOCKED = 0x775;

	public ActiveDirectoryAuthenticationProvider(String domain,
			NamedParameterJdbcOperations namedParameterJdbcTemplate) {
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
		String username = auth.getName().toUpperCase();
		String password = (String) auth.getCredentials();
		DirContext dirContext = bindAsUser(username, password);
		try {
			return searchForUser(dirContext, username);
		} catch (NamingException e) {
			logger.error("Failed to locate directory entry for authenticated user: " + username, e);
			throw badCredentials(e);
		} finally {
			LdapUtils.closeContext(dirContext);
		}
	}

	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username,
			String password) {
		String role = namedParameterJdbcTemplate.queryForObject(UserDAOQueries.GET_USER_ROLE_QUERY,
				new MapSqlParameterSource("username", username), String.class);
		List<String> activityCode = namedParameterJdbcTemplate.queryForList(UserDAOQueries.GET_USER_RIGHTS_QUERY,
				new MapSqlParameterSource("role", role), String.class);

		if (role == null) {
			logger.debug("Not Authorized to access ASISA Registers application");
			return AuthorityUtils.NO_AUTHORITIES;
		}
		if (activityCode.isEmpty()) {
			logger.debug("No values for 'memberOf' attribute. User not configured properly");
			return AuthorityUtils.NO_AUTHORITIES;
		}

		ArrayList<GrantedAuthority> authorities = new ArrayList<>(1);
		authorities.add(new AsisaGrantedAuthority(role, activityCode));
		return authorities;
	}

	private DirContext bindAsUser(String username, String password) {
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, username + "@" + domain);
		env.put(Context.PROVIDER_URL, "LDAP://" + domain);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());
		env.putAll(this.contextEnvironmentProperties);

		try {
			return contextFactory.createContext(env);
		} catch (AuthenticationException | OperationNotSupportedException e) {			
				handleBindException(username, e);
				throw badCredentials(e);
		}catch(NamingException e) {
				throw LdapUtils.convertLdapException(e);
			}		
	}

	private DirContextOperations searchForUser(DirContext context, String username) throws NamingException {
		String baseFilter = "(&((&(objectCategory=Person)(objectClass=User)))(samaccountname=" + username + "))";
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchControls.setReturningAttributes(returnAttributes);
		String domainBase = getDomainBase(domain);
		try {
			return SpringSecurityLdapTemplate.searchForSingleEntryInternal(context, searchControls, domainBase,
					baseFilter, new Object[] { username });
		} catch (IncorrectResultSizeDataAccessException incorrectResults) {
			// Search should never return multiple results if properly configured - just
			// rethrow
			if (incorrectResults.getActualSize() != 0) {
				throw incorrectResults;
			}
			// If we found no results, then the username/password did not match
			UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException(
					"User " + username + " not found in directory.", incorrectResults);
			throw badCredentials(userNameNotFoundException);
		}
	}

	private String getDomainBase(String base) {
		char[] namePair = base.toUpperCase().toCharArray();
		String dn = "DC=";
		for (int i = 0; i < namePair.length; i++) {
			if (namePair[i] == '.') {
				dn += ",DC=" + namePair[++i];
			} else {
				dn += namePair[i];
			}
		}
		return dn;
	}

	private void handleBindException(String bindPrincipal, NamingException exception) {
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication for " + bindPrincipal + " failed:" + exception);
		}
		handleResolveObj(exception);
		int subErrorCode = parseSubErrorCode(exception.getMessage());
		if (subErrorCode <= 0) {
			logger.debug("Failed to locate AD-specific sub-error code in message");
			return;
		}
		logger.info("Active Directory authentication failed: " + subCodeToLogMessage(subErrorCode));
	}

	private void handleResolveObj(NamingException exception) {
		Object resolvedObj = exception.getResolvedObj();
		boolean serializable = resolvedObj instanceof Serializable;
		if (resolvedObj != null && !serializable) {
			exception.setResolvedObj(null);
		}
	}

	private int parseSubErrorCode(String message) {
		Matcher m = SUB_ERROR_CODE.matcher(message);
		if (m.matches()) {
			return Integer.parseInt(m.group(1), 16);
		}
		return -1;
	}

	private String subCodeToLogMessage(int code) {
		switch (code) {
		case USERNAME_NOT_FOUND:
			return "User was not found in directory";
		case INVALID_PASSWORD:
			return "Supplied password was invalid";
		case NOT_PERMITTED:
			return "User not permitted to logon at this time";
		case PASSWORD_EXPIRED:
			return "Password has expired";
		case ACCOUNT_DISABLED:
			return "Account is disabled";
		case ACCOUNT_EXPIRED:
			return "Account expired";
		case PASSWORD_NEEDS_RESET:
			return "User must reset password";
		case ACCOUNT_LOCKED:
			return "Account locked";
		default:
			return "Unknown (error code " + Integer.toHexString(code) + ")";
		}
		
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(
				messages.getMessage("LdapAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		return (BadCredentialsException) badCredentials().initCause(cause);
	}

	static class ContextFactory {
		DirContext createContext(Hashtable<?, ?> env) throws NamingException {
			return new InitialLdapContext(env, null);
		}
	}

}
