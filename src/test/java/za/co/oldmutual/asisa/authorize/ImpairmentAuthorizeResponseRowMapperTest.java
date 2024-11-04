package za.co.oldmutual.asisa.authorize;

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
import za.co.oldmutual.asisa.authorize.mapper.ImpairmentAuthorizeResponseRowMapper;
import za.co.oldmutual.asisa.common.validation.HasColumnIdentifier;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class ImpairmentAuthorizeResponseRowMapperTest extends AbstractTest {

  @Mock
  private ReferenceDataCache referenceDataCache;
  @Mock
  HasColumnIdentifier columns;
  private final ResultSet rs = mock(ResultSet.class);

  @InjectMocks
  ImpairmentAuthorizeResponseRowMapper impairmentAuthorizeResponseRowMapper;

  private ResultSetMetaData metaData = mock(ResultSetMetaData.class);

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    try {
      Mockito.when(rs.getString("IMPAIRMENT_CODE")).thenReturn("AD01");
      Mockito.when(rs.getString("NOTIFICATION_SOURCE")).thenReturn("OM");
      Mockito.when(rs.getString("CREATED_BY")).thenReturn("XY43713");
      Mockito.when(rs.getString("CREATED_DATE")).thenReturn("2018-09-09 00:00:00");
      Mockito.when(rs.getString("POLICY_TYPE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("POLICY_REF_NUMBER")).thenReturn("123");
      Mockito.when(rs.getString("READINGS")).thenReturn("12");
      Mockito.when(rs.getString("TIME_SIGNAL")).thenReturn("2092");
      Mockito.when(rs.getString("LIFE_SPEC_CODES")).thenReturn("1");
      Mockito.when(rs.getString("LIFE_SYMBOL_CODES")).thenReturn("Q");
      Mockito.when(rs.getString("NOTIFICATION_ID")).thenReturn("13432");
      Mockito.when(rs.getString("REASON_FOR_EDIT")).thenReturn("Reason");
      Mockito.when(rs.getString("NOTIFICATION_STATUS_CODE")).thenReturn("ACTIVE");
      Mockito.when(rs.getString("IDENTITY_NUMBER")).thenReturn("12324");
      Mockito.when(rs.getString("IDENTITY_NUMBER_TYPE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("NOTIFICATION_TXN_TYPE")).thenReturn("UPDATE");
      Mockito.when(rs.getInt("NOTIFIABLE_IMPAIRMENT_ID")).thenReturn(1);
      Mockito.when(columns.hasColumn(Mockito.any(), Mockito.any())).thenReturn(true);
    } catch (SQLException e) {

    }
  }

  @Test
  public void mapRowTxnTypeCreateTest() throws SQLException, DatatypeConfigurationException {

    ImpairmentAuthorizeResponse impairmentAuthorizeResponse =
        impairmentAuthorizeResponseRowMapper.mapRow(rs, 16);
    assertEquals("AD01", impairmentAuthorizeResponse.getImpairment().getCode());
  }
}
