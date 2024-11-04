package za.co.oldmutual.asisa.claims;

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
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;

@Component
public class NotifiableClaimBeanRowMapper implements RowMapper<NotifiableClaimBean> {

	@Autowired
	private ReferenceDataCache referenceDataCache;

	private static final String COMMA_SEPERATOR = "\\s*,\\s*";

	@Override
	public NotifiableClaimBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		NotifiableClaimBean claim = new NotifiableClaimBean();
		claim.setNotifiableClaimID(rs.getInt("NOTIFIABLE_CLAIM_ID"));

		ClaimCategoryBean claimCategoryBean = new ClaimCategoryBean();
		claimCategoryBean.setCode(rs.getString("CLAIM_CATEGORY_CODE"));
		claimCategoryBean.setDescription(
				referenceDataCache.populateDescFromCode(claimCategoryBean.getCode(), RefTypeEnum.CLAIM_CATEGORY));
		claim.setClaimCategory(claimCategoryBean);
		claim.setEventDate(rs.getDate("EVENT_DATE") != null ? rs.getDate("EVENT_DATE").toString() : "");
		ClaimCauseBean claimCauseBean = new ClaimCauseBean();
		claimCauseBean.setCode(rs.getString("EVENT_CAUSE_CODE"));
		claimCauseBean.setDescription(
				referenceDataCache.populateDescFromCode(claimCauseBean.getCode(), RefTypeEnum.CLAIM_CAUSE));
		claim.setEventCause(claimCauseBean);
		claim.setEventDeathPlace(rs.getString("EVENT_DEATH_PLACE") != null ? rs.getString("EVENT_DEATH_PLACE") : "");
		claim.setEventDeathCertificateNo(
				rs.getString("EVENT_DEATH_CERTIFICATE_NO") != null ? rs.getString("EVENT_DEATH_CERTIFICATE_NO") : "");
		claim.setDha1663Number(rs.getString("DHA1663_NUMBER") != null ? rs.getString("DHA1663_NUMBER") : "");
		List<ImpairmentCodeBean> impairmentCodeBeanList = new ArrayList<>();
		if (StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.CLAIM_REASON_CODES.toString()))) {
			ImpairmentCodeBean impairmentCodeBean = new ImpairmentCodeBean();
			impairmentCodeBean.setCode("");
			impairmentCodeBeanList.add(impairmentCodeBean);
			claim.setClaimReason(impairmentCodeBeanList);
		} else {
			Arrays.asList(rs.getString(DataBaseColumnEnum.CLAIM_REASON_CODES.toString()).split(COMMA_SEPERATOR))
					.forEach(e -> {
						ImpairmentCodeBean impairmentCodeBean = new ImpairmentCodeBean();
						impairmentCodeBean.setCode(e);
						impairmentCodeBean.setDescription(referenceDataCache
								.populateDescFromCode(impairmentCodeBean.getCode(), RefTypeEnum.IMPAIRMENT_CODE));
						impairmentCodeBeanList.add(impairmentCodeBean);
					});

			claim.setClaimReason(impairmentCodeBeanList);
			claim.setClaimReasonCode(rs.getString(DataBaseColumnEnum.CLAIM_REASON_CODES.toString()));
		}
		ClaimStatusBean claimStatusBean = new ClaimStatusBean();
		claimStatusBean.setCode(rs.getString("CLAIM_STATUS_CODE"));
		claimStatusBean.setDescription(
				referenceDataCache.populateDescFromCode(claimStatusBean.getCode(), RefTypeEnum.CLAIM_STATUS));
		claim.setClaimStatus(claimStatusBean);
		PaymentMethodBean paymentMethodBean = new PaymentMethodBean();
		paymentMethodBean.setCode(rs.getString("PAYMENT_METHOD_CODE"));
		paymentMethodBean.setDescription(
				referenceDataCache.populateDescFromCode(paymentMethodBean.getCode(), RefTypeEnum.PAYMENT_METHOD));
		claim.setPaymentMethod(paymentMethodBean);
		claim.setUpdateReason(rs.getString("REASON_FOR_EDIT"));
		claim.setNotificationID(rs.getString("NOTIFICATION_ID"));
		claim.setClaimCategoryCode(rs.getString("CLAIM_CATEGORY_CODE"));
		claim.setClaimReasonCode(rs.getString("CLAIM_REASON_CODES"));
		return claim;
	}
}
