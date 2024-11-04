package za.co.oldmutual.asisa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import za.co.oldmutual.asisa.ldap.ActiveDirectoryAuthenticationProvider;
import za.co.oldmutual.asisa.ldap.AsisaUserDetailsContextMapper;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Value("${ldap.domain}")
	private String domain;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable()
	        .authorizeRequests()
	            .antMatchers("/api/**/").permitAll()
	            .anyRequest().authenticated()
	            .and()
	         .authorizeRequests()
	         	.antMatchers("/api/readiness","/api/liveness").permitAll()
	         	.and()
	        .formLogin()
	        	.defaultSuccessUrl("/")
	            .permitAll()
	            .and()
	        .logout()
	        	.logoutUrl("/logout")
	        	.invalidateHttpSession(true)
	        	.deleteCookies("JSESSIONID")
	            .permitAll()
	            .and()
		        .sessionManagement().maximumSessions(1);
	}

	@Bean
	public ActiveDirectoryAuthenticationProvider adAuthenticationProvider(NamedParameterJdbcOperations namedParameterJdbcTemplate) {
		ActiveDirectoryAuthenticationProvider adAuthenticationProvider = new ActiveDirectoryAuthenticationProvider(
				domain, namedParameterJdbcTemplate);
		adAuthenticationProvider.setUserDetailsContextMapper(userDetailsContextMapper());
		return adAuthenticationProvider;
	}

	@Bean
	public AsisaUserDetailsContextMapper userDetailsContextMapper() {
		return new AsisaUserDetailsContextMapper();
	}
}
