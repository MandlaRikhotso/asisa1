package za.co.oldmutual.asisa.authorize;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import java.sql.Date;
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
import za.co.oldmutual.asisa.authorize.mapper.ClaimAuthorizeResponseRowMapper;
import za.co.oldmutual.asisa.common.validation.HasColumnIdentifier;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class ClaimAuthorizeResponseRowMapperTest extends AbstractTest {

  @Mock
  private ReferenceDataCache referenceDataCache;
  @Mock
  HasColumnIdentifier columns;
  private final ResultSet rs = mock(ResultSet.class);

  @InjectMocks
  ClaimAuthorizeResponseRowMapper claimAuthorizeResponseRowMapper;

  private ResultSetMetaData metaData = mock(ResultSetMetaData.class);

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    try {
      Mockito.when(rs.getString("NOTIFICATION_ID")).thenReturn("1234");
      Mockito.when(rs.getString("CREATED_BY")).thenReturn("XY452762");
      Mockito.when(rs.getString("NOTIFICATION_SOURCE")).thenReturn("OM");
      Mockito.when(rs.getDate("CREATED_DATE")).thenReturn(new Date(System.currentTimeMillis()));
      Mockito.when(rs.getDate("EVENT_DATE")).thenReturn(new Date(System.currentTimeMillis()));
      Mockito.when(rs.getString("EVENT_DEATH_PLACE")).thenReturn("Africa");
      Mockito.when(rs.getString("EVENT_DEATH_CERTIFICATE_NO")).thenReturn("1233");
      Mockito.when(rs.getString("DHA1663_NUMBER")).thenReturn("12334");
      Mockito.when(rs.getString("POLICY_REF_NUMBER")).thenReturn("121");
      Mockito.when(rs.getString("REASON_FOR_EDIT")).thenReturn("Reason");
      Mockito.when(rs.getString("POLICY_TYPE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("EVENT_CAUSE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("NOTIFICATION_TXN_TYPE")).thenReturn("UPDATE");
      Mockito.when(rs.getString("IDENTITY_NUMBER")).thenReturn("12334");
      Mockito.when(rs.getString("IDENTITY_NUMBER_TYPE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("CLAIM_REASON_CODES")).thenReturn("1");
      Mockito.when(rs.getString("CLAIM_STATUS_CODE")).thenReturn("1");
      Mockito.when(rs.getString("CLAIM_CATEGORY_CODE")).thenReturn("1");
      Mockito.when(rs.getString("PAYMENT_METHOD_CODE")).thenReturn("1");
      Mockito.when(rs.getString("ASTUTE_REF_NO")).thenReturn("1");
      Mockito.when(rs.getString("IS_ACTIVE")).thenReturn("1");

      Mockito.when(columns.hasColumn(Mockito.any(), Mockito.any())).thenReturn(true);
    } catch (SQLException e) {

    }

  }

  @Test
  public void mapRowTest() throws SQLException, DatatypeConfigurationException {

    ClaimAuthorizeResponse claimAuthorizeResponse = claimAuthorizeResponseRowMapper.mapRow(rs, 21);
    assertEquals("OM", claimAuthorizeResponse.getLifeOffice());
  }
}
