package za.co.oldmutual.asisa.refdata;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.refdata.bean.ClaimCategoryBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimCauseBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimStatusBean;
import za.co.oldmutual.asisa.refdata.bean.ClaimTypeBean;
import za.co.oldmutual.asisa.refdata.bean.GenderBean;
import za.co.oldmutual.asisa.refdata.bean.IdentityTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ImpairmentCodeBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSpecBean;
import za.co.oldmutual.asisa.refdata.bean.LifeSymbolBean;
import za.co.oldmutual.asisa.refdata.bean.NotificationStatusBean;
import za.co.oldmutual.asisa.refdata.bean.NotificationTypeBean;
import za.co.oldmutual.asisa.refdata.bean.OfficeBean;
import za.co.oldmutual.asisa.refdata.bean.PaymentMethodBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;
import za.co.oldmutual.asisa.refdata.bean.ReadingsCriteriaBean;
import za.co.oldmutual.asisa.refdata.bean.TitleBean;

@Component
public class ReferenceDataCache implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataCache.class);

	private static Map<RefTypeEnum, Map<String, String>> refDataMap = new EnumMap<>(RefTypeEnum.class);

	@Autowired
	RefDataDAO refDataDAO;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		LOGGER.info("Reference Data Cache Initiating...");
		refDataMap.put(RefTypeEnum.GENDER, refDataDAO.getGenders().stream()
				.collect(Collectors.toMap(GenderBean::getCode, GenderBean::getDescription)));
		refDataMap.put(RefTypeEnum.TITLE, refDataDAO.getTitles().stream()
				.collect(Collectors.toMap(TitleBean::getCode, TitleBean::getDescription)));
		refDataMap.put(RefTypeEnum.IDTYPE, refDataDAO.getIdentityTypes().stream()
				.collect(Collectors.toMap(IdentityTypeBean::getCode, IdentityTypeBean::getDescription)));
		refDataMap.put(RefTypeEnum.LIFE_SYMBOL, refDataDAO.getLifeSymbols().stream()
				.collect(Collectors.toMap(LifeSymbolBean::getCode, LifeSymbolBean::getDescription)));
		refDataMap.put(RefTypeEnum.LIFE_SPEC, refDataDAO.getLifeSpecs().stream()
				.collect(Collectors.toMap(LifeSpecBean::getCode, LifeSpecBean::getDescription)));
		refDataMap.put(RefTypeEnum.POLICY_TYPE, refDataDAO.getPolicyTypes().stream()
				.collect(Collectors.toMap(PolicyTypeBean::getCode, PolicyTypeBean::getDescription)));
		refDataMap.put(RefTypeEnum.PAYMENT_METHOD, refDataDAO.getPaymentMethods().stream()
				.collect(Collectors.toMap(PaymentMethodBean::getCode, PaymentMethodBean::getDescription)));
		refDataMap.put(RefTypeEnum.IMPAIRMENT_CODE, refDataDAO.getImpairmentCodes().stream()
				.collect(Collectors.toMap(ImpairmentCodeBean::getCode, ImpairmentCodeBean::getDescription)));
		refDataMap.put(RefTypeEnum.CLAIM_TYPE, refDataDAO.getClaimTypes().stream()
				.collect(Collectors.toMap(ClaimTypeBean::getCode, ClaimTypeBean::getDescription)));
		refDataMap.put(RefTypeEnum.CLAIM_CAUSE, refDataDAO.getClaimCauses().stream()
				.collect(Collectors.toMap(ClaimCauseBean::getCode, ClaimCauseBean::getDescription)));
		refDataMap.put(RefTypeEnum.CLAIM_STATUS, refDataDAO.getClaimStatuses().stream()
				.collect(Collectors.toMap(ClaimStatusBean::getCode, ClaimStatusBean::getDescription)));
		refDataMap.put(RefTypeEnum.CLAIM_CATEGORY, refDataDAO.getClaimCategories().stream()
				.collect(Collectors.toMap(ClaimCategoryBean::getCode, ClaimCategoryBean::getDescription)));
		refDataMap.put(RefTypeEnum.NOTIFICATION_TYPE, refDataDAO.getNotificationTypes().stream()
				.collect(Collectors.toMap(NotificationTypeBean::getCode, NotificationTypeBean::getDescription)));
		refDataMap.put(RefTypeEnum.NOTIFICATION_STATUS, refDataDAO.getNotificationStatuses().stream()
				.collect(Collectors.toMap(NotificationStatusBean::getCode, NotificationStatusBean::getDescription)));
		refDataMap.put(RefTypeEnum.OFFICE, refDataDAO.getOffices().stream()
				.collect(Collectors.toMap(OfficeBean::getId, OfficeBean::getName)));
		refDataMap.put(RefTypeEnum.READINGS, refDataDAO.getReadingsCriteria().stream()
				.collect(Collectors.toMap(ReadingsCriteriaBean::getImpairmentCode, ReadingsCriteriaBean::getFormat)));
		LOGGER.info("Reference Data Cache initialized successfully");
	}

	public String populateDescFromCode(String code, RefTypeEnum refType) {
		return refDataMap.get(refType).get(code);
	}

	public boolean isCodeValid(String code, RefTypeEnum refType) {
		boolean isCodeValid = false;
		if (StringUtils.hasText(refDataMap.get(refType).get(code))) {
			isCodeValid = true;
		}
		return isCodeValid;
	}
}
