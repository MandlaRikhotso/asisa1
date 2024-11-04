package za.co.oldmutual.asisa.iphistory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

@Component
public class IPHistoryDAO {

  @Autowired
  InsuredPersonRowMapper insuredPersonRowMapper;

  @Autowired
  ReferenceDataCache referenceDataCache;

  @Autowired
  private NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  IPImpairmentHistoryBeanRowMapper iPImpairmentHistoryBeanRowMapper;

  @Autowired
  IPClaimHistoryBeanRowMapper iPClaimHistoryBeanRowMapper;
  
  @Autowired
  IPNoteHistoryBeanRowMapper ipNoteHistoryBeanRowMapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(IPHistoryDAO.class);

  private static final String PERSON_ID = "personId";

  public IPHistoryBean getInsuredPersonsHistory(String identityTypeCode, String identityNumber,
      String personId) throws ResourceNotFoundException {
    try {
      IPHistoryBean insuredPersonHistoryDetails = new IPHistoryBean();
      InsuredPersonBean insuredPersonDetails =
          getInsuredPersonDetails(identityTypeCode, identityNumber, personId);
      List<IPImpairmentHistoryBean> impairmentHistoryDetails =
          getImpairmentsHistoryDetails(insuredPersonDetails.getPersonID());
      List<IPClaimHistoryBean> claimHistoryDetails =
          getClaimsHistoryDetails(insuredPersonDetails.getPersonID());
      List<IPNoteHistoryBean> noteHistoryDetails =
          getNoteHistoryDetails(insuredPersonDetails.getPersonID());
      List<IPNoteHistoryBean> scratchpadHistoryDetails =
          getScratchpadHistoryDetails(insuredPersonDetails.getPersonID());
      insuredPersonHistoryDetails.setInsuredPerson(insuredPersonDetails);
      insuredPersonHistoryDetails.setImpairmentHistory(impairmentHistoryDetails);
      insuredPersonHistoryDetails.setClaimHistory(claimHistoryDetails);
      insuredPersonHistoryDetails.setNoteHistory(noteHistoryDetails);
      insuredPersonHistoryDetails.setScratchpadHistory(scratchpadHistoryDetails);
      return insuredPersonHistoryDetails;
    } catch (Exception e) {
      LOGGER.error("Fetching History Of Insured Person failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }

  }

  private InsuredPersonBean getInsuredPersonDetails(String identityTypeCode, String identityNumber,
      String personId) {
    LOGGER.debug("Initiating request to fetch InsuredPerson details");
    return namedParameterJdbcTemplate.queryForObject(
        IPHistoryDAOQueries.FETCH_INSURED_PERSON_DETAILS_QUERY,
        new MapSqlParameterSource("identityTypeCode", identityTypeCode)
            .addValue("identityNumber", identityNumber).addValue(PERSON_ID, personId),
        insuredPersonRowMapper);
  }

  private List<IPImpairmentHistoryBean> getImpairmentsHistoryDetails(String personId) {
    LOGGER.debug("Initiating request to fetch ImpairmentsHistory details..");
    return namedParameterJdbcTemplate.query(IPHistoryDAOQueries.FETCH_IMPAIRMENT_DETAILS_QUERY,
        new MapSqlParameterSource(PERSON_ID, personId), iPImpairmentHistoryBeanRowMapper);
  }

  private List<IPClaimHistoryBean> getClaimsHistoryDetails(String personId) {
    LOGGER.debug("Initiating request to fetch ClaimsHistory details..");
    return namedParameterJdbcTemplate.query(IPHistoryDAOQueries.FETCH_CLAIM_DETAILS_QUERY,
        new MapSqlParameterSource(PERSON_ID, personId), iPClaimHistoryBeanRowMapper);
  }

  private List<IPNoteHistoryBean> getNoteHistoryDetails(String personId) {
    LOGGER.debug("Initiating request to fetch NotesHistory details..");
    return namedParameterJdbcTemplate.query(IPHistoryDAOQueries.FETCH_NOTE_DETAILS_QUERY,
        new MapSqlParameterSource(PERSON_ID, personId),
        ipNoteHistoryBeanRowMapper);
  }

  private List<IPNoteHistoryBean> getScratchpadHistoryDetails(String personId) {
    LOGGER.debug("Initiating request to fetch ScratchpadHistory details..");
    return namedParameterJdbcTemplate.query(IPHistoryDAOQueries.FETCH_SCRATCHPAD_DETAILS_QUERY,
        new MapSqlParameterSource(PERSON_ID, personId),
        ipNoteHistoryBeanRowMapper);
  }
}
