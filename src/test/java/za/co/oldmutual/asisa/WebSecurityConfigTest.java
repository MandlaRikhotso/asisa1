package za.co.oldmutual.asisa;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.ldap.ActiveDirectoryAuthenticationProvider;

public class WebSecurityConfigTest extends AbstractTest {
	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@InjectMocks
	WebSecurityConfig webSecurityConfig;

	@Test
	public void ActiveDirectoryAuthenticationProviderTest() {
		String domain = "abc";
		ActiveDirectoryAuthenticationProvider obj = new ActiveDirectoryAuthenticationProvider(domain,
				namedParameterJdbcTemplate);
		assertNotNull(obj);
	}

}
