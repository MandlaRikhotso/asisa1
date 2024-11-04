package za.co.oldmutual.asisa.enquiry;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonRowMapper;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

@Component
public class IPEnquiryDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(IPEnquiryDAO.class);

  @Autowired
  private NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  ReferenceDataCache referenceDataCache;

  @Autowired
  InsuredPersonRowMapper insuredPersonRowMapper;


  public boolean isInsuredPersonExists(IPEnquiryCriteriaBean ipSearchCriteria)
      throws ResourceNotFoundException {
    Integer recordCount = 0;
    try {
      if (!StringUtils.isEmpty(ipSearchCriteria.getIdentityNumber())
          && !(StringUtils.isEmpty(ipSearchCriteria.getIdentityType().getCode()))) {
        recordCount = namedParameterJdbcTemplate.queryForObject(
            IPEnquiryDAOQueries.IS_EXISTS_BY_IDTYPE_AND_NUMBER_QUERY,
            new BeanPropertySqlParameterSource(ipSearchCriteria), Integer.class);
      } else if (!StringUtils.isEmpty(ipSearchCriteria.getDateOfBirth())
          && !(StringUtils.isEmpty(ipSearchCriteria.getSurname()))) {
        recordCount = namedParameterJdbcTemplate.queryForObject(
            IPEnquiryDAOQueries.IS_EXISTS_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY,
            new BeanPropertySqlParameterSource(ipSearchCriteria), Integer.class);
      }
    } catch (Exception e) {
      LOGGER.error("Insured Person Not Found : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
    if (recordCount == 0) {
      LOGGER.debug("No InsuredPersonBean found in ASISA local Register with the given criteria");
      return false;
    } else {
      LOGGER.debug("InsuredPersonBean found in ASISA local Register with the given criteria");
      return true;
    }
  }

  public List<InsuredPersonBean> getInsuredPersons(IPEnquiryCriteriaBean ipSearchCriteria)
      throws ResourceNotFoundException {
    List<InsuredPersonBean> insuredPersons = null;
    try {
      if (!StringUtils.isEmpty(ipSearchCriteria.getIdentityNumber())
          && !(StringUtils.isEmpty(ipSearchCriteria.getIdentityType().getCode()))) {
        insuredPersons = checkIfPerfectMatchById(ipSearchCriteria,
            IPEnquiryDAOQueries.FIND_BY_IDTYPE_AND_NUMBER_QUERY,
            IPEnquiryDAOQueries.FIND_IMPERFECT_MATCH_BY_IDTYPE_AND_NUMBER_QUERY);
      } else if (!StringUtils.isEmpty(ipSearchCriteria.getDateOfBirth())
          && !(StringUtils.isEmpty(ipSearchCriteria.getSurname()))) {
        insuredPersons = checkIfPerfectMatchBySurname(ipSearchCriteria,
            IPEnquiryDAOQueries.FIND_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY,
            IPEnquiryDAOQueries.FIND_IMPERFECT_MATCH_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY);
      }
      if (StringUtils.isEmpty(insuredPersons)) {
        LOGGER.info("Insured Persons records not found for the given criteria");
      } else {
        LOGGER.info("Insured Persons records found for the given criteria");
      }
      return insuredPersons;
    } catch (Exception e) {
      LOGGER.error("Fetching Insured Person Details failed : {}", e.getMessage());
      throw new ResourceNotFoundException();
    }
  }

  private List<InsuredPersonBean> checkIfPerfectMatchBySurname(
      IPEnquiryCriteriaBean ipSearchCriteria, String findBySurnameAndDateOfBirthSqlQuery,
      String findImperfectMatchBySurnameAndDateOfBirthSqlQuery) {
    LOGGER.debug("Checking if it is PerfectMatch by Surname..");
    List<InsuredPersonBean> insuredPersons;
    insuredPersons = namedParameterJdbcTemplate.query(findBySurnameAndDateOfBirthSqlQuery,
        new BeanPropertySqlParameterSource(ipSearchCriteria), insuredPersonRowMapper);
    if (!insuredPersons.isEmpty()) {
      insuredPersons.forEach(n -> n.setPerfectMatch(true));
    } else {
      if (ipSearchCriteria.getSurname().length() >= 3) {
        String trimmedSurname = ipSearchCriteria.getSurname().substring(0, 2);
        ipSearchCriteria.setSurname(trimmedSurname + "%");
        insuredPersons =
            namedParameterJdbcTemplate.query(findImperfectMatchBySurnameAndDateOfBirthSqlQuery,
                new BeanPropertySqlParameterSource(ipSearchCriteria), insuredPersonRowMapper);
        if (!insuredPersons.isEmpty()) {
          insuredPersons.forEach(n -> n.setPerfectMatch(false));
        }
      } else {
        return insuredPersons;
      }
    }
    return insuredPersons;
  }

  private List<InsuredPersonBean> checkIfPerfectMatchById(IPEnquiryCriteriaBean ipSearchCriteria,
      String findByIDTypeAndNumberSqlQuery, String findImperfectMatchByIDTypeAndNumberSqlQuery) {
    LOGGER.debug("Checking if it is PerfectMatch by IdentityNumber..");
    List<InsuredPersonBean> insuredPersons;
    insuredPersons = namedParameterJdbcTemplate.query(findByIDTypeAndNumberSqlQuery,
        new BeanPropertySqlParameterSource(ipSearchCriteria), insuredPersonRowMapper);
    if (!insuredPersons.isEmpty()) {
      insuredPersons.forEach(n -> n.setPerfectMatch(true));
    } else {
      if (ipSearchCriteria.getIdentityNumber().length() >= 10) {
        String trimmedIdNumber = ipSearchCriteria.getIdentityNumber().substring(0, 10);
        ipSearchCriteria.setIdentityNumber(trimmedIdNumber + "%");
        insuredPersons =
            namedParameterJdbcTemplate.query(findImperfectMatchByIDTypeAndNumberSqlQuery,
                new BeanPropertySqlParameterSource(ipSearchCriteria), insuredPersonRowMapper);
        if (!insuredPersons.isEmpty()) {
          insuredPersons.forEach(n -> n.setPerfectMatch(false));
        }
      } else {
        return insuredPersons;
      }
    }
    return insuredPersons;
  }

  public String getInsuredPersonID(IPEnquiryCriteriaBean ipSearchCriteria) {
    LOGGER.debug("Searching for InsuredpersonId...");
    String personID = "0";
    try {
      if (!ipSearchCriteria.getIdentityNumber().isEmpty()
          && !ipSearchCriteria.getIdentityType().getCode().isEmpty()) {
        personID = namedParameterJdbcTemplate.queryForObject(
            IPEnquiryDAOQueries.FIND_PERSON_BY_IDTYPE_AND_NUMBER_QUERY,
            new BeanPropertySqlParameterSource(ipSearchCriteria), String.class);
      } else if (!ipSearchCriteria.getDateOfBirth().isEmpty()
          && !ipSearchCriteria.getSurname().isEmpty()) {
        personID = namedParameterJdbcTemplate.queryForObject(
            IPEnquiryDAOQueries.FIND_PERSON_BY_SURNAME_AND_DATE_OF_BIRTH_QUERY,
            new BeanPropertySqlParameterSource(ipSearchCriteria), String.class);
      }
    } catch (EmptyResultDataAccessException e) {
      LOGGER.error("Exception occurred with the details {}", e.getMessage());
      LOGGER.error("action = getInsuredPersonID in IPEnquiryDAO = {}", ipSearchCriteria);
      return "0";
    }
    return personID;
  }

  public String getInsuredPersonByNotificationID(String notificationID) {
    return namedParameterJdbcTemplate.queryForObject(
        IPEnquiryDAOQueries.FETCH_INSURED_PERSONID_QUERY,
        new MapSqlParameterSource("notificationID", notificationID), String.class);
  }
}
