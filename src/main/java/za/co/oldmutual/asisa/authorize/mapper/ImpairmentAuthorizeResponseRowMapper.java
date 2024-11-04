package za.co.oldmutual.asisa.authorize.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.authorize.ImpairmentAuthorizeResponse;
import za.co.oldmutual.asisa.common.bean.DataBaseColumnEnum;
import za.co.oldmutual.asisa.common.validation.HasColumnIdentifier;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Component
public class ImpairmentAuthorizeResponseRowMapper
    implements RowMapper<ImpairmentAuthorizeResponse> {

  @Autowired
  private ReferenceDataCache referenceDataCache;

  @Autowired
  HasColumnIdentifier column;

  private static final String COMMA_SEPERATOR = "\\s*,\\s*";

  @Override
  public ImpairmentAuthorizeResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
    ImpairmentAuthorizeResponse impairmentAuthorizeResponse = new ImpairmentAuthorizeResponse();

    ResultSetMetaData metaData = rs.getMetaData();

    setImpairment(rs, impairmentAuthorizeResponse, metaData);

    setLifeOffice(rs, impairmentAuthorizeResponse, metaData);

    setCreatedBy(rs, impairmentAuthorizeResponse, metaData);

    setCreatedDate(rs, impairmentAuthorizeResponse, metaData);

    setPolicyBenefit(rs, impairmentAuthorizeResponse, metaData);

    setPolicyNumber(rs, impairmentAuthorizeResponse, metaData);

    setReadings(rs, impairmentAuthorizeResponse, metaData);

    setTimeSignal(rs, impairmentAuthorizeResponse, metaData);

    setSpecialInvestigation(rs, impairmentAuthorizeResponse, metaData);

    setSymbol(rs, impairmentAuthorizeResponse, metaData);

    setNotificationID(rs, impairmentAuthorizeResponse, metaData);

    setReasonForEdit(rs, impairmentAuthorizeResponse, metaData);

    setNotificationStatus(rs, impairmentAuthorizeResponse, metaData);

    setIdentityNumber(rs, impairmentAuthorizeResponse, metaData);

    setIdentityType(rs, impairmentAuthorizeResponse, metaData);
    
    setTransType(rs, impairmentAuthorizeResponse, metaData);
    
    setNotifiableImpairmentID(rs, impairmentAuthorizeResponse, metaData);
    
    return impairmentAuthorizeResponse;
  }

private void setNotifiableImpairmentID(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "NOTIFIABLE_IMPAIRMENT_ID")) {
      impairmentAuthorizeResponse.setNotifiableImpairmentID(rs.getInt("NOTIFIABLE_IMPAIRMENT_ID"));
    }
}

private void setTransType(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "NOTIFICATION_TXN_TYPE")) {
      impairmentAuthorizeResponse.setTransType(rs.getString("NOTIFICATION_TXN_TYPE"));
    }
}

private void setIdentityType(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "IDENTITY_NUMBER_TYPE_CODE")) {
      IdentityTypeBean identityTypeBean = new IdentityTypeBean();
      identityTypeBean.setCode(rs.getString("IDENTITY_NUMBER_TYPE_CODE"));
      identityTypeBean.setDescription(
          referenceDataCache.populateDescFromCode(identityTypeBean.getCode(), RefTypeEnum.IDTYPE));
      impairmentAuthorizeResponse.setIdentityType(identityTypeBean);
    }
}

private void setIdentityNumber(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "IDENTITY_NUMBER")) {
      impairmentAuthorizeResponse.setIdentityNumber(rs.getString("IDENTITY_NUMBER"));
    }
}

private void setNotificationStatus(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "NOTIFICATION_STATUS_CODE")) {
      impairmentAuthorizeResponse.setNotificationStatus(rs.getString("NOTIFICATION_STATUS_CODE"));
    }
}

private void setReasonForEdit(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "REASON_FOR_EDIT")) {
      impairmentAuthorizeResponse.setReasonForEdit(rs.getString("REASON_FOR_EDIT"));
    }
}

private void setNotificationID(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "NOTIFICATION_ID")) {
      impairmentAuthorizeResponse.setNotificationID(rs.getString("NOTIFICATION_ID"));
    }
}

private void setSymbol(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString())) {
      List<LifeSymbolBean> symbols = new ArrayList<>();
      if (!StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString()))) {
        Arrays.asList(
            rs.getString(DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString()).split(COMMA_SEPERATOR))
            .forEach(e -> {
              LifeSymbolBean lifeSymbolBean = new LifeSymbolBean();
              lifeSymbolBean.setCode(e);
              lifeSymbolBean.setDescription(referenceDataCache
                  .populateDescFromCode(lifeSymbolBean.getCode(), RefTypeEnum.LIFE_SYMBOL));
              symbols.add(lifeSymbolBean);
            });
      }
      impairmentAuthorizeResponse.setSymbol(symbols);
      impairmentAuthorizeResponse.setSymbolcode(rs.getString(DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString()));
    }
}

private void setSpecialInvestigation(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, DataBaseColumnEnum.LIFE_SPEC_CODES.toString())) {
      List<LifeSpecBean> specialInvestigations = new ArrayList<>();
      if (!StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.LIFE_SPEC_CODES.toString()))) {
        Arrays
            .asList(
                rs.getString(DataBaseColumnEnum.LIFE_SPEC_CODES.toString()).split(COMMA_SEPERATOR))
            .forEach(e -> {
              LifeSpecBean lifeSpecBean = new LifeSpecBean();
              lifeSpecBean.setCode(e);
              lifeSpecBean.setDescription(referenceDataCache
                  .populateDescFromCode(lifeSpecBean.getCode(), RefTypeEnum.LIFE_SPEC));
              specialInvestigations.add(lifeSpecBean);
            });
      }
      impairmentAuthorizeResponse.setSpecialInvestigation(specialInvestigations);
      impairmentAuthorizeResponse.setSpecialInvestigationcode(rs.getString(DataBaseColumnEnum.LIFE_SPEC_CODES.toString()));
    }
}

private void setTimeSignal(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "TIME_SIGNAL")) {
      impairmentAuthorizeResponse.setTimeSignal(rs.getString("TIME_SIGNAL"));
    }
}

private void setReadings(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "READINGS")) {
      impairmentAuthorizeResponse.setReadings(rs.getString("READINGS"));
    }
}

private void setPolicyNumber(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "POLICY_REF_NUMBER")) {
      impairmentAuthorizeResponse.setPolicyNumber(rs.getString("POLICY_REF_NUMBER"));
    }
}

private void setPolicyBenefit(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "POLICY_TYPE_CODE")) {
      PolicyTypeBean policyTypeBean = new PolicyTypeBean();
      policyTypeBean.setCode(rs.getString("POLICY_TYPE_CODE"));
      policyTypeBean.setDescription(referenceDataCache
          .populateDescFromCode(policyTypeBean.getCode(), RefTypeEnum.POLICY_TYPE));
      impairmentAuthorizeResponse.setPolicyBenefit(policyTypeBean);
    }
}

private void setCreatedDate(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "CREATED_DATE")) {
      impairmentAuthorizeResponse.setCreatedDate(rs.getString("CREATED_DATE"));
    }
}

private void setCreatedBy(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "CREATED_BY")) {
      impairmentAuthorizeResponse.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
    }
}

private void setLifeOffice(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "NOTIFICATION_SOURCE")) {
      impairmentAuthorizeResponse.setLifeOffice(rs.getString("NOTIFICATION_SOURCE"));
    }
}

private void setImpairment(ResultSet rs, ImpairmentAuthorizeResponse impairmentAuthorizeResponse,
		ResultSetMetaData metaData) throws SQLException {
	if (column.hasColumn(metaData, "IMPAIRMENT_CODE")) {
      ImpairmentCodeBean impairmentCodeBean = new ImpairmentCodeBean();
      impairmentCodeBean.setCode(rs.getString("IMPAIRMENT_CODE"));
      impairmentCodeBean.setDescription(referenceDataCache
          .populateDescFromCode(impairmentCodeBean.getCode(), RefTypeEnum.IMPAIRMENT_CODE));
      impairmentAuthorizeResponse.setImpairment(impairmentCodeBean);
    }
}
  
}
