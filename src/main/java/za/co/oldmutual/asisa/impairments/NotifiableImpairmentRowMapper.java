package za.co.oldmutual.asisa.impairments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.common.bean.DataBaseColumnEnum;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;

@Component
public class NotifiableImpairmentRowMapper implements RowMapper<NotifiableImpairmentBean> {
  
  @Autowired
  private ReferenceDataCache referenceDataCache;
  
  private static final String COMMA_SEPERATOR = "\\s*,\\s*";
  
  @Override
  public NotifiableImpairmentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
      NotifiableImpairmentBean notifiableImpairmentBean = new NotifiableImpairmentBean();
      ImpairmentCodeBean impairmentCodeBean = new ImpairmentCodeBean();
      
      impairmentCodeBean.setCode(rs.getString("IMPAIRMENT_CODE"));
      impairmentCodeBean.setDescription(
              referenceDataCache.populateDescFromCode(impairmentCodeBean.getCode(), RefTypeEnum.IMPAIRMENT_CODE));
      notifiableImpairmentBean.setImpairment(impairmentCodeBean);
      notifiableImpairmentBean.setReadings(rs.getString("READINGS"));
      notifiableImpairmentBean.setTimeSignal(rs.getString("TIME_SIGNAL"));
      
      List<LifeSpecBean> specialInvestigations = new ArrayList<>();
      if (!StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.LIFE_SPEC_CODES.toString()))) {
          Arrays.asList(rs.getString(DataBaseColumnEnum.LIFE_SPEC_CODES.toString()).split(COMMA_SEPERATOR)).forEach(e -> {
              LifeSpecBean specialInvestigationBean = new LifeSpecBean();
              specialInvestigationBean.setCode(e);
              specialInvestigationBean.setDescription(referenceDataCache
                      .populateDescFromCode(specialInvestigationBean.getCode(), RefTypeEnum.LIFE_SPEC));
              specialInvestigations.add(specialInvestigationBean);
          });
          notifiableImpairmentBean.setSpecialInvestigationcode(rs.getString(DataBaseColumnEnum.LIFE_SPEC_CODES.toString()));
      }
      notifiableImpairmentBean.setSpecialInvestigation(specialInvestigations);
                     
      
      List<LifeSymbolBean> symbols = new ArrayList<>();
      if (!StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString()))) {
          Arrays.asList(rs.getString(DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString()).split(COMMA_SEPERATOR)).forEach(e -> {
              LifeSymbolBean symbolBean = new LifeSymbolBean();
              symbolBean.setCode(e);
              symbolBean.setDescription(
                      referenceDataCache.populateDescFromCode(symbolBean.getCode(), RefTypeEnum.LIFE_SYMBOL));
              symbols.add(symbolBean);
          });
          notifiableImpairmentBean.setSymbolcode(rs.getString(DataBaseColumnEnum.LIFE_SYMBOL_CODES.toString()));
      }
      notifiableImpairmentBean.setSymbol(symbols);
      notifiableImpairmentBean.setNotificationID(rs.getString("NOTIFICATION_ID"));
      notifiableImpairmentBean.setUpdateReason(rs.getString("REASON_FOR_EDIT"));
      notifiableImpairmentBean.setNotifiableImpairmentID(rs.getInt("NOTIFIABLE_IMPAIRMENT_ID"));
      return notifiableImpairmentBean;
  }
}


