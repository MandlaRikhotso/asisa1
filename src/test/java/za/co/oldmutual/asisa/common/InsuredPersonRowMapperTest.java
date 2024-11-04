package za.co.oldmutual.asisa.common;

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
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class InsuredPersonRowMapperTest extends AbstractTest {

  private final ResultSet rs = mock(ResultSet.class);

  @InjectMocks
  InsuredPersonRowMapper insuredPersonRowMapper;

  @Mock
  ReferenceDataCache referenceDataCache;

  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    try {
      Mockito.when(rs.getString("PERSON_ID")).thenReturn("1");
      Mockito.when(rs.getString("IDENTITY_NUMBER")).thenReturn("6789");
      Mockito.when(rs.getString("IDENTITY_NUMBER_TYPE_CODE")).thenReturn("4");
      Mockito.when(rs.getString("DATE_OF_BIRTH")).thenReturn("1");
      Mockito.when(rs.getString("TITLE")).thenReturn("1");
      Mockito.when(rs.getString("GENDER")).thenReturn("1");
      Mockito.when(rs.getString("SURNAME")).thenReturn("abn");
      Mockito.when(rs.getString("GIVEN_NAME_1")).thenReturn("john");
      Mockito.when(rs.getString("GIVEN_NAME_2")).thenReturn("");
      Mockito.when(rs.getString("GIVEN_NAME_3")).thenReturn("");
      Mockito.when(rs.getString("ADDRESS_LINE_1")).thenReturn("park street");
      Mockito.when(rs.getString("ADDRESS_LINE_2")).thenReturn("");
      Mockito.when(rs.getString("ADDRESS_LINE_3")).thenReturn("");
      Mockito.when(rs.getString("POSTAL_CODE")).thenReturn("1004");

    } catch (SQLException e) {

    }
  }

  @Test
  public void mapRowTest() throws SQLException {
    InsuredPersonBean insuredPersonBean = insuredPersonRowMapper.mapRow(rs, 14);
    assertEquals("abn", insuredPersonBean.getSurname());
  }
}
