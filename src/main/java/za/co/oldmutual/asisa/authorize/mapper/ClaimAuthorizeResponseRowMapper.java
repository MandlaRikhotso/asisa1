package za.co.oldmutual.asisa.authorize.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import za.co.oldmutual.asisa.authorize.ClaimAuthorizeResponse;
import za.co.oldmutual.asisa.common.bean.DataBaseColumnEnum;
import za.co.oldmutual.asisa.common.validation.HasColumnIdentifier;
import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

@Component
public class ClaimAuthorizeResponseRowMapper implements RowMapper<ClaimAuthorizeResponse> {

	@Autowired
	private ReferenceDataCache referenceDataCache;

	@Autowired
	HasColumnIdentifier column;

	@Override
	public ClaimAuthorizeResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClaimAuthorizeResponse claimAuthorizeResponse = new ClaimAuthorizeResponse();
		ResultSetMetaData metaData = rs.getMetaData();

		setNotificationID(rs, claimAuthorizeResponse, metaData);

		setCreatedBy(rs, claimAuthorizeResponse, metaData);

		setLifeOffice(rs, claimAuthorizeResponse, metaData);

		setCreatedDate(rs, claimAuthorizeResponse, metaData);

		setEventDate(rs, claimAuthorizeResponse, metaData);

		setPlaceOfDeath(rs, claimAuthorizeResponse, metaData);

		setDeathCertificateNumber(rs, claimAuthorizeResponse, metaData);

		setDha1663Number(rs, claimAuthorizeResponse, metaData);

		setPolicyNumber(rs, claimAuthorizeResponse, metaData);

		setUpdateReason(rs, claimAuthorizeResponse, metaData);

		setPolicyBenefit(rs, claimAuthorizeResponse, metaData);

		setCauseOfEvent(rs, claimAuthorizeResponse, metaData);

		setTransType(rs, claimAuthorizeResponse, metaData);

		setIdentityNumber(rs, claimAuthorizeResponse, metaData);

		setIdentityType(rs, claimAuthorizeResponse, metaData);
		
		setClaimReason(rs, claimAuthorizeResponse, metaData);

		setClaimStatus(rs, claimAuthorizeResponse, metaData);
		
		setClaimCategory(rs, claimAuthorizeResponse, metaData);
		
		setPaymentMethod(rs, claimAuthorizeResponse, metaData);
		
		setAstuteRefNo(rs, claimAuthorizeResponse, metaData);

		setIsActive(rs, claimAuthorizeResponse, metaData);

		return claimAuthorizeResponse;
	}

	private void setIsActive(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "IS_ACTIVE")) {
			claimAuthorizeResponse.setIsActive(rs.getString("IS_ACTIVE"));
		}
	}

	private void setAstuteRefNo(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "ASTUTE_REF_NO")) {
			claimAuthorizeResponse.setAstuteRefNo(rs.getString("ASTUTE_REF_NO"));
		}
	}

	private void setPaymentMethod(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "PAYMENT_METHOD_CODE")) {
			PaymentMethodBean paymentMethodBean = new PaymentMethodBean();
			paymentMethodBean.setCode(rs.getString("PAYMENT_METHOD_CODE"));
			paymentMethodBean.setDescription(
					referenceDataCache.populateDescFromCode(paymentMethodBean.getCode(), RefTypeEnum.PAYMENT_METHOD));
			claimAuthorizeResponse.setPaymentMethod(paymentMethodBean);
		}
	}

	private void setClaimCategory(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "CLAIM_CATEGORY_CODE")) {
			claimAuthorizeResponse.setClaimCategoryCode(rs.getString(DataBaseColumnEnum.CLAIM_CATEGORY_CODE.toString()));

			ClaimCategoryBean claimCategoryBean = new ClaimCategoryBean();
			claimCategoryBean.setCode(rs.getString(DataBaseColumnEnum.CLAIM_CATEGORY_CODE.toString()));
			claimCategoryBean.setDescription(
					referenceDataCache.populateDescFromCode(claimCategoryBean.getCode(), RefTypeEnum.CLAIM_CATEGORY));
			claimAuthorizeResponse.setClaimCategory(claimCategoryBean);

		}
	}

	private void setClaimStatus(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "CLAIM_STATUS_CODE")) {
			if(StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.CLAIM_STATUS_CODE.toString()))) {
				ClaimStatusBean claimStatus = new ClaimStatusBean();
				claimStatus.setCode("");
				claimStatus.setDescription("");
				claimAuthorizeResponse.setClaimStatus(claimStatus);
			}
			else {
			ClaimStatusBean claimStatus = new ClaimStatusBean();
			claimStatus.setCode(rs.getString(DataBaseColumnEnum.CLAIM_STATUS_CODE.toString()));
			claimStatus.setDescription(
					referenceDataCache.populateDescFromCode(claimStatus.getCode(), RefTypeEnum.CLAIM_STATUS));
			claimAuthorizeResponse.setClaimStatus(claimStatus);
			}
		}
	}

	private void setClaimReason(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, DataBaseColumnEnum.CLAIM_REASON_CODES.toString())) {
			if (StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.CLAIM_REASON_CODES.toString()))) {
				ImpairmentCodeBean impairmentCode = new ImpairmentCodeBean();
				impairmentCode.setCode("");
				impairmentCode.setDescription("");
				List<ImpairmentCodeBean> impairmentCodes = new ArrayList<>();
				impairmentCodes.add(impairmentCode);
				claimAuthorizeResponse.setClaimReason(impairmentCodes);
				claimAuthorizeResponse.setClaimReasonCode("");
			} else {
				ImpairmentCodeBean impairmentCode = new ImpairmentCodeBean();
				impairmentCode.setCode(rs.getString(DataBaseColumnEnum.CLAIM_REASON_CODES.toString()));
				impairmentCode.setDescription(
						referenceDataCache.populateDescFromCode(impairmentCode.getCode(), RefTypeEnum.IMPAIRMENT_CODE));
				List<ImpairmentCodeBean> impairmentCodes = new ArrayList<>();
				impairmentCodes.add(impairmentCode);
				claimAuthorizeResponse.setClaimReason(impairmentCodes);
				claimAuthorizeResponse
						.setClaimReasonCode(rs.getString(DataBaseColumnEnum.CLAIM_REASON_CODES.toString()));
			}
		}
	}

	private void setIdentityType(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "IDENTITY_NUMBER_TYPE_CODE")) {
			IdentityTypeBean identityTypeBean = new IdentityTypeBean();
			identityTypeBean.setCode(rs.getString("IDENTITY_NUMBER_TYPE_CODE"));
			identityTypeBean.setDescription(
					referenceDataCache.populateDescFromCode(identityTypeBean.getCode(), RefTypeEnum.IDTYPE));
			claimAuthorizeResponse.setIdentityType(identityTypeBean);
		}
	}

	private void setIdentityNumber(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "IDENTITY_NUMBER")) {
			claimAuthorizeResponse.setIdentityNumber(rs.getString("IDENTITY_NUMBER"));
		}
	}

	private void setTransType(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "NOTIFICATION_TXN_TYPE")) {
			claimAuthorizeResponse.setTransType(rs.getString("NOTIFICATION_TXN_TYPE"));
		}
	}

	private void setCauseOfEvent(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "EVENT_CAUSE_CODE")) {
			if (StringUtils.isEmpty(rs.getString(DataBaseColumnEnum.EVENT_CAUSE_CODE.toString()))) {
				ClaimCauseBean claimCause = new ClaimCauseBean();
				claimCause.setCode("");
				claimCause.setDescription("");
				claimAuthorizeResponse.setCauseOfEvent(claimCause);
			}else {
			ClaimCauseBean claimCause = new ClaimCauseBean();
			claimCause.setCode(rs.getString(DataBaseColumnEnum.EVENT_CAUSE_CODE.toString()));
			claimCause.setDescription(
					referenceDataCache.populateDescFromCode(claimCause.getCode(), RefTypeEnum.CLAIM_CAUSE));
			claimAuthorizeResponse.setCauseOfEvent(claimCause);
			}
		}
	}

	private void setPolicyBenefit(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "POLICY_TYPE_CODE")) {
			PolicyTypeBean policyBenefit = new PolicyTypeBean();
			policyBenefit.setCode(rs.getString("POLICY_TYPE_CODE"));
			policyBenefit.setDescription(
					referenceDataCache.populateDescFromCode(policyBenefit.getCode(), RefTypeEnum.POLICY_TYPE));
			claimAuthorizeResponse.setPolicyBenefit(policyBenefit);
		}
	}

	private void setUpdateReason(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "REASON_FOR_EDIT")) {
			claimAuthorizeResponse.setUpdateReason(rs.getString("REASON_FOR_EDIT"));
		}
	}

	private void setPolicyNumber(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "POLICY_REF_NUMBER")) {
			claimAuthorizeResponse.setPolicyNumber(rs.getString("POLICY_REF_NUMBER"));
		}
	}

	private void setDha1663Number(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "DHA1663_NUMBER")) {

			claimAuthorizeResponse
					.setDha1663Number(rs.getString(DataBaseColumnEnum.DHA1663_NUMBER.toString()) != null ? rs.getString(DataBaseColumnEnum.DHA1663_NUMBER.toString()) : "");
		}
	}

	private void setDeathCertificateNumber(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "EVENT_DEATH_CERTIFICATE_NO")) {

			claimAuthorizeResponse.setDeathCertificateNumber(
					rs.getString(DataBaseColumnEnum.EVENT_DEATH_CERTIFICATE_NO.toString()) != null ? rs.getString(DataBaseColumnEnum.EVENT_DEATH_CERTIFICATE_NO.toString())
							: "");
		}
	}

	private void setPlaceOfDeath(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "EVENT_DEATH_PLACE")) {

			claimAuthorizeResponse.setPlaceOfDeath(
					rs.getString(DataBaseColumnEnum.EVENT_DEATH_PLACE.toString()) != null ? rs.getString(DataBaseColumnEnum.EVENT_DEATH_PLACE.toString()) : "");
		}
	}

	private void setEventDate(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "EVENT_DATE")) {

			claimAuthorizeResponse
					.setEventDate(rs.getDate(DataBaseColumnEnum.EVENT_DATE.toString()) != null ? rs.getDate(DataBaseColumnEnum.EVENT_DATE.toString()).toString() : "");
		}
	}

	private void setCreatedDate(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "CREATED_DATE")) {
			claimAuthorizeResponse.setCreatedDate(rs.getDate("CREATED_DATE").toString());
		}
	}

	private void setLifeOffice(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "NOTIFICATION_SOURCE")) {
			claimAuthorizeResponse.setLifeOffice(rs.getString("NOTIFICATION_SOURCE"));
		}
	}

	private void setCreatedBy(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse, ResultSetMetaData metaData)
			throws SQLException {
		if (column.hasColumn(metaData, "CREATED_BY")) {
			claimAuthorizeResponse.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
		}
	}

	private void setNotificationID(ResultSet rs, ClaimAuthorizeResponse claimAuthorizeResponse,
			ResultSetMetaData metaData) throws SQLException {
		if (column.hasColumn(metaData, "NOTIFICATION_ID")) {
			claimAuthorizeResponse.setNotificationID(rs.getString("NOTIFICATION_ID"));
		}
	}

}
