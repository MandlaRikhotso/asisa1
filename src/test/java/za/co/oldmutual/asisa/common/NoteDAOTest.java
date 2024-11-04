package za.co.oldmutual.asisa.common;

import java.text.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.common.dao.NoteDAO;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;

public class NoteDAOTest extends AbstractTest {
  @InjectMocks
  private NoteDAO noteDAO;

  @Mock
  NamedParameterJdbcOperations namedParameterJdbcTemplate;
  NoteBean noteBean = new NoteBean();

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    noteBean.setScratchpad("scratchpad");
    noteBean.setNoteText("Note Text");
  }

  @Test
  public void insertNoteTest() throws ParseException, ResourceUpdationFailedException {
    Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
        Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
    noteDAO.insertNote(noteBean);
  }

  @Test
  public void insertScratchpadTest() throws ParseException, ResourceUpdationFailedException {
    Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
        Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
    noteDAO.insertScratchpad(noteBean);
  }
}
