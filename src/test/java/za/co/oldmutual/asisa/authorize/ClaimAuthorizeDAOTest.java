package za.co.oldmutual.asisa.authorize;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.authorize.mapper.ClaimAuthorizeResponseRowMapper;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class ClaimAuthorizeDAOTest extends AbstractTest {

	@InjectMocks
	private ClaimAuthorizeDAO claimAuthorizeDAO;

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	IPEnquiryDAO insuredPersonEnquiryDAO;

	@Mock
	ClaimAuthorizeResponseRowMapper claimAuthorizeResponseRowMapper;

	@Mock
	ReferenceDataCache referenceDataCache;

	@Mock
	InsuredPersonRowMapper insuredPersonRowMapper;

	@Mock
	UserLoginIdentifier userLoginIdentifier;

	private final ResultSet rs = mock(ResultSet.class);

	@Mock
	MailService mailService;

	Map<String, Object> responseObject = new HashMap<>();
	List<ClaimAuthorizeResponse> dummyClaimsForAuthorization;
	ClaimAuthorizeResponse dummyExistingClaimAuthorizeResponseDetail;
	ClaimAuthorizeResponse dummyUpdatedClaimAuthorizeResponseDetail;
	ClaimAuthorizeResponse dummyNotifiableClaimAuditBean;
	ClaimAuthorizeResponse claimAuthorizeResponse;
	InsuredPersonBean insuredPersonBean;
	String notificationID;

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();

		notificationID = "3c65e70b-bba3-4f2a-9a41-2fc13edd05af";
		dummyClaimsForAuthorization = new ArrayList<>();
		claimAuthorizeResponse = CommonTestData.getDummyClaimAuthorizationResponse();
		dummyClaimsForAuthorization.add(claimAuthorizeResponse);
		insuredPersonBean = CommonTestData.getDummyInsuredPersonBeanResponse();
		dummyExistingClaimAuthorizeResponseDetail = CommonTestData.getDummyClaimAuthorizationResponseDetail();
		dummyUpdatedClaimAuthorizeResponseDetail = CommonTestData.getDummyClaimAuthorizationResponseDetail();
		dummyUpdatedClaimAuthorizeResponseDetail.setNotificationID("3c65e70b-bba3-4f2a-9a41-2fc13edd05af");
		dummyNotifiableClaimAuditBean = CommonTestData.getDummyClaimAuthorizationResponseDetail();
		Map<String, Object> model = new HashMap<>();
		model.put("reason", "change in data");
		Mockito.when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.any(Map.class)))
				.thenReturn(true);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void displayClaimTransaction() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(ClaimAuthorizeResponseRowMapper.class))).thenReturn(dummyClaimsForAuthorization);
		List<ClaimAuthorizeResponse> claimData = null;
		try {
			claimData = (List<ClaimAuthorizeResponse>) claimAuthorizeDAO.displayClaimTransaction();
		} catch (ResourceNotFoundException e) {
			fail("ResourceNotFoundException occured");
		}
		assertEquals(1, claimData.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void displayTransactionCountTest() throws ResourceNotFoundException {
		Mockito.when(namedParameterJdbcTemplate.queryForObject(Mockito.anyString(),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(Integer.class))).thenReturn(1);
		Object count = claimAuthorizeDAO.displayTransactionCount();
		assertNotNull(count);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findClaimApprovalById() throws ParseException, ResourceUpdationFailedException {

		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(ClaimAuthorizeDAOQueries.FETCH_TRANSCATION_TYPE_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class))).thenReturn("UPDATE");

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(ClaimAuthorizeDAOQueries.FIND_INSURED_PERSON),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(insuredPersonBean));

		Mockito.when(
				namedParameterJdbcTemplate.query(Mockito.eq(ClaimAuthorizeDAOQueries.FIND_EXISTING_CLAIM_BY_ID_QUERY),
						Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(dummyExistingClaimAuthorizeResponseDetail));

		Mockito.when(
				namedParameterJdbcTemplate.query(Mockito.eq(ClaimAuthorizeDAOQueries.FIND_UPDATED_CLAIM_BY_ID_QUERY),
						Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(dummyUpdatedClaimAuthorizeResponseDetail));

		
		Map<String, Object> claimDetails = new HashMap<>();
		try {
			claimDetails = (Map<String, Object>) claimAuthorizeDAO.findClaimApprovalById(notificationID);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		assertEquals("UPDATE", claimDetails.get("transType"));
		assertEquals(insuredPersonBean, claimDetails.get("insuredPerson"));
		assertEquals(dummyExistingClaimAuthorizeResponseDetail, claimDetails.get("old"));
		assertEquals(dummyUpdatedClaimAuthorizeResponseDetail, claimDetails.get("new"));
	}

	@Test
	public void findClaimApprovalByIdException() {
		String notificationId = null;
		try {
			claimAuthorizeDAO.findClaimApprovalById(notificationId);
		} catch (ResourceNotFoundException e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeUpdateApproval() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = new AuthorizeRequest();
		approvedImpairmentRequest.setAction("APPROVE");
		approvedImpairmentRequest.setActionRemark("Approving");
		approvedImpairmentRequest.setNotificationID(notificationID);
		approvedImpairmentRequest.setTransType("UPDATE");

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(Map.class),
				Mockito.any(RowMapper.class))).thenReturn(Arrays.asList(claimAuthorizeResponse));

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		Map<String, Object> result = (Map<String, Object>) claimAuthorizeDAO
				.updateClaimApproved(approvedImpairmentRequest);
		assertEquals("Authorization for Claim Update successfull", result.get("successMessage").toString());
	}

	@Test
	public void authorizeUpdateApprovalException() {
		AuthorizeRequest claimAuthorizeRequest = null;
		try {
			claimAuthorizeDAO.updateClaimApproved(claimAuthorizeRequest);
		} catch (ResourceUpdationFailedException e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeUpdateReject() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = new AuthorizeRequest();
		approvedImpairmentRequest.setAction("REJECT");
		approvedImpairmentRequest.setActionRemark("Rejecting");
		approvedImpairmentRequest.setNotificationID(notificationID.toUpperCase());
		approvedImpairmentRequest.setTransType("UPDATE");

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(RowMapper.class))).thenReturn(Arrays.asList(dummyNotifiableClaimAuditBean));

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(Map.class),
				Mockito.any(RowMapper.class))).thenReturn(Arrays.asList(claimAuthorizeResponse));

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(Map.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");

		Map<String, Object> success = (Map<String, Object>) claimAuthorizeDAO
				.updateClaimReject(approvedImpairmentRequest);
		assertEquals("Authorization for Claim Reject successful", success.get("successMessage").toString());
	}

	@Test
	public void authorizeUpdateRejectException() {
		AuthorizeRequest claimAuthorizeRequest = null;
		try {
			claimAuthorizeDAO.updateClaimReject(claimAuthorizeRequest);
		} catch (ResourceUpdationFailedException e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeDeleteApproval() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = new AuthorizeRequest();
		approvedImpairmentRequest.setAction("APPROVE");
		approvedImpairmentRequest.setActionRemark("Approving");
		approvedImpairmentRequest.setNotificationID(notificationID);
		approvedImpairmentRequest.setTransType("DELETE");

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(Map.class),
				Mockito.any(RowMapper.class))).thenReturn(Arrays.asList(claimAuthorizeResponse));

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		Map<String, Object> success = (Map<String, Object>) claimAuthorizeDAO
				.deleteClaimApproved(approvedImpairmentRequest);
		assertEquals("Authorization for Claim Delete successfull", success.get("successMessage").toString());
	}

	@Test
	public void authorizeDeleteApprovalException() {
		AuthorizeRequest claimAuthorizeRequest = null;
		try {
			claimAuthorizeDAO.deleteClaimApproved(claimAuthorizeRequest);
		} catch (ResourceUpdationFailedException e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeDeleteReject() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = new AuthorizeRequest();
		approvedImpairmentRequest.setAction("REJECT");
		approvedImpairmentRequest.setActionRemark("Approving");
		approvedImpairmentRequest.setNotificationID(notificationID);
		approvedImpairmentRequest.setTransType("DELETE");

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(Map.class),
				Mockito.any(RowMapper.class))).thenReturn(Arrays.asList(claimAuthorizeResponse));

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		Map<String, Object> success = (Map<String, Object>) claimAuthorizeDAO
				.deleteClaimReject(approvedImpairmentRequest);
		assertEquals("Authorization for Claim delete Rejected successful", success.get("successMessage").toString());
	}

	@Test
	public void authorizeDeleteRejectException() {
		AuthorizeRequest claimAuthorizeRequest = null;
		try {
			claimAuthorizeDAO.deleteClaimReject(claimAuthorizeRequest);
		} catch (ResourceUpdationFailedException e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}
}
