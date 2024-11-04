package za.co.oldmutual.asisa.history;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.iphistory.IPNoteHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPNoteHistoryBeanRowMapper;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class IPNoteHistoryBeanRowMapperTest extends AbstractTest{

	@Mock
	ReferenceDataCache referenceDataCache;
	
	@InjectMocks
	IPNoteHistoryBeanRowMapper iPNoteHistoryBeanRowMapper;
	
	private final ResultSet rs = mock(ResultSet.class);
	
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
		  try {
			  Mockito.when(rs.getString("BUSINESS_UNIT")).thenReturn("FINANCE");
			  Mockito.when(rs.getString("NOTIFICATION_SOURCE")).thenReturn("OM");			  
			  Mockito.when(rs.getString("OM_USER_ID")).thenReturn("XY452762");
			  Mockito.when(rs.getString("CREATED_BY")).thenReturn("XY452762");
			  Mockito.when(rs.getString("SCRATCHPAD")).thenReturn("sfdt");
			  Mockito.when(rs.getString("CREATED_DATE")).thenReturn("12/12/2009");
			  Mockito.when(rs.getString("NOTE_ID")).thenReturn("7890");
			  Mockito.when(rs.getString("NOTE_TEXT")).thenReturn("text note");			  
			  Mockito.when(rs.getString("ROLE")).thenReturn("OM52380");
			  Mockito.when(rs.getString("POLICY_NUMBER")).thenReturn("456");
			  
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapRowTest() throws SQLException {
		IPNoteHistoryBean iPNoteHistoryBean = iPNoteHistoryBeanRowMapper.mapRow(rs, 10);
	    assertEquals("OM52380", iPNoteHistoryBean.getRole());
	}
}
