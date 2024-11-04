package za.co.oldmutual.asisa.enquiry;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "partyapi")
public class PartyAPIConfigProperties {

	private String url;

	@JsonIgnore
	private String headerClientId;

	@JsonIgnore
	private String headerClientSecret;
}
