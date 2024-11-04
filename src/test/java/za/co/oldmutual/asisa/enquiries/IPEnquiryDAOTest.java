package za.co.oldmutual.asisa.enquiries;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaBean;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAOQueries;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;

public class IPEnquiryDAOTest extends AbstractTest {

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	ReferenceDataCache referenceDataCache;

	@InjectMocks
	IPEnquiryDAO ipEnquiryDao;

	@Mock
	InsuredPersonRowMapper insuredPersonRowMapper;

	private final ResultSet rs = mock(ResultSet.class);

	List<InsuredPersonBean> dummyIPEnquiryResponseforPerfectMatch = CommonTestData
			.getDummyIPEnquiryResponseforPerfectMatch();
	List<InsuredPersonBean> dummyIPEnquiryResponseforIMperfectMatch = CommonTestData
			.getDummyIPEnquiryResponseForImPerfectMatch();
	IPEnquiryCriteriaBean iPEnquiryCriteriaBean = new IPEnquiryCriteriaBean();

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getInsuredPersonsPerfectMatchByIDNumberAndIDType() throws Exception {
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		iPEnquiryCriteriaBean.setIdentityNumber("1234567890123");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("1");
		identityTypeBean.setDescription("SA ID");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(IPEnquiryDAOQueries.FIND_BY_IDTYPE_AND_NUMBER_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(dummyIPEnquiryResponseforPerfectMatch);
		List<InsuredPersonBean> listOFInsuredPerson = (List<InsuredPersonBean>) ipEnquiryDao
				.getInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(dummyIPEnquiryResponseforPerfectMatch, listOFInsuredPerson);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getInsuredPersonsPerfectMatchBySurnameAndDOB() throws Exception {
		Mockito.when(namedParameterJdbcTemplate.query(
				Mockito.eq(IPEnquiryDAOQueries.FIND_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(dummyIPEnquiryResponseforPerfectMatch);
		iPEnquiryCriteriaBean.setIdentityNumber("");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		List<InsuredPersonBean> listOFInsuredPerson = (List<InsuredPersonBean>) ipEnquiryDao
				.getInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(dummyIPEnquiryResponseforPerfectMatch, listOFInsuredPerson);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getInsuredPersonsImPerfectMatchByIDNumberAndIDType() throws Exception {
		Mockito.when(namedParameterJdbcTemplate.query(
				Mockito.eq(IPEnquiryDAOQueries.FIND_IMPERFECT_MATCH_BY_IDTYPE_AND_NUMBER_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(dummyIPEnquiryResponseforIMperfectMatch);
		iPEnquiryCriteriaBean.setIdentityNumber("1234567890");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("1");
		identityTypeBean.setDescription("SA ID");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setSurname("");
		iPEnquiryCriteriaBean.setDateOfBirth("");
		List<InsuredPersonBean> listOFInsuredPerson = (List<InsuredPersonBean>) ipEnquiryDao
				.getInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(dummyIPEnquiryResponseforIMperfectMatch, listOFInsuredPerson);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getInsuredPersonsImPerfectMatchByBySurnameAndDOB() throws Exception {
		Mockito.when(namedParameterJdbcTemplate.query(
				Mockito.eq(IPEnquiryDAOQueries.FIND_IMPERFECT_MATCH_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(dummyIPEnquiryResponseforIMperfectMatch);
		iPEnquiryCriteriaBean.setIdentityNumber("");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setSurname("Kum");
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		List<InsuredPersonBean> listOFInsuredPerson = (List<InsuredPersonBean>) ipEnquiryDao
				.getInsuredPersons(iPEnquiryCriteriaBean);
		assertEquals(dummyIPEnquiryResponseforIMperfectMatch, listOFInsuredPerson);
	}

	@Test
	public void checkByIdTypeAndIdNumber() throws Exception {
		boolean result = true;
		iPEnquiryCriteriaBean.setDateOfBirth("");
		iPEnquiryCriteriaBean.setSurname("");
		iPEnquiryCriteriaBean.setIdentityNumber("1234567890123");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("1");
		identityTypeBean.setDescription("SA ID");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(IPEnquiryDAOQueries.IS_EXISTS_BY_IDTYPE_AND_NUMBER_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.eq(Integer.class))).thenReturn(1);
		boolean value = ipEnquiryDao.isInsuredPersonExists(iPEnquiryCriteriaBean);
		assertEquals(result, value);
	}

	@Test
	public void checkBySurnameAndDateOfBirth() throws Exception {
		boolean result = true;
		iPEnquiryCriteriaBean.setIdentityNumber("");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009");
		iPEnquiryCriteriaBean.setSurname("Kumar");

		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(IPEnquiryDAOQueries.IS_EXISTS_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.eq(Integer.class))).thenReturn(1);
		boolean value = ipEnquiryDao.isInsuredPersonExists(iPEnquiryCriteriaBean);
		assertEquals(result, value);
	}

	@Test
	public void isInsuredPersonExistsFailedException() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009123");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		try {
			ipEnquiryDao.isInsuredPersonExists(iPEnquiryCriteriaBean);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}

	@Test
	public void getInsuredPersonsFailedException() throws Exception {
		iPEnquiryCriteriaBean.setIdentityNumber("");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		try {
			ipEnquiryDao.getInsuredPersons(iPEnquiryCriteriaBean);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}

	@Test
	public void getInsuredPersonIDByIdNumberAndType() {
		String personId = "456678";
		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(IPEnquiryDAOQueries.FIND_PERSON_BY_IDTYPE_AND_NUMBER_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.eq(String.class))).thenReturn(personId);
		iPEnquiryCriteriaBean.setIdentityNumber("1234569876007");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("1");
		identityTypeBean.setDescription("Identity number");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("");
		iPEnquiryCriteriaBean.setSurname("");
		String result = ipEnquiryDao.getInsuredPersonID(iPEnquiryCriteriaBean);
		assertEquals(personId, result);
	}

	@Test
	public void getInsuredPersonIDBySurnameAndDateofbirth() {
		String personId = "986678";
		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(IPEnquiryDAOQueries.FIND_PERSON_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class), Mockito.eq(String.class))).thenReturn(personId);
		iPEnquiryCriteriaBean.setIdentityNumber("");
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("12/12/2009123");
		iPEnquiryCriteriaBean.setSurname("Kumar");
		String result = ipEnquiryDao.getInsuredPersonID(iPEnquiryCriteriaBean);
		assertEquals(personId, result);
	}

	@Test
	public void getInsuredPersonIDExceptionTest() {
		IdentityTypeBean identityTypeBean = new IdentityTypeBean();
		identityTypeBean.setCode("");
		identityTypeBean.setDescription("");
		iPEnquiryCriteriaBean.setIdentityType(identityTypeBean);
		iPEnquiryCriteriaBean.setDateOfBirth("");
		iPEnquiryCriteriaBean.setSurname("");
		try {
			ipEnquiryDao.getInsuredPersonID(iPEnquiryCriteriaBean);
		} catch (Exception e) {
			assertThatExceptionOfType(EmptyResultDataAccessException.class);
		}
	}

	@Test
	public void getInsuredPersonByNotificationID() {
		String personId = "789767";
		String notificationID = "56879809098987";
		Mockito.when(
				namedParameterJdbcTemplate.queryForObject(Mockito.eq(IPEnquiryDAOQueries.FETCH_INSURED_PERSONID_QUERY),
						Mockito.any(MapSqlParameterSource.class), Mockito.eq(String.class)))
				.thenReturn(personId);
		String result = ipEnquiryDao.getInsuredPersonByNotificationID(notificationID);
		assertEquals(personId, result);
	}
}
