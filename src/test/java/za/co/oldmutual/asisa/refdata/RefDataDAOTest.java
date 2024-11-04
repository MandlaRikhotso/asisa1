package za.co.oldmutual.asisa.refdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimTypeBean;
import za.co.oldmutual.asisa.refdata.bean.GenderBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.NotificationStatusBean;
import za.co.oldmutual.asisa.refdata.bean.NotificationTypeBean;
import za.co.oldmutual.asisa.refdata.bean.OfficeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ReadingsCriteriaBean;
import za.co.oldmutual.asisa.refdata.bean.TitleBean;

public class RefDataDAOTest extends AbstractTest {

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@InjectMocks
	RefDataDAO refDataDAO;

	List<String> responseObject;
	List<ClaimCategoryBean> dummyClaimCategories = new ArrayList();

	public void setup() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getClaimTypes() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_CLAIM_TYPES_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimTypes());
		List<ClaimTypeBean> claimTypes = refDataDAO.getClaimTypes();
		assertTrue(claimTypes != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getClaimCategories() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_CLAIM_CATEGORY_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<ClaimCategoryBean> claimCategories = refDataDAO.getClaimCategories();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyGenders() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_GENDERS_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyGenders());
		List<GenderBean> claimCategories = refDataDAO.getGenders();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyTitles() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_TITLES_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyTitles());
		List<TitleBean> claimCategories = refDataDAO.getTitles();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyIdentityTypes() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_IDENTITY_TYPES_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<IdentityTypeBean> claimCategories = refDataDAO.getIdentityTypes();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummySymbols() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_LIFE_SYMBOL_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<LifeSymbolBean> claimCategories = refDataDAO.getLifeSymbols();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummySpecialInvestigations() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_LIFE_SPEC_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<LifeSpecBean> claimCategories = refDataDAO.getLifeSpecs();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyPolicyBenefits() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_POLICY_TYPES_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<PolicyTypeBean> claimCategories = refDataDAO.getPolicyTypes();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyPaymentMethods() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_PAYMENT_METHOD_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<PaymentMethodBean> claimCategories = refDataDAO.getPaymentMethods();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyImpairmentCodes() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_IMPAIRMENT_CODES_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<ImpairmentCodeBean> claimCategories = refDataDAO.getImpairmentCodes();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyClaimCauses() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_CLAIM_CAUSE_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<ClaimCauseBean> claimCategories = refDataDAO.getClaimCauses();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyClaimStatuses() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_CLAIM_STATUS_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<ClaimStatusBean> claimCategories = refDataDAO.getClaimStatuses();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getDummyClaimCategories() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_CLAIM_CATEGORY_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyClaimCategories());
		List<ClaimCategoryBean> claimCategories = refDataDAO.getClaimCategories();
		assertTrue(claimCategories != null);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getNotificationTypes() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_NOTIFICATION_TYPES_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyNotificationTypes());
		List<NotificationTypeBean> notificationTypes = refDataDAO.getNotificationTypes();
		assertNotNull(notificationTypes);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getNotificationStatuses() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_NOTIFICATION_STATUS_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyNotificationStatus());
		List<NotificationStatusBean> notificationStatus = refDataDAO.getNotificationStatuses();
		assertNotNull(notificationStatus);
	}

	@Test
	public void getOfficeTest() {
		String source = "1:2";
		Mockito.when(namedParameterJdbcTemplate.queryForObject(Mockito.anyString(),
				Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class))).thenReturn("123");
		String result = refDataDAO.getOffice(source);
		assertEquals("123", result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getOfficesTest() {
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_OFFICE_QUERY_BY_ID),
				(Mockito.any(ResultSetExtractor.class)))).thenReturn(CommonTestData.getDummyOffice());
		List<OfficeBean> result = refDataDAO.getOffices();
		assertNotNull(result);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getReadings() {
		
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(RefDataDAOQueries.FETCH_READINGS_QUERY),
				Mockito.any(ResultSetExtractor.class))).thenReturn(CommonTestData.getDummyReadings());
		List<ReadingsCriteriaBean> readings = refDataDAO.getReadingsCriteria();
		assertNotNull(readings);
	}
	
}
