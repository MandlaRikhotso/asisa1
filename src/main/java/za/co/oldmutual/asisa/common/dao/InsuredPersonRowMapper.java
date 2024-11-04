package za.co.oldmutual.asisa.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.GenderBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.TitleBean;

@Component
public class InsuredPersonRowMapper implements RowMapper<InsuredPersonBean> {

  private ReferenceDataCache referenceDataCache;

  IdentityTypeBean identityTypeBean = new IdentityTypeBean();
  GenderBean genderBean = new GenderBean();
  TitleBean titleBean = new TitleBean();

  public InsuredPersonRowMapper(ReferenceDataCache referenceDataCache) {
    super();
    this.referenceDataCache = referenceDataCache;
  }

  @Override
  public InsuredPersonBean mapRow(ResultSet rs, int rowNum) throws SQLException {
    InsuredPersonBean insuredPersonBean = new InsuredPersonBean();
    insuredPersonBean.setPersonID(rs.getString("PERSON_ID"));
    insuredPersonBean.setIdentityNumber(rs.getString("IDENTITY_NUMBER"));
    identityTypeBean.setCode(rs.getString("IDENTITY_NUMBER_TYPE_CODE"));
    identityTypeBean.setDescription(
        referenceDataCache.populateDescFromCode(identityTypeBean.getCode(), RefTypeEnum.IDTYPE));
    insuredPersonBean.setIdentityType(identityTypeBean);
    insuredPersonBean.setDateOfBirth(rs.getString("DATE_OF_BIRTH"));
    titleBean.setCode(rs.getString("TITLE"));
    titleBean.setDescription(
        referenceDataCache.populateDescFromCode(titleBean.getCode(), RefTypeEnum.TITLE));
    insuredPersonBean.setTitle(titleBean);
    genderBean.setCode(rs.getString("GENDER"));
    genderBean.setDescription(
        referenceDataCache.populateDescFromCode(genderBean.getCode(), RefTypeEnum.GENDER));
    insuredPersonBean.setGender(genderBean);
    
    insuredPersonBean.setSurname(rs.getString("SURNAME"));
    insuredPersonBean.setGivenName1(rs.getString("GIVEN_NAME_1"));
    insuredPersonBean.setGivenName2(rs.getString("GIVEN_NAME_2"));
    insuredPersonBean.setGivenName3(rs.getString("GIVEN_NAME_3"));
    insuredPersonBean.setAddressLine1(rs.getString("ADDRESS_LINE_1"));
    insuredPersonBean.setAddressLine2(rs.getString("ADDRESS_LINE_2"));
    insuredPersonBean.setAddressLine3(rs.getString("ADDRESS_LINE_3"));
    insuredPersonBean.setPostalCode(rs.getInt("POSTAL_CODE"));
    return insuredPersonBean;
  }
}
