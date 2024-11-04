package za.co.oldmutual.asisa.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "email.notification")
public class EmailConfigProperties {

	private boolean enabled;

	private String fromAddress;

	private String updateMailSubject;

	private String deleteMailSubject;

	private String updateRejectMailSubject;

	private String deletRejectMailSubject;

}
