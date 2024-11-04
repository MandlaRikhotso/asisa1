package za.co.oldmutual.asisa.refdata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import za.co.oldmutual.asisa.common.validation.CustomBeanPropertyRowMapper;
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

@Component
public class RefDataDAO {

	@Autowired
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Cacheable("claimTypeBean")
	public List<ClaimTypeBean> getClaimTypes() {
		List<ClaimTypeBean> claimTypes = null;
		claimTypes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_CLAIM_TYPES_QUERY,
				new CustomBeanPropertyRowMapper<ClaimTypeBean>(ClaimTypeBean.class));
		return claimTypes;
	}

	@Cacheable("claimCategoryBean")
	public List<ClaimCategoryBean> getClaimCategories() {
		List<ClaimCategoryBean> claimCategories = null;
		claimCategories = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_CLAIM_CATEGORY_QUERY,
				new CustomBeanPropertyRowMapper<ClaimCategoryBean>(ClaimCategoryBean.class));
		return claimCategories;
	}

	@Cacheable("claimStatusBean")
	public List<ClaimStatusBean> getClaimStatuses() {
		List<ClaimStatusBean> claimStatuses = null;
		claimStatuses = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_CLAIM_STATUS_QUERY,
				new CustomBeanPropertyRowMapper<ClaimStatusBean>(ClaimStatusBean.class));
		return claimStatuses;
	}

	@Cacheable("claimCauseBean")
	public List<ClaimCauseBean> getClaimCauses() {
		List<ClaimCauseBean> claimCauses = null;
		claimCauses = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_CLAIM_CAUSE_QUERY,
				new CustomBeanPropertyRowMapper<ClaimCauseBean>(ClaimCauseBean.class));
		return claimCauses;
	}

	@Cacheable("impairmentCodeBean")
	public List<ImpairmentCodeBean> getImpairmentCodes() {
		List<ImpairmentCodeBean> impairmentCodes = null;
		impairmentCodes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_IMPAIRMENT_CODES_QUERY,
				new CustomBeanPropertyRowMapper<ImpairmentCodeBean>(ImpairmentCodeBean.class));
		return impairmentCodes;
	}

	@Cacheable("paymentMethodBean")
	public List<PaymentMethodBean> getPaymentMethods() {
		List<PaymentMethodBean> paymentMethods = null;
		paymentMethods = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_PAYMENT_METHOD_QUERY,
				new CustomBeanPropertyRowMapper<PaymentMethodBean>(PaymentMethodBean.class));
		return paymentMethods;
	}

	@Cacheable("policyTypeBean")
	public List<PolicyTypeBean> getPolicyTypes() {
		List<PolicyTypeBean> policyTypes = null;
		policyTypes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_POLICY_TYPES_QUERY,
				new CustomBeanPropertyRowMapper<PolicyTypeBean>(PolicyTypeBean.class));
		return policyTypes;
	}

	@Cacheable("lifeSpecBean")
	public List<LifeSpecBean> getLifeSpecs() {
		List<LifeSpecBean> specialInvestigations = null;
		specialInvestigations = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_LIFE_SPEC_QUERY,
				new CustomBeanPropertyRowMapper<LifeSpecBean>(LifeSpecBean.class));
		return specialInvestigations;
	}

	@Cacheable("lifeSymbolBean")
	public List<LifeSymbolBean> getLifeSymbols() {
		List<LifeSymbolBean> symbols = null;
		symbols = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_LIFE_SYMBOL_QUERY,
				new CustomBeanPropertyRowMapper<LifeSymbolBean>(LifeSymbolBean.class));
		return symbols;
	}

	@Cacheable("identityTypeBean")
	public List<IdentityTypeBean> getIdentityTypes() {
		List<IdentityTypeBean> identityTypes = null;
		identityTypes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_IDENTITY_TYPES_QUERY,
				new CustomBeanPropertyRowMapper<IdentityTypeBean>(IdentityTypeBean.class));
		return identityTypes;
	}

	@Cacheable("titleBean")
	public List<TitleBean> getTitles() {
		List<TitleBean> titleTypes = null;
		titleTypes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_TITLES_QUERY,
				new CustomBeanPropertyRowMapper<TitleBean>(TitleBean.class));
		return titleTypes;
	}

	@Cacheable("genderBean")
	public List<GenderBean> getGenders() {
		List<GenderBean> genderTypes = null;
		genderTypes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_GENDERS_QUERY,
				new CustomBeanPropertyRowMapper<GenderBean>(GenderBean.class));
		return genderTypes;
	}

	@Cacheable("notificationTypeBean")
	public List<NotificationTypeBean> getNotificationTypes() {
		List<NotificationTypeBean> notificationTypes = null;
		notificationTypes = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_NOTIFICATION_TYPES_QUERY,
				new CustomBeanPropertyRowMapper<NotificationTypeBean>(NotificationTypeBean.class));
		return notificationTypes;
	}

	@Cacheable("notificationStatusBean")
	public List<NotificationStatusBean> getNotificationStatuses() {
		List<NotificationStatusBean> notificationStatuses = null;
		notificationStatuses = namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_NOTIFICATION_STATUS_QUERY,
				new CustomBeanPropertyRowMapper<NotificationStatusBean>(NotificationStatusBean.class));
		return notificationStatuses;
	}

	public String getOffice(String source) {
		String[] arr = source.split(":");
		return namedParameterJdbcTemplate.queryForObject(
				RefDataDAOQueries.FETCH_OFFICE_QUERY_BY_LOCATION_ID_AND_OFFICE_ID,
				new MapSqlParameterSource("locationID", arr[0]).addValue("officeID", arr[1]), String.class);
	}

	@Cacheable("office")
	public List<OfficeBean> getOffices() {

		return namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_OFFICE_QUERY_BY_ID,
				new CustomBeanPropertyRowMapper<OfficeBean>(OfficeBean.class));
	}
	
	@Cacheable("readings")
	public List<ReadingsCriteriaBean> getReadingsCriteria() {
		return namedParameterJdbcTemplate.query(RefDataDAOQueries.FETCH_READINGS_QUERY,
				new CustomBeanPropertyRowMapper<ReadingsCriteriaBean>(ReadingsCriteriaBean.class));
	}
	

}
