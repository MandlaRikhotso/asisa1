package za.co.oldmutual.asisa.enquiries;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.enquiry.IPEnquiryController;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaBean;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.enquiry.PartyAPIConfigProperties;
import za.co.oldmutual.asisa.enquiry.PartySearchCriteriaBean;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;

public class IPEnquiryControllerTest extends AbstractTest {
	@Mock
	ReferenceDataCache referenceDataCache;

	@Mock
	RefDataDAO refDataDAO;

	@Mock
	IPEnquiryDAO iPEnquiryDAO;

	@Mock
	RestTemplate restTemplate;

	@Mock
	PartyAPIConfigProperties partyConfigBean;

	@InjectMocks
	IPEnquiryController iPEnquiryController;

	IPEnquiryCriteriaBean iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();
	IdentityTypeBean identityTypeBean = new IdentityTypeBean();
	public Errors errors;
	List<InsuredPersonBean> dummyIPEnquiryResponseforPerfectMatch = CommonTestData
			.getDummyIPEnquiryResponseforPerfectMatch();
	List<InsuredPersonBean> dummyIPEnquiryResponseforIMperfectMatch = CommonTestData
			.getDummyIPEnquiryResponseForImPerfectMatch();

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
		Mockito.when(referenceDataCache.isCodeValid("4", RefTypeEnum.IDTYPE)).thenReturn(true);
	}

	@Test
	public void enquiryBySurnameAndDateOfBirth() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("");
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		Mockito.when(iPEnquiryDAO.getInsuredPersons(Mockito.any(IPEnquiryCriteriaBean.class)))
				.thenReturn(dummyIPEnquiryResponseforPerfectMatch);
		ResponseEntity<Object> result = iPEnquiryController.enquireInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		String content = result.getBody().toString();
		assertEquals(dummyIPEnquiryResponseforPerfectMatch.toString(), content);
	}

	@Test
	public void enquiryByIdTypeAndIdNumber() throws Exception {
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		iPEnquiryCriteriaBean.setIdentityNumber("1234567890123");
		identityTypeBean.setCode("4");
		identityTypeBean.setDescription("ID NUMBER");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		Mockito.when(iPEnquiryDAO.getInsuredPersons(Mockito.any(IPEnquiryCriteriaBean.class)))
				.thenReturn(dummyIPEnquiryResponseforPerfectMatch);
		ResponseEntity<Object> result = iPEnquiryController.enquireInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		String content = result.getBody().toString();
		assertEquals(dummyIPEnquiryResponseforPerfectMatch.toString(), content);
	}

	@Test
	public void enquiryBySurnameAndDateOfBirthForImperfectMatch() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("");
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kum");
		Mockito.when(iPEnquiryDAO.getInsuredPersons(Mockito.any(IPEnquiryCriteriaBean.class)))
				.thenReturn(dummyIPEnquiryResponseforIMperfectMatch);
		ResponseEntity<Object> result = iPEnquiryController.enquireInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		String content = result.getBody().toString();
		assertEquals(dummyIPEnquiryResponseforIMperfectMatch.toString(), content);
	}

	@Test
	public void enquiryByIdTypeAndIdNumberForImperfectMatch() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("12345678901");
		identityTypeBean.setCode("4");
		identityTypeBean.setDescription("ID NUMBER");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("");
		iPEnquiryCriteriaBean.setSurname("");
		Mockito.when(iPEnquiryDAO.getInsuredPersons(Mockito.any(IPEnquiryCriteriaBean.class)))
				.thenReturn(dummyIPEnquiryResponseforIMperfectMatch);
		ResponseEntity<Object> result = iPEnquiryController.enquireInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		String content = result.getBody().toString();
		assertEquals(dummyIPEnquiryResponseforIMperfectMatch.toString(), content);
	}

	@Test
	public void checkBySurnameAndDateOfBirth() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("");
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		Mockito.when(iPEnquiryDAO.isInsuredPersonExists(Mockito.any(IPEnquiryCriteriaBean.class))).thenReturn(true);
		ResponseEntity<Object> result = iPEnquiryController.enquireInsuredPersonExists(iPEnquiryCriteriaBean);
		assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		String content = result.getBody().toString();
		String expected = "true";
		assertEquals(expected, content);
	}

	@Test
	public void checkByIdTypeAndIdNumber() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("1234567890123");
		identityTypeBean.setCode("4");
		identityTypeBean.setDescription("ID NUMBER");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("");
		iPEnquiryCriteriaBean.setSurname("");
		Mockito.when(iPEnquiryDAO.isInsuredPersonExists(Mockito.any(IPEnquiryCriteriaBean.class))).thenReturn(true);
		ResponseEntity<Object> result = iPEnquiryController.enquireInsuredPersonExists(iPEnquiryCriteriaBean);
		assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
		String content = result.getBody().toString();
		String expected = "true";
		assertEquals(expected, content);
	}

	@Test
	public void invalidDateOfBirth() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("");
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009123");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		try {
			iPEnquiryController.enquireInsuredPersons(iPEnquiryCriteriaBean);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}

	@Test
	public void invalidEnquiryRequest() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("12345678901");
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("");
		try {
			iPEnquiryController.enquireInsuredPersons(iPEnquiryCriteriaBean);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}

	@Test
	public void searchBygcsClientIdTest() throws JsonProcessingException, ResourceNotFoundException {

		String gcsURL = "https://apigwint.dev.za.omlac.net/omem/dev-int/insurance/life/party/v9/persons";
		String gcsHeaderClientId = "9b928004-1c88-4d7e-b659-49a04814f19e";
		String gcsHeaderClientSecret = "G5yR7iR5uM8yY5mV1xG1qW8qE0wY4uR4oX5jS2kL3vR8xO1uE0";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ibm-client-id", gcsHeaderClientId);
		headers.set("x-ibm-client-secret", gcsHeaderClientSecret);

		PartySearchCriteriaBean partySearchCriteriaBean = new PartySearchCriteriaBean();
		Map<String, PartySearchCriteriaBean> body = new HashMap<>();
		partySearchCriteriaBean.setPartyID("acd");
		body.put("PartySearchCriteria", partySearchCriteriaBean);

		Mockito.when(partyConfigBean.getUrl()).thenReturn(gcsURL);
		Mockito.when(partyConfigBean.getHeaderClientId()).thenReturn(gcsHeaderClientId);
		Mockito.when(partyConfigBean.getHeaderClientSecret()).thenReturn(gcsHeaderClientSecret);
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(body), headers);

		Mockito.when(restTemplate.postForObject(Mockito.eq(gcsURL), Mockito.any(HttpEntity.class), Mockito.any()))
				.thenReturn("GCS Client");
		ResponseEntity<Object> actual = iPEnquiryController.searchBygcsClientId(partySearchCriteriaBean);
		assertNotNull(actual.getBody());
	}

	@Test
	public void searchBygcsClientIdExceptionTest() {
		PartySearchCriteriaBean partySearchCriteriaBean = new PartySearchCriteriaBean();
		partySearchCriteriaBean.setPartyID("");
		try {
			iPEnquiryController.searchBygcsClientId(partySearchCriteriaBean);
		} catch (JsonProcessingException | ResourceNotFoundException e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}
}
