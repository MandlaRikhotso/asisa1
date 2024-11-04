package za.co.oldmutual.asisa.iphistory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Component
public class IPImpairmentHistoryBeanRowMapper implements RowMapper<IPImpairmentHistoryBean> {

  @Autowired
  private ReferenceDataCache referenceDataCache;

  private static final String COMMA_SEPERATOR = "\\s*,\\s*";

  @Override
  public IPImpairmentHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
    ImpairmentCodeBean impairmentCodeBean = new ImpairmentCodeBean();
    PolicyTypeBean policyTypeBean = new PolicyTypeBean();
    IPImpairmentHistoryBean impairmentHistory = new IPImpairmentHistoryBean();
    impairmentHistory.setNotifiableImpairmentID(rs.getInt("NOTIFIABLE_IMPAIRMENT_ID"));
    impairmentCodeBean.setCode(rs.getString("IMPAIRMENT_CODE"));
    impairmentCodeBean.setDescription(referenceDataCache
        .populateDescFromCode(impairmentCodeBean.getCode(), RefTypeEnum.IMPAIRMENT_CODE));
    impairmentHistory.setImpairment(impairmentCodeBean);
    impairmentHistory.setDateCreated(rs.getString("CREATED_DATE"));
    impairmentHistory.setNotificationSource(referenceDataCache.populateDescFromCode(rs.getString("NOTIFICATION_SOURCE"), RefTypeEnum.OFFICE));
    impairmentHistory.setPolicyNumber(rs.getString("POLICY_REF_NUMBER"));
    policyTypeBean.setCode(rs.getString("POLICY_TYPE_CODE"));
    policyTypeBean.setDescription(
        referenceDataCache.populateDescFromCode(policyTypeBean.getCode(), RefTypeEnum.POLICY_TYPE));
    impairmentHistory.setPolicyBenefit(policyTypeBean);
    impairmentHistory.setReadings(rs.getString("READINGS"));
    if (StringUtils.isEmpty(rs.getString("LIFE_SPEC_CODES"))) {
      impairmentHistory.setSpecialInvestigation(new ArrayList<>());
    } else {
      List<LifeSpecBean> specialInvestigations = new ArrayList<>();
      Arrays.asList(rs.getString("LIFE_SPEC_CODES").split(COMMA_SEPERATOR)).forEach(e -> {
        LifeSpecBean lifeSpecBean = new LifeSpecBean();
        lifeSpecBean.setCode(e);
        lifeSpecBean.setDescription(
            referenceDataCache.populateDescFromCode(lifeSpecBean.getCode(), RefTypeEnum.LIFE_SPEC));
        specialInvestigations.add(lifeSpecBean);
      });
      impairmentHistory.setSpecialInvestigation(specialInvestigations);
    }
    if (StringUtils.isEmpty(rs.getString("LIFE_SYMBOL_CODES"))) {
      impairmentHistory.setSymbol(new ArrayList<>());
    } else {
      List<LifeSymbolBean> symbols = new ArrayList<>();
      Arrays.asList(rs.getString("LIFE_SYMBOL_CODES").split(COMMA_SEPERATOR)).forEach(e -> {
        LifeSymbolBean lifeSymbolBeans = new LifeSymbolBean();
        lifeSymbolBeans.setCode(e);
        lifeSymbolBeans.setDescription(referenceDataCache
            .populateDescFromCode(lifeSymbolBeans.getCode(), RefTypeEnum.LIFE_SYMBOL));
        symbols.add(lifeSymbolBeans);
      });
      impairmentHistory.setSymbol(symbols);
    }
    impairmentHistory.setTimeSignal(rs.getString("TIME_SIGNAL"));
    impairmentHistory.setNotificationStatus(rs.getString("NOTIFICATION_STATUS_CODE"));
    impairmentHistory.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
    impairmentHistory.setUserRole(rs.getString("ROLE_CODE"));
    impairmentHistory.setBusinessUnit(impairmentHistory.getNotificationSource());
    impairmentHistory.setNotificationID(rs.getString("NOTIFICATION_ID"));
    return impairmentHistory;
  }
}

