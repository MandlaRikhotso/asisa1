package za.co.oldmutual.asisa.history;

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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.iphistory.IPClaimHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPClaimHistoryBeanRowMapper;
import za.co.oldmutual.asisa.iphistory.IPHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPHistoryDAO;
import za.co.oldmutual.asisa.iphistory.IPHistoryDAOQueries;
import za.co.oldmutual.asisa.iphistory.IPImpairmentHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPImpairmentHistoryBeanRowMapper;
import za.co.oldmutual.asisa.iphistory.IPNoteHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPNoteHistoryBeanRowMapper;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class IPHistoryDAOTest extends AbstractTest {
	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	ReferenceDataCache referenceDataCache;

	@InjectMocks
	IPHistoryDAO ipHistoryDAO;

	@Mock
	IPImpairmentHistoryBeanRowMapper iPImpairmentHistoryBeanRowMapper;

	@Mock
	IPClaimHistoryBeanRowMapper iPClaimHistoryBeanRowMapper;

	@Mock
	InsuredPersonRowMapper insuredPersonRowMapper;
	
	@Mock
	IPNoteHistoryBeanRowMapper iPNoteHistoryBeanRowMapper;

	private final ResultSet rs = mock(ResultSet.class);
	IPHistoryBean iPHistoryBean = new IPHistoryBean();
	InsuredPersonBean insuredPersonHistoryResponse = CommonTestData.getDummyInsuredPersonBeanResponse();
	List<IPNoteHistoryBean> noteHistory = CommonTestData.getDummyNoteHistoryResponse();
	List<IPImpairmentHistoryBean> impairmentHistory = CommonTestData.getDummyImpairmentHistoryResponse();
	List<IPClaimHistoryBean> claimHistory = CommonTestData.getDummyClaimHistoryResponse();
	String identityTypeCode;
	String identityNumber;
	String personId;

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
		iPHistoryBean.setInsuredPerson(insuredPersonHistoryResponse);
		iPHistoryBean.setClaimHistory(claimHistory);
		iPHistoryBean.setImpairmentHistory(impairmentHistory);
		iPHistoryBean.setNoteHistory(noteHistory);
		iPHistoryBean.setScratchpadHistory(noteHistory);
	}

	@SuppressWarnings("unchecked")

	@Test
	public void getHistory() throws Exception {
		identityTypeCode = "4";
		identityNumber = "123450000";
		personId = "123";

		Mockito.when(namedParameterJdbcTemplate.queryForObject(
				Mockito.eq(IPHistoryDAOQueries.FETCH_INSURED_PERSON_DETAILS_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(insuredPersonHistoryResponse);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(IPHistoryDAOQueries.FETCH_IMPAIRMENT_DETAILS_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))).thenReturn(impairmentHistory);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(IPHistoryDAOQueries.FETCH_CLAIM_DETAILS_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))).thenReturn(claimHistory);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(IPHistoryDAOQueries.FETCH_NOTE_DETAILS_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))).thenReturn(noteHistory);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(IPHistoryDAOQueries.FETCH_SCRATCHPAD_DETAILS_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class))).thenReturn(noteHistory);
		IPHistoryBean responseObject = ipHistoryDAO.getInsuredPersonsHistory(identityTypeCode, identityNumber,
				personId);
		assertEquals(iPHistoryBean.getInsuredPerson(), responseObject.getInsuredPerson());
		assertEquals(iPHistoryBean.getImpairmentHistory(), responseObject.getImpairmentHistory());
		assertEquals(iPHistoryBean.getClaimHistory(), responseObject.getClaimHistory());
		assertEquals(iPHistoryBean.getNoteHistory(), responseObject.getNoteHistory());
		assertEquals(iPHistoryBean.getScratchpadHistory(), responseObject.getScratchpadHistory());
	}

	@Test
	public void getInsuredPersonsFailedException() throws Exception {
		String identityTypeCode = "";
		String identityNumber = "123450000";
		String personId = "123";
		try {
			ipHistoryDAO.getInsuredPersonsHistory(identityTypeCode, identityNumber, personId);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceNotFoundException.class);
		}
	}
}
