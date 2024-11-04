package za.co.oldmutual.asisa.common.dao;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.claims.NotifiableClaimsController;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;

@Component
public class InsuredPersonDAO {

  @Autowired
  NamedParameterJdbcOperations namedParameterJdbcTemplate;

  @Autowired
  UserLoginIdentifier userLoginIdentifier;

  private static final Logger LOGGER = LoggerFactory.getLogger(NotifiableClaimsController.class);

  public String insertInsuredPerson(InsuredPersonBean insuredPerson) {
    LOGGER.debug("Initiating request to insert InsuredPerson");
    String personID = UUID.randomUUID().toString().toUpperCase();
    insuredPerson.setPersonID(personID);
    insuredPerson.setCreatedBy(userLoginIdentifier.fetchUserDetails().toUpperCase());
    namedParameterJdbcTemplate.update(CommonQueries.INSERT_INSURED_PERSON,
        new BeanPropertySqlParameterSource(insuredPerson));
    return personID;
  }

  public IPEnquiryCriteriaBean getIPSearchCriteria(InsuredPersonBean insuredPerson) {
    IPEnquiryCriteriaBean criteriaBean = new IPEnquiryCriteriaBean();
    IdentityTypeBean identityTypeBean = new IdentityTypeBean();
    if (!StringUtils.isEmpty(insuredPerson.getIdentityType().getCode())) {
      identityTypeBean.setCode(insuredPerson.getIdentityType().getCode());
      identityTypeBean.getDescription();
      criteriaBean.setIdentityType(identityTypeBean);
    }
    if (!StringUtils.isEmpty(insuredPerson.getIdentityNumber())) {
      criteriaBean.setIdentityNumber(insuredPerson.getIdentityNumber());
    }
    if (!StringUtils.isEmpty(insuredPerson.getSurname())) {
      criteriaBean.setSurname(insuredPerson.getSurname());
    }
    if (!StringUtils.isEmpty(insuredPerson.getDateOfBirth())) {
      criteriaBean.setDateOfBirth(insuredPerson.getDateOfBirth());
    }
    return criteriaBean;
  }
}
