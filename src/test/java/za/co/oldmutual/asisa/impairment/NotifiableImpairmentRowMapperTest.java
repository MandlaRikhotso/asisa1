package za.co.oldmutual.asisa.impairment;

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
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentBean;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentRowMapper;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class NotifiableImpairmentRowMapperTest extends AbstractTest {

  @Mock
  private ReferenceDataCache referenceDataCache;

  private final ResultSet rs = mock(ResultSet.class);

  @InjectMocks
  NotifiableImpairmentRowMapper notifiableImpairmentRowMapper;

  private ResultSetMetaData metaData = mock(ResultSetMetaData.class);

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    try {
      Mockito.when(rs.getString("IMPAIRMENT_CODE")).thenReturn("AD01");
      Mockito.when(rs.getString("READINGS")).thenReturn("12");
      Mockito.when(rs.getString("TIME_SIGNAL")).thenReturn("2092");
      Mockito.when(rs.getString("LIFE_SPEC_CODES")).thenReturn("1");
      Mockito.when(rs.getString("LIFE_SYMBOL_CODES")).thenReturn("Q");
      Mockito.when(rs.getString("NOTIFICATION_ID")).thenReturn("13432");
      Mockito.when(rs.getString("REASON_FOR_EDIT")).thenReturn("Reason");
      Mockito.when(rs.getInt("NOTIFIABLE_IMPAIRMENT_ID")).thenReturn(1);

    } catch (SQLException e) {

    }
  }

  @Test
  public void mapRowTxnTypeCreateTest() throws SQLException, DatatypeConfigurationException {

    NotifiableImpairmentBean notifiableImpairmentBean =
        notifiableImpairmentRowMapper.mapRow(rs, 16);
    assertEquals("AD01", notifiableImpairmentBean.getImpairment().getCode());
  }
}
