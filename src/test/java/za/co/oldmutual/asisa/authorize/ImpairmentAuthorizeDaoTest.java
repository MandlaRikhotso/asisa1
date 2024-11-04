package za.co.oldmutual.asisa.authorize;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
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
import za.co.oldmutual.asisa.authorize.mapper.ImpairmentAuthorizeResponseRowMapper;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class ImpairmentAuthorizeDaoTest extends AbstractTest {

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;
	@Mock
	ReferenceDataCache referenceDataCache;
	@InjectMocks
	ImpairmentAuthorizeDao impairmentAuthorizeDao;
	@Mock
	ImpairmentAuthorizeResponseRowMapper impairmentAuthorizeResponseRowMapper;
	@Mock
	InsuredPersonRowMapper insuredPersonRowMapper;
	@Mock
	UserLoginIdentifier userLoginIdentifier;
	@Mock
	MailService mailService;

	List<ImpairmentAuthorizeResponse> dummyImpairmentsForAuthorization;
	ImpairmentAuthorizeResponse impairmentAuthorizeDetailResponseOld;
	ImpairmentAuthorizeResponse impairmentAuthorizeDetailResponseNew;
	InsuredPersonBean insuredPersonBean;
	String notificationID;
	ImpairmentAuthorizeResponse notifiableImpairmentAuditBean;
	ImpairmentAuthorizeResponse impairmentAuthorizeDetailReq;
	private final ResultSet rs = mock(ResultSet.class);

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
		super.setUp();
		dummyImpairmentsForAuthorization = new ArrayList<>();
		ImpairmentAuthorizeResponse impairmentAuthorizeResponse = CommonTestData.getDummyAuthorizationResponse();
		dummyImpairmentsForAuthorization.add(impairmentAuthorizeResponse);
		impairmentAuthorizeDetailResponseOld = CommonTestData.getDummyImpairmentAuthorizeDetailResponse();
		impairmentAuthorizeDetailResponseOld.setReadings("12-999");
		insuredPersonBean = CommonTestData.getDummyInsuredPersonBeanResponse();
		notifiableImpairmentAuditBean = CommonTestData.getDummyNotifiableImpairmentForAuditBean();
		impairmentAuthorizeDetailReq = CommonTestData.getDummyImpairmentAuthorizeDetailRequest();
		Map<String, Object> model = new HashMap<>();
		model.put("reason", "change in data");
		Mockito.when(mailService.sendEmail(Mockito.anyString(),Mockito.anyString(),  Mockito.any(Map.class))).thenReturn(true);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAllPendingImpairments() throws ResourceNotFoundException {

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class)))
				.thenReturn(dummyImpairmentsForAuthorization);
		List<ImpairmentAuthorizeResponse> impairmentsForAuthorization = (List<ImpairmentAuthorizeResponse>) impairmentAuthorizeDao
				.getAllPendingImpairments();
		assertEquals(dummyImpairmentsForAuthorization, impairmentsForAuthorization);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getImpairmentsForNotifiableImpairmentId() throws ResourceNotFoundException {

		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_NOTIFICATION_TRANSACTION_TYPE_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class))).thenReturn("UPDATE");

		Mockito.when(
				(namedParameterJdbcTemplate.query(Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_INSURED_PERSON_QUERY),
						Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))))
				.thenReturn(Arrays.asList(insuredPersonBean));

		Mockito.when((namedParameterJdbcTemplate.query(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_OLD_NOTIFIABLE_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))))
				.thenReturn(Arrays.asList(impairmentAuthorizeDetailResponseOld));

		Mockito.when((namedParameterJdbcTemplate.query(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_NEW_NOTIFIABLE_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))))
				.thenReturn(Arrays.asList(impairmentAuthorizeDetailResponseNew));
		Map<String, Object> impairmentDetails = (Map<String, Object>) impairmentAuthorizeDao
				.getImpairmentsForNotifiableImpairmentId(notificationID);

		assertEquals("UPDATE", impairmentDetails.get("transType"));
		assertEquals(insuredPersonBean, impairmentDetails.get("insuredPerson"));
		assertEquals(impairmentAuthorizeDetailResponseOld, impairmentDetails.get("old"));
		assertEquals(impairmentAuthorizeDetailResponseNew, impairmentDetails.get("new"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeUpdateApproval() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
		approvedImpairmentRequest.setAction("APPROVE");
		approvedImpairmentRequest.setActionRemark("Approving");

		Mockito.when(
				namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_NOTIFICATION_QUERY),
						Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_REMARK_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(notifiableImpairmentAuditBean));

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		Map<String, Object> success = (Map<String, Object>) impairmentAuthorizeDao
				.authorizeUpdate(approvedImpairmentRequest);
		assertEquals("Impairment updation approved successfully ", success.get("successMessage").toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeUpdateReject() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
		approvedImpairmentRequest.setAction("REJECT");
		approvedImpairmentRequest.setActionRemark("Rejecting");

		Mockito.when(
				namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_NOTIFICATION_QUERY),
						Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_REMARK_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(ImpairmentAuthorizeDaoQueries.IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(impairmentAuthorizeDetailReq));

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.REVERT_OLD_NOTIFIABLE_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(notifiableImpairmentAuditBean));

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		Map<String, Object> success = (Map<String, Object>) impairmentAuthorizeDao
				.authorizeUpdate(approvedImpairmentRequest);
		assertEquals("Impairment updation rejected successfully ", success.get("successMessage").toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeDeleteApproval() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
		approvedImpairmentRequest.setAction("APPROVE");
		approvedImpairmentRequest.setActionRemark("Approving");

		Mockito.when(
				namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_NOTIFICATION_QUERY),
						Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_REMARK_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(notifiableImpairmentAuditBean));

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		
		Map<String, Object> success = (Map<String, Object>) impairmentAuthorizeDao
				.authorizeDelete(approvedImpairmentRequest);
		assertEquals("Impairment deletion approved successfully ", success.get("successMessage").toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void authorizeDeleteReject() throws ResourceUpdationFailedException {
		AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
		approvedImpairmentRequest.setAction("REJECT");
		approvedImpairmentRequest.setActionRemark("Rejecting");

		Mockito.when(
				namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_NOTIFICATION_QUERY),
						Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(ImpairmentAuthorizeDaoQueries.UPDATE_REMARK_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(ImpairmentAuthorizeDaoQueries.FETCH_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(Arrays.asList(notifiableImpairmentAuditBean));

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(ImpairmentAuthorizeDaoQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");

		Map<String, Object> success = (Map<String, Object>) impairmentAuthorizeDao
				.authorizeDelete(approvedImpairmentRequest);
		assertEquals("Impairment deletion rejected successfully ", success.get("successMessage").toString());
	}

	@Test
	public void authorizeUpdateHandleResourceUpdationFailedException() throws Exception {
		AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForUpdate();
		approvedImpairmentRequest.setAction("APPROVEd12");
		approvedImpairmentRequest.setActionRemark("Approving");
		try {
			impairmentAuthorizeDao.authorizeUpdate(approvedImpairmentRequest);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@Test
	public void authorizeDeleteHandleResourceUpdationFailedException() throws Exception {
		AuthorizeRequest approvedImpairmentRequest = CommonTestData.getDummyAuthorizeRequestForDelete();
		approvedImpairmentRequest.setAction("APPROVEd12");
		approvedImpairmentRequest.setActionRemark("Approving");
		try {
			impairmentAuthorizeDao.authorizeDelete(approvedImpairmentRequest);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAllPendingImpairmentsException() throws ResourceNotFoundException {
		String authorizeRequestQuery = "";
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(authorizeRequestQuery), Mockito.any(RowMapper.class)))
				.thenReturn(dummyImpairmentsForAuthorization);
		try {
			impairmentAuthorizeDao.getAllPendingImpairments();
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getImpairmentsForNotifiableImpairmentIdException() throws ResourceNotFoundException {
		final String txnQuery = "";
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(txnQuery), Mockito.any(MapSqlParameterSource.class),
				Mockito.any(RowMapper.class))).thenReturn(Arrays.asList("UPDATE"));
		final String insuredPersonQuery = "";
		Mockito.when((namedParameterJdbcTemplate.query(Mockito.eq(insuredPersonQuery),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))))
				.thenReturn(Arrays.asList(insuredPersonBean));
		final String oldNotifiableImpairmentQuery = "";
		Mockito.when((namedParameterJdbcTemplate.query(Mockito.eq(oldNotifiableImpairmentQuery),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))))
				.thenReturn(Arrays.asList(impairmentAuthorizeDetailResponseOld));
		final String newNotifiableImpairmentQuery = "";
		Mockito.when((namedParameterJdbcTemplate.query(Mockito.eq(newNotifiableImpairmentQuery),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))))
				.thenReturn(Arrays.asList(impairmentAuthorizeDetailResponseNew));
		try {
			impairmentAuthorizeDao.getImpairmentsForNotifiableImpairmentId(notificationID);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}
}
