package za.co.oldmutual.asisa.common.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import za.co.oldmutual.asisa.claims.NotifiableClaimsController;
import za.co.oldmutual.asisa.common.bean.NoteBean;

@Component
public class NoteDAO {

  @Autowired
  NamedParameterJdbcOperations namedParameterJdbcTemplate;

  private static final Logger LOGGER = LoggerFactory.getLogger(NotifiableClaimsController.class);

  public void insertNote(NoteBean note) {
    LOGGER.debug("Inserting note...");
    namedParameterJdbcTemplate.update(CommonQueries.INSERT_NOTE_QUERY,
        new BeanPropertySqlParameterSource(note));
  }

  public void insertScratchpad(NoteBean note) {
    LOGGER.debug("Inserting scratchpad...");
    namedParameterJdbcTemplate.update(CommonQueries.INSERT_SCRATCHPAD_QUERY,
        new BeanPropertySqlParameterSource(note));
  }
}
