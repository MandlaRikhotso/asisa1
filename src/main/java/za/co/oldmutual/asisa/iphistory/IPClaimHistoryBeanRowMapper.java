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
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Component
public class IPClaimHistoryBeanRowMapper implements RowMapper<IPClaimHistoryBean> {

  @Autowired
  private ReferenceDataCache referenceDataCache;

  private static final String COMMA_SEPERATOR = "\\s*,\\s*";

  @Override
  public IPClaimHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
    ClaimStatusBean claimStatusBean = new ClaimStatusBean();
    PaymentMethodBean paymentMethodBean = new PaymentMethodBean();
    ClaimCauseBean claimCauseBean = new ClaimCauseBean();
    IPClaimHistoryBean claimHistory = new IPClaimHistoryBean();
    PolicyTypeBean policyTypeBean = new PolicyTypeBean();
    claimHistory.setNotifiableClaimID(rs.getInt("NOTIFIABLE_CLAIM_ID"));
    if (StringUtils.isEmpty(rs.getString("CLAIM_CATEGORY_CODE"))) {
      claimHistory.setClaimCategory(new ArrayList<>());
    } else {
      List<ClaimCategoryBean> claimCategorys = new ArrayList<>();
      Arrays.asList(rs.getString("CLAIM_CATEGORY_CODE").split(COMMA_SEPERATOR)).forEach(e -> {
        ClaimCategoryBean claimCategoryBean = new ClaimCategoryBean();
        claimCategoryBean.setCode(e);
        claimCategoryBean.setDescription(referenceDataCache
            .populateDescFromCode(claimCategoryBean.getCode(), RefTypeEnum.CLAIM_CATEGORY));
        claimCategorys.add(claimCategoryBean);
      });
      claimHistory.setClaimCategory(claimCategorys);
    }
    claimHistory.setEventDate(rs.getString("EVENT_DATE"));
    if(StringUtils.isEmpty(rs.getString("EVENT_CAUSE_CODE"))) {
      claimHistory.setEventCause(null);
    }
    else {
    claimCauseBean.setCode(rs.getString("EVENT_CAUSE_CODE"));
    claimCauseBean.setDescription(
        referenceDataCache.populateDescFromCode(claimCauseBean.getCode(), RefTypeEnum.CLAIM_CAUSE));
    claimHistory.setEventCause(claimCauseBean);
    }
    claimHistory.setEventDeathPlace(rs.getString("EVENT_DEATH_PLACE"));
    claimHistory.setEventDeathCertificateNo(rs.getString("EVENT_DEATH_CERTIFICATE_NO"));
    claimHistory.setDha1663Number(rs.getString("DHA1663_NUMBER"));
    claimHistory.setPolicyNumber(rs.getString("POLICY_REF_NUMBER"));
    policyTypeBean.setCode(rs.getString("POLICY_TYPE_CODE"));
    policyTypeBean.setDescription(
        referenceDataCache.populateDescFromCode(policyTypeBean.getCode(), RefTypeEnum.POLICY_TYPE));
    claimHistory.setClaimType(policyTypeBean);
    claimHistory.setDateCreated(rs.getString("CREATED_DATE"));
    claimHistory.setNotificationSource(referenceDataCache.populateDescFromCode(rs.getString("NOTIFICATION_SOURCE"), RefTypeEnum.OFFICE));
    if (StringUtils.isEmpty(rs.getString("CLAIM_REASON_CODES"))) {
      claimHistory.setClaimReason(new ArrayList<>());
    } else {
      List<ImpairmentCodeBean> impairmentCodes = new ArrayList<>();
      Arrays.asList(rs.getString("CLAIM_REASON_CODES").split(COMMA_SEPERATOR)).forEach(e -> {
        ImpairmentCodeBean impairmentCodeBean = new ImpairmentCodeBean();
        impairmentCodeBean.setCode(e);
        impairmentCodeBean.setDescription(referenceDataCache
            .populateDescFromCode(impairmentCodeBean.getCode(), RefTypeEnum.IMPAIRMENT_CODE));
        impairmentCodes.add(impairmentCodeBean);
      });
      claimHistory.setClaimReason(impairmentCodes);
    }
    if(StringUtils.isEmpty(rs.getString("CLAIM_STATUS_CODE"))) {
            claimHistory.setClaimStatus(null);
        }
    else {
    claimStatusBean.setCode(rs.getString("CLAIM_STATUS_CODE"));
    claimStatusBean.setDescription(referenceDataCache
        .populateDescFromCode(claimStatusBean.getCode(), RefTypeEnum.CLAIM_STATUS));
    claimHistory.setClaimStatus(claimStatusBean);
    }
    if(StringUtils.isEmpty(rs.getString("PAYMENT_METHOD_CODE"))){
      claimHistory.setPaymentMethod(null);
    }
    else {
    paymentMethodBean.setCode(rs.getString("PAYMENT_METHOD_CODE"));
    paymentMethodBean.setDescription(referenceDataCache
        .populateDescFromCode(paymentMethodBean.getCode(), RefTypeEnum.PAYMENT_METHOD));
    claimHistory.setPaymentMethod(paymentMethodBean);
    }
    claimHistory.setNotificationStatus(rs.getString("NOTIFICATION_STATUS_CODE"));
    claimHistory.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
    claimHistory.setUserRole(rs.getString("ROLE_CODE"));
    claimHistory.setBusinessUnit(claimHistory.getNotificationSource());
    claimHistory.setNotificationID(rs.getString("NOTIFICATION_ID"));
    return claimHistory;
  }
}
