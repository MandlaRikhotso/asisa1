package za.co.oldmutual.asisa.history;

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
import za.co.oldmutual.asisa.iphistory.IPClaimHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPClaimHistoryBeanRowMapper;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class IPClaimHistoryBeanRowMapperTest extends AbstractTest {

  @Mock
  private ReferenceDataCache referenceDataCache;

  private final ResultSet rs = mock(ResultSet.class);

  @InjectMocks
  IPClaimHistoryBeanRowMapper iPClaimHistoryBeanRowMapper;

  private ResultSetMetaData metaData = mock(ResultSetMetaData.class);

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException {
    super.setUp();
    try {
      Mockito.when(rs.getString("NOTIFICATION_ID")).thenReturn("1234");
      Mockito.when(rs.getString("CREATED_BY")).thenReturn("XY452762");
      Mockito.when(rs.getString("NOTIFICATION_SOURCE")).thenReturn("OM");
      Mockito.when(rs.getString("CREATED_DATE")).thenReturn("12/12/2009");
      Mockito.when(rs.getString("EVENT_DATE")).thenReturn("10/10/2010");
      Mockito.when(rs.getString("EVENT_DEATH_PLACE")).thenReturn("Africa");
      Mockito.when(rs.getString("EVENT_DEATH_CERTIFICATE_NO")).thenReturn("1233");
      Mockito.when(rs.getString("DHA1663_NUMBER")).thenReturn("12334");
      Mockito.when(rs.getString("POLICY_REF_NUMBER")).thenReturn("121");
      Mockito.when(rs.getString("POLICY_TYPE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("EVENT_CAUSE_CODE")).thenReturn("1");
      Mockito.when(rs.getString("CLAIM_REASON_CODES")).thenReturn("1");
      Mockito.when(rs.getString("CLAIM_STATUS_CODE")).thenReturn("1");
      Mockito.when(rs.getString("CLAIM_CATEGORY_CODE")).thenReturn("1");
      Mockito.when(rs.getString("PAYMENT_METHOD_CODE")).thenReturn("1");
      Mockito.when(rs.getInt("NOTIFIABLE_CLAIM_ID")).thenReturn(1);
      Mockito.when(rs.getString("NOTIFICATION_STATUS_CODE")).thenReturn("ACTIVE");
      Mockito.when(rs.getString("ROLE_CODE")).thenReturn("OM52380");
      Mockito.when(rs.getString("BUSINESS_UNIT")).thenReturn("FINANCE");
    } catch (SQLException e) {

    }

  }

  @Test
  public void mapRowTest() throws SQLException, DatatypeConfigurationException {

    IPClaimHistoryBean iPClaimHistoryBean = iPClaimHistoryBeanRowMapper.mapRow(rs, 19);
    assertEquals("OM52380", iPClaimHistoryBean.getUserRole());
  }
}
