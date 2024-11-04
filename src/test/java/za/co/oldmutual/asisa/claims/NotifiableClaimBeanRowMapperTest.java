package za.co.oldmutual.asisa.claims;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class NotifiableClaimBeanRowMapperTest extends AbstractTest {

	private final ResultSet rs = mock(ResultSet.class);

	@InjectMocks
	NotifiableClaimBeanRowMapper notifiableClaimBeanRowMapper;

	@Mock
	private ReferenceDataCache referenceDataCache;

	private ResultSetMetaData metaData = mock(ResultSetMetaData.class);

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
		super.setUp();
		try {

			Mockito.when(rs.getString("PAYMENT_METHOD_CODE")).thenReturn("1");
			Mockito.when(rs.getString("NOTIFICATION_ID")).thenReturn("1234");
			Mockito.when(rs.getInt("NOTIFIABLE_CLAIM_ID")).thenReturn(1);
			Mockito.when(rs.getString("EVENT_DATE")).thenReturn("12/12/2018");
			Mockito.when(rs.getString("EVENT_DEATH_PLACE")).thenReturn("Africa");
			Mockito.when(rs.getString("EVENT_DEATH_CERTIFICATE_NO")).thenReturn("1233");
			Mockito.when(rs.getString("DHA1663_NUMBER")).thenReturn("12334");
			Mockito.when(rs.getString("REASON_FOR_EDIT")).thenReturn("Reason");
			Mockito.when(rs.getString("EVENT_CAUSE_CODE")).thenReturn("1");
			Mockito.when(rs.getString("CLAIM_REASON_CODES")).thenReturn("1");
			Mockito.when(rs.getString("CLAIM_STATUS_CODE")).thenReturn("1");
			Mockito.when(rs.getString("CLAIM_CATEGORY_CODE")).thenReturn("1");

		} catch (SQLException e) {

		}

	}

	@Test
	public void mapRowTest() throws SQLException, DatatypeConfigurationException {

		NotifiableClaimBean notifiableClaimBean = notifiableClaimBeanRowMapper.mapRow(rs, 12);
		assertEquals("1", notifiableClaimBean.getClaimCategory().getCode());
	}
}
