package za.co.oldmutual.asisa.ldap;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import za.co.oldmutual.asisa.AbstractTest;

public class AsisaUserDetailsContextMapperTest extends AbstractTest{

	@InjectMocks
	AsisaUserDetailsContextMapper asisaUserDetailsContextMapper;
	
	@Mock
	LdapUserDetails ldapUserDetails;
	
	@Test
	public void mapUserFromContextTest() {
		AsisaUserDetails obj = new AsisaUserDetails(ldapUserDetails, "john", "abc", "bcd@oldmutual.com", "IT", "Manager");
		assertNotNull(obj);
	}
	
}
