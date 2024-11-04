package za.co.oldmutual.asisa.enquiry;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;

@RestController
@RequestMapping("/api/enquiry")
@PostAuthorize(value = "hasAnyAuthority(@roles.enquiry)")
public class IPEnquiryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPEnquiryController.class);

	@Autowired
	private IPEnquiryDAO iPEnquiryDAO;

	@Autowired
	private IPEnquiryCriteriaValidator iPEnquiryCriteriaValidator;

	@Autowired
	private PartyAPIConfigProperties partyAPIConfigProperties;

	@InitBinder("IPEnquiryCriteriaBean")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(iPEnquiryCriteriaValidator);
	}

	@ApiOperation(value = "Returns list of Insured Persons for the given IP Enquiry criteria")
	@PostMapping("/enquireInsuredPersons")
	public ResponseEntity<Object> enquireInsuredPersons(@Valid @RequestBody IPEnquiryCriteriaBean iPEnquiryCriteriaBean)
			throws ResourceNotFoundException {
		LOGGER.info("Initiating InsuredPersonBean enquiry with details [{}]", iPEnquiryCriteriaBean);
		return new ResponseEntity<>(iPEnquiryDAO.getInsuredPersons(iPEnquiryCriteriaBean), HttpStatus.OK);
	}

	@ApiOperation(value = "Returns a Yes/No response for the given IP Enquiry criteria")
	@PostMapping("/enquireInsuredPersonExists")
	public ResponseEntity<Object> enquireInsuredPersonExists(@Valid @RequestBody IPEnquiryCriteriaBean ipSearchCriteria)
			throws ResourceNotFoundException {
		LOGGER.info("Initiating InsuredPerson Search with details [{}]", ipSearchCriteria);
		return new ResponseEntity<>(iPEnquiryDAO.isInsuredPersonExists(ipSearchCriteria), HttpStatus.OK);
	}

	@PostMapping("/party/gcs")
	public ResponseEntity<Object> searchBygcsClientId(@RequestBody PartySearchCriteriaBean partySearchCriteriaBean)
			throws JsonProcessingException, ResourceNotFoundException {
		LOGGER.info("Initiating InsuredPerson Search by GCS with details [{}]", partySearchCriteriaBean);

		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("x-ibm-client-id", partyAPIConfigProperties.getHeaderClientId());
			headers.set("x-ibm-client-secret", partyAPIConfigProperties.getHeaderClientSecret());
			Map<String, PartySearchCriteriaBean> body = new HashMap<>();
			body.put("PartySearchCriteria", partySearchCriteriaBean);
			ObjectMapper mapper = new ObjectMapper();

			HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(body), headers);

			return new ResponseEntity<>(
					restTemplate.postForObject(partyAPIConfigProperties.getUrl(), request, Object.class),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResourceNotFoundException();
		}
	}

}
