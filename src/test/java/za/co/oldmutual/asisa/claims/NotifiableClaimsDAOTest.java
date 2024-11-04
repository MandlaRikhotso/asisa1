package za.co.oldmutual.asisa.claims;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonDAO;
import za.co.oldmutual.asisa.common.dao.NoteDAO;
import za.co.oldmutual.asisa.common.dao.NotificationDAO;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class NotifiableClaimsDAOTest extends AbstractTest {
	@Mock
	private IPEnquiryDAO insuredPersonEnquiryDAO;

	@Mock
	InsuredPersonDAO insuredPersonDAO;

	@Mock
	UserLoginIdentifier userLoginIdentifier;

	@Mock
	NotificationDAO notificationDAO;

	@Mock
	NoteDAO noteDAO;

	@Mock
	RefDataDAO refDataDAO;

	@Mock
	ReferenceDataCache referenceDataCache;

	@InjectMocks
	private NotifiableClaimsDAO notifiableClaimsDAO;

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	NotifiableClaimBeanRowMapper notifiableClaimBeanRowMapper;

	@Mock
	MailService mailService;

	NotifiableClaimsRequest notifiableClaimsRequest;
	NotifiableClaimsRequest notifiableClaimsRequestForDeathValidation;
	NotifiableClaimsRequest notifiableClaimsRequestForMandatoryValidation;
	NotifiableClaimsRequest notifiableClaimsRequestForAddingScratchpad;
	Map<String, Object> responseObject = new HashMap<>();
	private final ResultSet rs = mock(ResultSet.class);

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();

		InsuredPersonBean insuredPersonBean = CommonTestData.getDummyInsuredPerson();
		List<ClaimNotificationBean> notificationBeanList = new ArrayList<>();
		ClaimNotificationBean notificationBean = CommonTestData.getDummyClaimNotificationBeanRequest();
		notificationBeanList.add(notificationBean);
		notifiableClaimsRequest = new NotifiableClaimsRequest();
		notifiableClaimsRequest.setInsuredPerson(insuredPersonBean);
		notifiableClaimsRequest.setNotifications(notificationBeanList);

		List<ClaimNotificationBean> notificationBeanListNew = new ArrayList<>();
		ClaimNotificationBean notificationBeanNew = CommonTestData
				.getDummyClaimNotificationBeanRequestForDeathValidation();
		notificationBeanListNew.add(notificationBeanNew);
		notifiableClaimsRequestForDeathValidation = new NotifiableClaimsRequest();
		notifiableClaimsRequestForDeathValidation.setInsuredPerson(insuredPersonBean);
		notifiableClaimsRequestForDeathValidation.setNotifications(notificationBeanListNew);

		List<ClaimNotificationBean> listOfNotificationBean = new ArrayList<>();
		ClaimNotificationBean notificationBeann = CommonTestData
				.getDummyClaimNotificationBeanRequestForMandatoryValidation();
		listOfNotificationBean.add(notificationBeann);
		notifiableClaimsRequestForMandatoryValidation = new NotifiableClaimsRequest();
		notifiableClaimsRequestForMandatoryValidation.setInsuredPerson(insuredPersonBean);
		notifiableClaimsRequestForMandatoryValidation.setNotifications(listOfNotificationBean);

		List<ClaimNotificationBean> notificationBeanlist = new ArrayList<>();
		ClaimNotificationBean notificationBeannn = CommonTestData.getDummyClaimNotificationBeanRequestForScratchpad();
		notificationBeanlist.add(notificationBeannn);
		notifiableClaimsRequestForAddingScratchpad = new NotifiableClaimsRequest();
		notifiableClaimsRequestForAddingScratchpad.setInsuredPerson(insuredPersonBean);
		notifiableClaimsRequestForAddingScratchpad.setNotifications(notificationBeanlist);
		responseObject.put("status", HttpStatus.CREATED);
		Map<String, Object> model = new HashMap<>();
		model.put("reason", "change in data");
		Mockito.when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.any(Map.class)))
				.thenReturn(true);
	}

	@Test
	public void addNotifiableClaimsTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableClaimsRequest.getInsuredPerson())).thenReturn("1");
		Mockito.when(insuredPersonEnquiryDAO
				.getInsuredPersonID(insuredPersonDAO.getIPSearchCriteria(notifiableClaimsRequest.getInsuredPerson())))
				.thenReturn("1");
		Mockito.when(notificationDAO.insertClaimNotification(notifiableClaimsRequest.getNotifications().get(0)))
				.thenReturn("1");
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		responseObject = notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequest);
		assertEquals("Claims added successfully", responseObject.get("successMessage").toString());
	}

	@Test
	public void addNotifiableClaimsForDeathValidationTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableClaimsRequest.getInsuredPerson())).thenReturn("1");
		Mockito.when(insuredPersonEnquiryDAO
				.getInsuredPersonID(insuredPersonDAO.getIPSearchCriteria(notifiableClaimsRequest.getInsuredPerson())))
				.thenReturn("1");
		Mockito.when(notificationDAO
				.insertClaimNotification(notifiableClaimsRequestForDeathValidation.getNotifications().get(0)))
				.thenReturn("1");
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		responseObject = notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequestForDeathValidation);
		assertEquals("Validation failed for mandatory fields for RISK/DEATH BENEFIT, FUNERAL POLICY and OTHER ",
				responseObject.get("successMessage").toString());
	}

	@Test
	public void addNotifiableClaimsForMandatoryValidationTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableClaimsRequest.getInsuredPerson())).thenReturn("1");
		Mockito.when(insuredPersonEnquiryDAO
				.getInsuredPersonID(insuredPersonDAO.getIPSearchCriteria(notifiableClaimsRequest.getInsuredPerson())))
				.thenReturn("1");
		Mockito.when(notificationDAO
				.insertClaimNotification(notifiableClaimsRequestForMandatoryValidation.getNotifications().get(0)))
				.thenReturn("1");
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		responseObject = notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequestForMandatoryValidation);
		assertEquals("Validation failed for mandatory fields", responseObject.get("successMessage").toString());
	}

	@Test
	public void addNotifiableClaimsForScratchpadTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableClaimsRequest.getInsuredPerson())).thenReturn("1");
		Mockito.when(insuredPersonEnquiryDAO
				.getInsuredPersonID(insuredPersonDAO.getIPSearchCriteria(notifiableClaimsRequest.getInsuredPerson())))
				.thenReturn("1");
		Mockito.when(notificationDAO
				.insertClaimNotification(notifiableClaimsRequestForAddingScratchpad.getNotifications().get(0)))
				.thenReturn("1");
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		responseObject = notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequestForAddingScratchpad);
		assertEquals("Scratchpad saved successfully", responseObject.get("successMessage").toString());
	}

	@Test
	public void addNotifiableClaimForExistingPersonTest() throws ResourceUpdationFailedException {
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableClaimsRequest.getInsuredPerson())).thenReturn("0");
		Mockito.when(insuredPersonEnquiryDAO
				.getInsuredPersonID(insuredPersonDAO.getIPSearchCriteria(notifiableClaimsRequest.getInsuredPerson())))
				.thenReturn("0");
		Mockito.when(notificationDAO.insertClaimNotification(notifiableClaimsRequest.getNotifications().get(0)))
				.thenReturn("1");
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		responseObject = notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequest);
		assertEquals("Claims added successfully", responseObject.get("successMessage").toString());
	}

	@Test
	public void addNotifiableClaimExceptionTest() {
		NotifiableClaimsRequest notifiableClaimsRequest = null;
		try {
			notifiableClaimsDAO.addNotifiableClaims(notifiableClaimsRequest);
		} catch (ResourceUpdationFailedException e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@Test
	public void updateNotifiableClaim() throws ResourceUpdationFailedException {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		NotifiableClaimBean notifiableClaimBean = CommonTestData.getDummyNotifiableClaimBeanRequest();
		List<NotifiableClaimBean> claimList = new ArrayList<>();
		claimList.add(notifiableClaimBean);
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(NotifiableClaimBeanRowMapper.class))).thenReturn(claimList);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		UpdateNotifiableClaimRequest updateNotifiableClaimRequest = CommonTestData.getDummyUpdateClaimDetailRequest();
		responseObject = notifiableClaimsDAO.updateNotifiableClaim(updateNotifiableClaimRequest);
		assertEquals("Claims updated successfully", responseObject.get("successMessage").toString());
	}

	@Test
	public void updateNotifiableClaimForApprovalRequired() throws ResourceUpdationFailedException {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		NotifiableClaimBean notifiableClaimBean = CommonTestData.getDummyNotifiableClaimBeanRequest();
		List<NotifiableClaimBean> claimList = new ArrayList<>();
		claimList.add(notifiableClaimBean);
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(NotifiableClaimBeanRowMapper.class))).thenReturn(claimList);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		UpdateNotifiableClaimRequest updateNotifiableClaimRequest = CommonTestData.getDummyUpdateClaimDetailRequest();
		responseObject = notifiableClaimsDAO.updateNotifiableClaim(updateNotifiableClaimRequest);
		assertEquals("Claims updated successfully", responseObject.get("successMessage").toString());
	}

	@Test(expected = Exception.class)
	public void updateNotifiableClaimException() throws ResourceUpdationFailedException, Exception {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		NotifiableClaimBean notifiableClaimBean = CommonTestData.getDummyNotifiableClaimBeanRequest();
		List<NotifiableClaimBean> claimList = new ArrayList<>();
		claimList.add(notifiableClaimBean);
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(NotifiableClaimBeanRowMapper.class))).thenThrow(Exception.class);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		UpdateNotifiableClaimRequest updateNotifiableClaimRequest = CommonTestData.getDummyUpdateClaimDetailRequest();
		responseObject = notifiableClaimsDAO.updateNotifiableClaim(updateNotifiableClaimRequest);
	}

	@Test
	public void deleteNotifiableClaim() throws ResourceUpdationFailedException {
		NotifiableClaimBean notifiableClaimBean = CommonTestData.getDummyNotifiableClaimBeanRequest();
		List<NotifiableClaimBean> claimList = new ArrayList<>();
		claimList.add(notifiableClaimBean);
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(NotifiableClaimBeanRowMapper.class))).thenReturn(claimList);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		UpdateNotifiableClaimRequest updateNotifiableClaimRequest = CommonTestData.getDummyUpdateClaimDetailRequest();
		updateNotifiableClaimRequest.setDelete(true);
		responseObject = notifiableClaimsDAO.updateNotifiableClaim(updateNotifiableClaimRequest);
		assertEquals("Claims deleted successfully", responseObject.get("successMessage").toString());
	}

	@Test
	public void isInsuredPersonExistsTest() throws ResourceNotFoundException, ParseException {
		Mockito.when(insuredPersonEnquiryDAO
				.isInsuredPersonExists(insuredPersonDAO.getIPSearchCriteria(Mockito.any(InsuredPersonBean.class))))
				.thenReturn(true);
		InsuredPersonBean insuredPersonBean = new InsuredPersonBean();
		Boolean exist = notifiableClaimsDAO.isInsuredPersonExists(insuredPersonBean);
		assertEquals(true, exist);
	}
}
