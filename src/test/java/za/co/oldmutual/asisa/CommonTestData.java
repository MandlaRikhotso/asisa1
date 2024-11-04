package za.co.oldmutual.asisa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import za.co.oldmutual.asisa.authorize.AuthorizeRequest;
import za.co.oldmutual.asisa.authorize.ClaimAuthorizeResponse;
import za.co.oldmutual.asisa.authorize.ImpairmentAuthorizeResponse;
import za.co.oldmutual.asisa.claims.ClaimNotificationBean;
import za.co.oldmutual.asisa.claims.NotifiableClaimAuditBean;
import za.co.oldmutual.asisa.claims.NotifiableClaimBean;
import za.co.oldmutual.asisa.claims.NotifiableClaimsDAO;
import za.co.oldmutual.asisa.claims.UpdateNotifiableClaimRequest;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.impairments.ImpairmentNotificationBean;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentAuditBean;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentBean;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsRequest;
import za.co.oldmutual.asisa.impairments.UpdateNotifiableImpairmentRequest;
import za.co.oldmutual.asisa.iphistory.IPClaimHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPImpairmentHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPNoteHistoryBean;
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

public class CommonTestData {

	static PolicyTypeBean policyBenefit = new PolicyTypeBean();
	static PolicyTypeBean polBenefit = new PolicyTypeBean();
	static PolicyTypeBean policBenefit = new PolicyTypeBean();
	static ClaimCauseBean claimCause = new ClaimCauseBean();
	static ImpairmentCodeBean impairmentCode = new ImpairmentCodeBean();
	static ClaimStatusBean claimStatus = new ClaimStatusBean();
	static PaymentMethodBean paymentMethod = new PaymentMethodBean();
	static ClaimCategoryBean claimCategory = new ClaimCategoryBean();
	static ClaimTypeBean claimType = new ClaimTypeBean();
	static IdentityTypeBean identityTypeBean = new IdentityTypeBean();
	static PolicyTypeBean policyBenefitBean = new PolicyTypeBean();
	static LifeSpecBean specialInvestigation = new LifeSpecBean();
	static LifeSymbolBean symbolBean = new LifeSymbolBean();
	final static String NOTIFICATION_ID = "412b0fc5-8b22-4426-a93b-4a180cc5d969";
	final static TitleBean titleBean = new TitleBean();
	final static GenderBean genderTypeBean = new GenderBean();
	final static NotificationTypeBean notificationTypeBean = new NotificationTypeBean();
	final static NotificationStatusBean notificationStatusBean = new NotificationStatusBean();
	final static ReadingsCriteriaBean readings = new ReadingsCriteriaBean();

	static {
		policyBenefit.setCode("7");
		policyBenefit.setDescription("REPUDIATION");

		polBenefit.setCode("35");
		polBenefit.setDescription("FUNERAL POLICY");

		policBenefit.setCode("12");
		policBenefit.setDescription("DISABILITY INCOME REPLACEMENT");

		claimCause.setCode("16");
		claimCause.setDescription("DROWNING");

		impairmentCode.setCode("LOAAD11");
		impairmentCode.setDescription("CHEST DEFORMITIES (PIGEON/BARREL/FUNNEL");

		claimStatus.setCode("15");
		claimStatus.setDescription("BASIC PAID");

		paymentMethod.setCode("1");
		paymentMethod.setDescription("EFT");

		claimCategory.setCode("1");
		claimCategory.setDescription("testDesc");

		claimType.setCode("1");
		claimType.setDescription("DescForTest");

		identityTypeBean.setCode("1");
		identityTypeBean.setDescription("SA ID");

		policyBenefitBean.setCode("1");
		policyBenefitBean.setDescription("RISK/DEATH BENEFIT");

		specialInvestigation.setCode("W0");
		specialInvestigation.setDescription("W0");

		symbolBean.setCode("N");
		symbolBean.setDescription("N");

		titleBean.setCode("1");
		titleBean.setDescription("Dr");

		genderTypeBean.setCode("1");
		genderTypeBean.setDescription("Male");

		notificationTypeBean.setCode("2");
		notificationTypeBean.setDescription("testDescription");

		notificationStatusBean.setCode("1");
		notificationStatusBean.setDescription("description");
	}

	public static InsuredPersonBean getDummyInsuredPersonBeanResponse() {
		InsuredPersonBean insuredPersonBean = new InsuredPersonBean();

		insuredPersonBean.setPersonID("1");
		insuredPersonBean.setIdentityNumber("1234567890");
		insuredPersonBean.setDateOfBirth("12/12/2009");
		insuredPersonBean.setSurname("Kumar");
		insuredPersonBean.setGivenName1("Ram");
		insuredPersonBean.setGivenName2("");
		insuredPersonBean.setGivenName3("");
		insuredPersonBean.setAddressLine1("");
		insuredPersonBean.setAddressLine2("");
		insuredPersonBean.setAddressLine3("");
		insuredPersonBean.setPostalCode(0);
		insuredPersonBean.setTitle(titleBean);
		insuredPersonBean.setGender(genderTypeBean);
		insuredPersonBean.setIdentityType(identityTypeBean);
		return insuredPersonBean;
	}

	public static ClaimAuthorizeResponse getDummyClaimAuthorizationResponse() {
		ClaimAuthorizeResponse claimAuthorizeResponse = new ClaimAuthorizeResponse();
		claimAuthorizeResponse.setTransType("UPDATE");
		claimAuthorizeResponse.setIdentityNumber("123456789");

		claimAuthorizeResponse.setNotificationID("12345".toUpperCase());
		claimAuthorizeResponse.setPolicyNumber("12345");

		claimAuthorizeResponse.setPolicyBenefit(policyBenefit);
		List<ImpairmentCodeBean> impairmentCodes = new ArrayList<>();
		impairmentCodes.add(impairmentCode);
		claimAuthorizeResponse.setClaimReason(impairmentCodes);
		claimAuthorizeResponse.setDha1663Number("1234");

		claimAuthorizeResponse.setClaimStatus(claimStatus);

		claimAuthorizeResponse.setCauseOfEvent(claimCause);
		return claimAuthorizeResponse;
	}

	public static ClaimAuthorizeResponse getDummyClaimAuthorizationResponseDetail() {
		ClaimAuthorizeResponse claimAuthorizeResponse = new ClaimAuthorizeResponse();
		claimAuthorizeResponse.setNotificationID("12345".toUpperCase());
		claimAuthorizeResponse.setCreatedBy("XY57850");
		claimAuthorizeResponse.setLifeOffice("TestOffice");
		claimAuthorizeResponse.setCreatedDate("16/06/1988");
		claimAuthorizeResponse.setEventDate("16/06/1988");
		claimAuthorizeResponse.setPlaceOfDeath("Capetown");
		claimAuthorizeResponse.setDeathCertificateNumber("12345");
		claimAuthorizeResponse.setDha1663Number("b123");
		claimAuthorizeResponse.setPolicyNumber("123");
		claimAuthorizeResponse.setUpdateReason("");
		claimAuthorizeResponse.setPolicyBenefit(policyBenefit);
		claimAuthorizeResponse.setCauseOfEvent(claimCause);
		return claimAuthorizeResponse;
	}

	public static NotifiableClaimAuditBean getDummyNotifiableClaimAuditBean() {

		NotifiableClaimAuditBean dummyNotifiableClaimAuditBean = new NotifiableClaimAuditBean();
		dummyNotifiableClaimAuditBean.setNotificationID("123".toUpperCase());
		dummyNotifiableClaimAuditBean.setCreatedDate(new Date());
		dummyNotifiableClaimAuditBean.setEventDate(NotifiableClaimsDAO.formatDate(new Date()));
		dummyNotifiableClaimAuditBean.setEventCause(claimCause);
		dummyNotifiableClaimAuditBean.setEventDeathPlace("Capetown");
		dummyNotifiableClaimAuditBean.setEventDeathCertificateNo("456");
		dummyNotifiableClaimAuditBean.setDha1663Number("123");
		List<ImpairmentCodeBean> impairmentCodes = new ArrayList<>();
		impairmentCodes.add(impairmentCode);
		dummyNotifiableClaimAuditBean.setClaimReason(impairmentCodes);
		dummyNotifiableClaimAuditBean.setClaimStatus(claimStatus);
		dummyNotifiableClaimAuditBean.setIsActive("Y");
		dummyNotifiableClaimAuditBean.setNotificationTxnType("UPDATE");
		dummyNotifiableClaimAuditBean.setPaymentMethod(paymentMethod);
		dummyNotifiableClaimAuditBean.setClaimCategory(claimCategory);
		return dummyNotifiableClaimAuditBean;
	}

	public static AuthorizeRequest getDummyAuthorizeRequestForUpdate() {
		AuthorizeRequest authorizeClaimRequest = new AuthorizeRequest();
		authorizeClaimRequest.setNotificationID("412b0fc5-8b22-4426-a93b-4a180cc5d969".toUpperCase());
		authorizeClaimRequest.setTransType("UPDATE");
		return authorizeClaimRequest;
	}

	public static AuthorizeRequest getDummyAuthorizeRequestForDelete() {
		AuthorizeRequest authorizeClaimRequest = new AuthorizeRequest();
		authorizeClaimRequest.setNotificationID("412b0fc5-8b22-4426-a93b-4a180cc5d969".toUpperCase());
		authorizeClaimRequest.setTransType("DELETE");
		return authorizeClaimRequest;
	}

	public static ImpairmentAuthorizeResponse getDummyAuthorizationResponse() {
		ImpairmentAuthorizeResponse impairmentAuthorizeResponse = new ImpairmentAuthorizeResponse();
		impairmentAuthorizeResponse.setIdentityNumber("1234567890");
		impairmentAuthorizeResponse.setIdentityType(identityTypeBean);
		impairmentAuthorizeResponse.setImpairment(impairmentCode);
		impairmentAuthorizeResponse.setNotificationID("3c65e70b-bba3-4f2a-9a41-2fc13edd05af".toUpperCase());
		impairmentAuthorizeResponse.setPolicyNumber("123456");
		impairmentAuthorizeResponse.setReadings("123-908");
		impairmentAuthorizeResponse.setTimeSignal("2000");
		impairmentAuthorizeResponse.setTransType("UPDATE");
		impairmentAuthorizeResponse.setNotificationStatus("APPROVAL_PENDING");
		return impairmentAuthorizeResponse;
	}

	public static ImpairmentAuthorizeResponse getDummyImpairmentAuthorizeDetailResponse() {

		ImpairmentAuthorizeResponse impairmentAuthorizeDetailResponse = new ImpairmentAuthorizeResponse();

		impairmentAuthorizeDetailResponse.setImpairment(impairmentCode);
		impairmentAuthorizeDetailResponse.setLifeOffice("APPROVAL_PENDING");
		impairmentAuthorizeDetailResponse.setCreatedBy("XY57183");
		impairmentAuthorizeDetailResponse.setCreatedDate("12/09/2009");

		impairmentAuthorizeDetailResponse.setPolicyBenefit(policyBenefitBean);
		impairmentAuthorizeDetailResponse.setPolicyNumber("123456");
		impairmentAuthorizeDetailResponse.setReadings("12-98");

		impairmentAuthorizeDetailResponse.setTimeSignal("2000");
		List<LifeSpecBean> specialInvestigations = new ArrayList<>();
		specialInvestigations.add(specialInvestigation);
		impairmentAuthorizeDetailResponse.setSpecialInvestigation(specialInvestigations);

		List<LifeSymbolBean> symbols = new ArrayList<>();
		symbols.add(symbolBean);

		impairmentAuthorizeDetailResponse.setSymbol(symbols);
		impairmentAuthorizeDetailResponse.setNotificationID("3c65e70b-bba3-4f2a-9a41-2fc13edd05af".toUpperCase());
		impairmentAuthorizeDetailResponse.setReasonForEdit("REASON_FOR_EDIT");
		return impairmentAuthorizeDetailResponse;
	}

	public static ImpairmentAuthorizeResponse getDummyNotifiableImpairmentForAuditBean() {
		ImpairmentAuthorizeResponse notifiableImpairmentAuditBean = new ImpairmentAuthorizeResponse();
		notifiableImpairmentAuditBean.setTransType("UPDATE");
		notifiableImpairmentAuditBean.setCreatedBy("XY57183");
		notifiableImpairmentAuditBean.setNotificationID(NOTIFICATION_ID.toUpperCase());
		notifiableImpairmentAuditBean.setTimeSignal("2000");
		notifiableImpairmentAuditBean.setReadings("120-19");
		notifiableImpairmentAuditBean.setReasonForEdit("Updating");
		notifiableImpairmentAuditBean.setImpairment(impairmentCode);
		List<LifeSpecBean> specialInvestigations = new ArrayList<>();
		specialInvestigations.add(specialInvestigation);
		notifiableImpairmentAuditBean.setSpecialInvestigation(specialInvestigations);

		List<LifeSymbolBean> symbols = new ArrayList<>();
		symbols.add(symbolBean);
		notifiableImpairmentAuditBean.setSymbol(symbols);
		return notifiableImpairmentAuditBean;
	}

	public static InsuredPersonBean getDummyInsuredPersonBeanRequest() {
		InsuredPersonBean insuredPersonBean = new InsuredPersonBean();

		insuredPersonBean.setAddressLine1("addressLine1");
		insuredPersonBean.setGivenName1("givenName1");
		insuredPersonBean.setIdentityNumber("123450000");
		insuredPersonBean.setIdentityType(identityTypeBean);
		insuredPersonBean.setDateOfBirth("18/01/2019");
		insuredPersonBean.setGender(genderTypeBean);
		insuredPersonBean.setTitle(titleBean);
		insuredPersonBean.setSurname("Surname");
		return insuredPersonBean;

	}

	public static ClaimNotificationBean getDummyClaimNotificationBeanRequest() {
		ClaimNotificationBean claimNotificationBean = new ClaimNotificationBean();

		claimNotificationBean.setPolicyBenefit(policyBenefitBean);

		claimNotificationBean.setPolicyNumber("10");
		List<NoteBean> notesList = new ArrayList<>();
		NoteBean noteBean = new NoteBean();
		noteBean.setScratchpad("scratchpad");
		noteBean.setNoteText("Note Text");
		notesList.add(noteBean);
		claimNotificationBean.setNotes(notesList);

		NotifiableClaimBean notifiableClaimBean = CommonTestData.getDummyNotifiableClaimBeanRequest();
		List<NotifiableClaimBean> notifiableClaimBeanList = new ArrayList<>();
		notifiableClaimBeanList.add(notifiableClaimBean);

		claimNotificationBean.setNotifiableClaims(notifiableClaimBeanList);
		List<ClaimNotificationBean> notificationBeanList = new ArrayList<>();
		notificationBeanList.add(claimNotificationBean);
		return claimNotificationBean;
	}

	public static ClaimNotificationBean getDummyClaimNotificationBeanRequestForDeathValidation() {
		ClaimNotificationBean claimNotificationBean = new ClaimNotificationBean();

		claimNotificationBean.setPolicyBenefit(polBenefit);

		claimNotificationBean.setPolicyNumber("10");
		List<NoteBean> notesList = new ArrayList<>();
		NoteBean noteBean = new NoteBean();
		noteBean.setScratchpad("scratchpad");
		noteBean.setNoteText("Note Text");
		notesList.add(noteBean);
		claimNotificationBean.setNotes(notesList);

		NotifiableClaimBean notifiableClaimBean = CommonTestData.getDummyNotifiableClaimBeanRequestForDeathValidation();
		List<NotifiableClaimBean> notifiableClaimBeanList = new ArrayList<>();
		notifiableClaimBeanList.add(notifiableClaimBean);

		claimNotificationBean.setNotifiableClaims(notifiableClaimBeanList);
		List<ClaimNotificationBean> notificationBeanList = new ArrayList<>();
		notificationBeanList.add(claimNotificationBean);
		return claimNotificationBean;
	}

	public static ClaimNotificationBean getDummyClaimNotificationBeanRequestForMandatoryValidation() {
		ClaimNotificationBean claimNotificationBean = new ClaimNotificationBean();

		claimNotificationBean.setPolicyBenefit(policBenefit);

		claimNotificationBean.setPolicyNumber("10");
		List<NoteBean> notesList = new ArrayList<>();
		NoteBean noteBean = new NoteBean();
		noteBean.setScratchpad("scratchpad");
		noteBean.setNoteText("Note Text");
		notesList.add(noteBean);
		claimNotificationBean.setNotes(notesList);

		NotifiableClaimBean notifiableClaimBean = CommonTestData
				.getDummyNotifiableClaimBeanRequestForMandatoryValidation();
		List<NotifiableClaimBean> notifiableClaimBeanList = new ArrayList<>();
		notifiableClaimBeanList.add(notifiableClaimBean);

		claimNotificationBean.setNotifiableClaims(notifiableClaimBeanList);
		List<ClaimNotificationBean> notificationBeanList = new ArrayList<>();
		notificationBeanList.add(claimNotificationBean);
		return claimNotificationBean;
	}

	public static ClaimNotificationBean getDummyClaimNotificationBeanRequestForScratchpad() {
		ClaimNotificationBean claimNotificationBean = new ClaimNotificationBean();
		policyBenefit.setCode("");
		policyBenefit.setDescription("");
		claimNotificationBean.setPolicyBenefit(policyBenefit);

		claimNotificationBean.setPolicyNumber("");
		List<NoteBean> notesList = new ArrayList<>();
		NoteBean noteBean = new NoteBean();
		noteBean.setScratchpad("scratchpad");
		noteBean.setNoteText("");
		notesList.add(noteBean);
		claimNotificationBean.setNotes(notesList);

		NotifiableClaimBean notifiableClaimBean = null;
		List<NotifiableClaimBean> notifiableClaimBeanList = new ArrayList<>();

		claimNotificationBean.setNotifiableClaims(notifiableClaimBeanList);
		List<ClaimNotificationBean> notificationBeanList = new ArrayList<>();
		notificationBeanList.add(claimNotificationBean);
		return claimNotificationBean;
	}

	public static NotifiableClaimBean getDummyNotifiableClaimBeanRequest() {
		NotifiableClaimBean notifiableClaimBean = new NotifiableClaimBean();
		notifiableClaimBean.setDha1663Number("7890");

		notifiableClaimBean.setClaimCategory(claimCategory);

		List<ImpairmentCodeBean> claimReasonCodeList = new ArrayList<>();
		claimReasonCodeList.add(impairmentCode);
		notifiableClaimBean.setClaimReason(claimReasonCodeList);

		notifiableClaimBean.setClaimStatus(claimStatus);

		notifiableClaimBean.setEventCause(claimCause);

		notifiableClaimBean.setEventDate("2019-07-09");
		notifiableClaimBean.setEventDeathCertificateNo("1234567");
		notifiableClaimBean.setEventDeathPlace("Pune");

		notifiableClaimBean.setPaymentMethod(paymentMethod);
		notifiableClaimBean.setUpdateReason("Updating Pay mode");

		return notifiableClaimBean;
	}

	public static NotifiableClaimBean getDummyNotifiableClaimBeanRequestForDeathValidation() {
		NotifiableClaimBean notifiableClaimBean = new NotifiableClaimBean();
		notifiableClaimBean.setDha1663Number("7890");

		notifiableClaimBean.setClaimCategory(claimCategory);

		List<ImpairmentCodeBean> claimReasonCodeList = new ArrayList<>();
		claimReasonCodeList.add(impairmentCode);
		notifiableClaimBean.setClaimReason(claimReasonCodeList);

		notifiableClaimBean.setClaimStatus(claimStatus);

		notifiableClaimBean.setEventCause(claimCause);

		notifiableClaimBean.setEventDate("2019-07-09");
		notifiableClaimBean.setEventDeathCertificateNo("1234567");
		notifiableClaimBean.setEventDeathPlace("");

		notifiableClaimBean.setPaymentMethod(paymentMethod);
		notifiableClaimBean.setUpdateReason("Updating Pay mode");

		return notifiableClaimBean;
	}

	public static NotifiableClaimBean getDummyNotifiableClaimBeanRequestForMandatoryValidation() {
		NotifiableClaimBean notifiableClaimBean = new NotifiableClaimBean();
		notifiableClaimBean.setDha1663Number("7890");

		notifiableClaimBean.setClaimCategory(claimCategory);

		List<ImpairmentCodeBean> claimReasonCodeList = new ArrayList<>();
		claimReasonCodeList.add(impairmentCode);
		notifiableClaimBean.setClaimReason(claimReasonCodeList);

		notifiableClaimBean.setClaimStatus(claimStatus);

		notifiableClaimBean.setEventCause(claimCause);

		notifiableClaimBean.setEventDate("");
		notifiableClaimBean.setEventDeathCertificateNo("1234567");
		notifiableClaimBean.setEventDeathPlace("Pune");

		notifiableClaimBean.setPaymentMethod(paymentMethod);
		notifiableClaimBean.setUpdateReason("Updating Pay mode");

		return notifiableClaimBean;
	}

	public static NotifiableClaimBean getDummyNotifiableClaimBeanRequestForScratchpad() {
		NotifiableClaimBean notifiableClaimBean = new NotifiableClaimBean();
		claimCategory.setCode("");
		claimCategory.setDescription("");
		notifiableClaimBean.setDha1663Number("");
		notifiableClaimBean.setClaimCategory(claimCategory);

		List<ImpairmentCodeBean> claimReasonCodeList = new ArrayList<>();
		impairmentCode.setCode("");
		impairmentCode.setDescription("");
		claimReasonCodeList.add(impairmentCode);
		notifiableClaimBean.setClaimReason(claimReasonCodeList);

		claimStatus.setCode("");
		claimStatus.setDescription("");
		notifiableClaimBean.setClaimStatus(claimStatus);

		claimCause.setCode("");
		claimCause.setDescription("");
		notifiableClaimBean.setEventCause(claimCause);

		notifiableClaimBean.setEventDate("");
		notifiableClaimBean.setEventDeathCertificateNo("");
		notifiableClaimBean.setEventDeathPlace("");

		paymentMethod.setCode("");
		paymentMethod.setDescription("");
		notifiableClaimBean.setPaymentMethod(paymentMethod);
		notifiableClaimBean.setUpdateReason("");

		return notifiableClaimBean;
	}

	public static UpdateNotifiableClaimRequest getDummyUpdateClaimDetailRequest() {

		UpdateNotifiableClaimRequest updateNotifiableClaimRequest = new UpdateNotifiableClaimRequest();

		updateNotifiableClaimRequest.setDha1663Number("7890");

		updateNotifiableClaimRequest.setClaimCategory(claimCategory);

		List<ImpairmentCodeBean> claimReasonCodeList = new ArrayList<>();

		claimReasonCodeList.add(impairmentCode);
		updateNotifiableClaimRequest.setClaimReason(claimReasonCodeList);

		updateNotifiableClaimRequest.setClaimStatus(claimStatus);

		updateNotifiableClaimRequest.setDelete(false);

		updateNotifiableClaimRequest.setEventCause(claimCause);

		updateNotifiableClaimRequest.setEventDate("18/01/2019");
		updateNotifiableClaimRequest.setEventDeathCertificateNo("1234567");
		updateNotifiableClaimRequest.setEventDeathPlace("Pune");

		List<NoteBean> NoteBeanList = new ArrayList<>();
		NoteBean note = new NoteBean();
		note.setNoteText("noteText");
		note.setScratchpad("Y");
		NoteBeanList.add(note);
		updateNotifiableClaimRequest.setNotes(NoteBeanList);
		updateNotifiableClaimRequest.setNotificationID("5a1490ab-6129-4d9f-9752-e8e947c39a3d".toUpperCase());

		List<PaymentMethodBean> paymentMethodBeanList = new ArrayList<>();

		paymentMethodBeanList.add(paymentMethod);
		updateNotifiableClaimRequest.setPaymentMethod(paymentMethod);
		updateNotifiableClaimRequest.setUpdateReason("Updating Pay mode");
		return updateNotifiableClaimRequest;
	}

	public static List<String> getDummyAutosuggestIdentityNumberResponse() {
		List<String> identityNumber = new ArrayList<>();
		identityNumber.add("123456789");
		identityNumber.add("123968574");
		return identityNumber;

	}

	public static List<String> getDummyAutosuggestSurnameResponse() {
		List<String> surname = new ArrayList<>();
		surname.add("Kumar");
		surname.add("Kumari");
		return surname;

	}

	public static List<InsuredPersonBean> getDummyIPEnquiryResponseforPerfectMatch() {
		List<InsuredPersonBean> dummyIPEnquiryResponseforPerfectMatch = new ArrayList<>();
		InsuredPersonBean iPEnquiryResponse = new InsuredPersonBean();
		iPEnquiryResponse.setIdentityNumber("1234567890123");

		iPEnquiryResponse.setIdentityType(identityTypeBean);
		iPEnquiryResponse.setDateOfBirth("12/12/2009");

		iPEnquiryResponse.setGender(genderTypeBean);
		iPEnquiryResponse.setGivenName1("Ram");
		iPEnquiryResponse.setSurname("Kumar");
		iPEnquiryResponse.setPerfectMatch(true);
		dummyIPEnquiryResponseforPerfectMatch.add(iPEnquiryResponse);
		return dummyIPEnquiryResponseforPerfectMatch;

	}

	public static List<InsuredPersonBean> getDummyIPEnquiryResponseForImPerfectMatch() {
		List<InsuredPersonBean> dummyIPEnquiryResponseforIMperfectMatch = new ArrayList<>();
		InsuredPersonBean iPEnquiryResponse1 = new InsuredPersonBean();
		iPEnquiryResponse1.setIdentityNumber("1234567890123");

		iPEnquiryResponse1.setIdentityType(identityTypeBean);
		iPEnquiryResponse1.setDateOfBirth("12/12/2009");

		iPEnquiryResponse1.setGender(genderTypeBean);
		iPEnquiryResponse1.setGivenName1("Ram");
		iPEnquiryResponse1.setSurname("Kumar");
		iPEnquiryResponse1.setPerfectMatch(false);
		dummyIPEnquiryResponseforIMperfectMatch.add(iPEnquiryResponse1);
		return dummyIPEnquiryResponseforIMperfectMatch;
	}

	public static List<IPClaimHistoryBean> getDummyClaimHistoryResponse() {
		IPClaimHistoryBean iPClaimHistoryBean = new IPClaimHistoryBean();
		iPClaimHistoryBean.setNotificationID("1");
		iPClaimHistoryBean.setPolicyNumber("987654");

		iPClaimHistoryBean.setClaimType(policyBenefit);
		iPClaimHistoryBean.setDateCreated("08/02/2019");
		iPClaimHistoryBean.setNotificationSource("OM");
		List<ClaimCategoryBean> claimCategoryList = new ArrayList<>();

		claimCategoryList.add(claimCategory);
		iPClaimHistoryBean.setClaimCategory(claimCategoryList);
		iPClaimHistoryBean.setEventDate("02/02/2019");

		iPClaimHistoryBean.setEventCause(claimCause);
		iPClaimHistoryBean.setEventDeathPlace("CapeTown");
		iPClaimHistoryBean.setEventDeathCertificateNo("85246");
		iPClaimHistoryBean.setDha1663Number("9632587");
		List<ImpairmentCodeBean> impairmentCodeList = new ArrayList<>();

		impairmentCodeList.add(impairmentCode);
		iPClaimHistoryBean.setClaimReason(impairmentCodeList);

		iPClaimHistoryBean.setClaimStatus(claimStatus);

		iPClaimHistoryBean.setPaymentMethod(paymentMethod);
		iPClaimHistoryBean.setUpdateReason("Edit");
		iPClaimHistoryBean.setNotificationStatus("UPDATE");
		iPClaimHistoryBean.setCreatedBy("XY57473");
		iPClaimHistoryBean.setUserRole("UNDERWRITER");
		iPClaimHistoryBean.setBusinessUnit("Personal Finance ");
		iPClaimHistoryBean.setNotificationID("1");
		List<IPClaimHistoryBean> claimHistory = new ArrayList<>();
		claimHistory.add(iPClaimHistoryBean);
		return claimHistory;
	}

	public static List<IPImpairmentHistoryBean> getDummyImpairmentHistoryResponse() {
		IPImpairmentHistoryBean iPImpairmentHistoryBean = new IPImpairmentHistoryBean();
		iPImpairmentHistoryBean.setNotifiableImpairmentID(1);

		iPImpairmentHistoryBean.setImpairment(impairmentCode);

		iPImpairmentHistoryBean.setTimeSignal("2000");
		iPImpairmentHistoryBean.setReadings("45");
		List<LifeSpecBean> specialInvestigationList = new ArrayList<>();

		specialInvestigationList.add(specialInvestigation);
		iPImpairmentHistoryBean.setSpecialInvestigation(specialInvestigationList);
		List<LifeSymbolBean> symbolList = new ArrayList<>();

		symbolList.add(symbolBean);
		iPImpairmentHistoryBean.setSymbol(symbolList);
		iPImpairmentHistoryBean.setUpdateReason("CREATE");
		iPImpairmentHistoryBean.setPolicyNumber("1234567");
		iPImpairmentHistoryBean.setPolicyBenefit(policyBenefitBean);
		iPImpairmentHistoryBean.setDateCreated("02/02/2019");
		iPImpairmentHistoryBean.setNotificationSource("OM");
		iPImpairmentHistoryBean.setNotificationStatus("UPDATE");
		iPImpairmentHistoryBean.setCreatedBy("XY57473");
		iPImpairmentHistoryBean.setUserRole("UNDERWRITER");
		iPImpairmentHistoryBean.setBusinessUnit("Personal Finance ");
		iPImpairmentHistoryBean.setNotificationID("2");
		List<IPImpairmentHistoryBean> impairmentHistory = new ArrayList<>();
		impairmentHistory.add(iPImpairmentHistoryBean);
		return impairmentHistory;
	}

	public static List<IPNoteHistoryBean> getDummyNoteHistoryResponse() {
		IPNoteHistoryBean iPNoteHistoryBean = new IPNoteHistoryBean();
		iPNoteHistoryBean.setNoteId(1);
		iPNoteHistoryBean.setPolicyNumber("1234567");
		iPNoteHistoryBean.setNotificationSource("OM");
		iPNoteHistoryBean.setDateCreated("02/02/2019");
		iPNoteHistoryBean.setCreatedBy("XY57473");
		iPNoteHistoryBean.setOmUserId("XY57473");
		iPNoteHistoryBean.setBusinessUnit("Personal Finance ");
		iPNoteHistoryBean.setNotificationID("3".toUpperCase());
		List<IPNoteHistoryBean> noteHistory = new ArrayList<>();
		noteHistory.add(iPNoteHistoryBean);
		return noteHistory;
	}

	public static NotifiableImpairmentAuditBean getDummyNotifiableImpairmentAuditBean() {
		NotifiableImpairmentAuditBean notifiableImpairmentAuditBean = new NotifiableImpairmentAuditBean();
		notifiableImpairmentAuditBean.setNotificationTxnType("UPDATE");
		notifiableImpairmentAuditBean.setIsActive("Y");
		notifiableImpairmentAuditBean.setCreatedBy("XY57183");
		notifiableImpairmentAuditBean.setCreatedDate(new Date());
		notifiableImpairmentAuditBean.setAstuteRefNo("");
		notifiableImpairmentAuditBean.setNotifiableStatusCode("ACTIVE");
		notifiableImpairmentAuditBean.setNotificationID(NOTIFICATION_ID.toUpperCase());
		notifiableImpairmentAuditBean.setTimeSignal("2000");
		notifiableImpairmentAuditBean.setReadings("120-19");
		notifiableImpairmentAuditBean.setUpdateReason("Updating");
		notifiableImpairmentAuditBean.setImpairment(impairmentCode);
		List<LifeSpecBean> specialInvestigations = new ArrayList<>();
		specialInvestigations.add(specialInvestigation);
		notifiableImpairmentAuditBean.setSpecialInvestigation(specialInvestigations);

		List<LifeSymbolBean> symbols = new ArrayList<>();
		symbols.add(symbolBean);
		notifiableImpairmentAuditBean.setSymbol(symbols);
		return notifiableImpairmentAuditBean;
	}

	public static ImpairmentAuthorizeResponse getDummyImpairmentAuthorizeDetailRequest() {

		ImpairmentAuthorizeResponse impairmentAuthorizeDetailReq = new ImpairmentAuthorizeResponse();
		impairmentAuthorizeDetailReq.setImpairment(impairmentCode);
		impairmentAuthorizeDetailReq.setLifeOffice("OM");
		impairmentAuthorizeDetailReq.setCreatedBy("XY57187");
		impairmentAuthorizeDetailReq.setCreatedDate("12/09/1997");
		impairmentAuthorizeDetailReq.setPolicyBenefit(policyBenefitBean);
		impairmentAuthorizeDetailReq.setPolicyNumber("21321");
		impairmentAuthorizeDetailReq.setReadings("109-90");
		impairmentAuthorizeDetailReq.setTimeSignal("2000");
		List<LifeSpecBean> specialInvestigations = new ArrayList<>();
		specialInvestigations.add(specialInvestigation);
		impairmentAuthorizeDetailReq.setSpecialInvestigation(specialInvestigations);

		List<LifeSymbolBean> symbols = new ArrayList<>();
		symbols.add(symbolBean);
		impairmentAuthorizeDetailReq.setSymbol(symbols);
		impairmentAuthorizeDetailReq.setNotificationID(NOTIFICATION_ID.toUpperCase());
		return impairmentAuthorizeDetailReq;

	}

	public static InsuredPersonBean getDummyInsuredPerson() {
		InsuredPersonBean insuredPersonBean = new InsuredPersonBean();

		insuredPersonBean.setPersonID("1");
		insuredPersonBean.setIdentityNumber("1234567890");
		insuredPersonBean.setDateOfBirth("12/12/2009");
		insuredPersonBean.setSurname("Kumar");
		insuredPersonBean.setGivenName1("Ram");
		insuredPersonBean.setGivenName2("");
		insuredPersonBean.setGivenName3("");
		insuredPersonBean.setAddressLine1("");
		insuredPersonBean.setAddressLine2("");
		insuredPersonBean.setAddressLine3("");
		insuredPersonBean.setPostalCode(0);

		insuredPersonBean.setTitle(titleBean);

		insuredPersonBean.setGender(genderTypeBean);

		insuredPersonBean.setIdentityType(identityTypeBean);
		return insuredPersonBean;
	}

	public static List<NotifiableImpairmentBean> getDummyNotifiableImpairmentBean() {

		List<NotifiableImpairmentBean> notifiableImpairmentBeanList = new ArrayList<>();
		NotifiableImpairmentBean notifiableImpairmentBean = new NotifiableImpairmentBean();

		notifiableImpairmentBean = new NotifiableImpairmentBean();
		notifiableImpairmentBean.setReadings("2018");
		notifiableImpairmentBean.setTimeSignal("20");
		notifiableImpairmentBean.setNotifiableImpairmentID(5);
		notifiableImpairmentBean.setNotificationID(NOTIFICATION_ID.toUpperCase());
		notifiableImpairmentBean.setSpecialInvestigation(getDummySpecialInvestigations());
		notifiableImpairmentBean.setSymbol(getDummySymbols());
		notifiableImpairmentBean.setSpecialInvestigationcode(specialInvestigation.getCode());
		notifiableImpairmentBean.setSymbolcode(symbolBean.getCode());

		notifiableImpairmentBeanList.add(notifiableImpairmentBean);
		return notifiableImpairmentBeanList;
	}

	public static NotifiableImpairmentsRequest getDummyNotifiableImpairmentsRequest() {

		NotifiableImpairmentsRequest notifiableImpairmentsRequest = new NotifiableImpairmentsRequest();
		notifiableImpairmentsRequest.setInsuredPerson(getDummyInsuredPerson());
		List<ImpairmentNotificationBean> notificationBeanList = new ArrayList<>();

		List<NoteBean> notesList;
		NoteBean noteBean;
		notesList = new ArrayList<>();
		noteBean = new NoteBean();
		noteBean.setScratchpad("Y");
		noteBean.setNoteText("Note Text");
		noteBean.setNotificationID(NOTIFICATION_ID.toUpperCase());
		notesList.add(noteBean);

		ImpairmentNotificationBean notificationBean = new ImpairmentNotificationBean();

		notificationBean.setPolicyBenefit(policyBenefit);
		notificationBean.setPolicyNumber("12");
		notificationBean.setNotes(notesList);
		notificationBean.setNotifiableImpairments(getDummyNotifiableImpairmentBean());
		notificationBeanList.add(notificationBean);
		notifiableImpairmentsRequest.setNotifications(notificationBeanList);
		notifiableImpairmentsRequest.setNotifications(notificationBeanList);

		return notifiableImpairmentsRequest;

	}

	public static UpdateNotifiableImpairmentRequest getDummyUpdateNotifiableImpairmentRequest() {

		UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest = new UpdateNotifiableImpairmentRequest();
		updateNotifiableImpairmentRequest.setDelete(false);
		updateNotifiableImpairmentRequest.setImpairment(impairmentCode);
		updateNotifiableImpairmentRequest.setNotifiableImpairmentID(5);
		updateNotifiableImpairmentRequest.setNotificationID(NOTIFICATION_ID.toUpperCase());
		updateNotifiableImpairmentRequest.setReadings("100-90");
		List<LifeSpecBean> specialInvestigationBean = new ArrayList<>();
		updateNotifiableImpairmentRequest.setSpecialInvestigation(specialInvestigationBean);
		List<LifeSymbolBean> symbolBean = new ArrayList<>();
		updateNotifiableImpairmentRequest.setSymbol(symbolBean);
		updateNotifiableImpairmentRequest.setTimeSignal("2000");
		updateNotifiableImpairmentRequest.setUpdateReason("updatingggg");
		List<NoteBean> notesList;
		NoteBean noteBean;
		notesList = new ArrayList<>();
		noteBean = new NoteBean();
		noteBean.setScratchpad("Y");
		noteBean.setNoteText("Note Text");
		noteBean.setNotificationID(NOTIFICATION_ID.toUpperCase());
		notesList.add(noteBean);
		updateNotifiableImpairmentRequest.setNotes(notesList);
		return updateNotifiableImpairmentRequest;
	}

	public static UpdateNotifiableImpairmentRequest getDummyUpdateNotifiableImpairmentRequestForNoteOnly() {

		UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest = new UpdateNotifiableImpairmentRequest();
		updateNotifiableImpairmentRequest.setDelete(false);
		updateNotifiableImpairmentRequest.setImpairment(impairmentCode);
		updateNotifiableImpairmentRequest.setNotifiableImpairmentID(5);
		updateNotifiableImpairmentRequest.setNotificationID(NOTIFICATION_ID.toUpperCase());
		updateNotifiableImpairmentRequest.setReadings("2018");
		List<LifeSpecBean> specialInvestigationBean = new ArrayList<>();
		updateNotifiableImpairmentRequest.setSpecialInvestigation(specialInvestigationBean);
		List<LifeSymbolBean> symbolBean = new ArrayList<>();
		updateNotifiableImpairmentRequest.setSymbol(symbolBean);
		updateNotifiableImpairmentRequest.setTimeSignal("20");
		updateNotifiableImpairmentRequest.setUpdateReason("updatingggg");
		List<NoteBean> notesList;
		NoteBean noteBean;
		notesList = new ArrayList<>();
		noteBean = new NoteBean();
		noteBean.setScratchpad("N");
		noteBean.setNoteText("Note Text");
		noteBean.setNotificationID(NOTIFICATION_ID.toUpperCase());
		notesList.add(noteBean);
		updateNotifiableImpairmentRequest.setNotes(notesList);
		return updateNotifiableImpairmentRequest;
	}

	public static List<GenderBean> getDummyGenders() {
		List<GenderBean> dummyGenders = new ArrayList();
		dummyGenders.add(genderTypeBean);
		return dummyGenders;
	}

	public static List<TitleBean> getDummyTitles() {
		List<TitleBean> titles = new ArrayList();
		titles.add(titleBean);
		return titles;
	}

	public static List<IdentityTypeBean> getDummyIdentityTypes() {
		List<IdentityTypeBean> identityTypes = new ArrayList();
		identityTypes.add(identityTypeBean);
		return identityTypes;
	}

	public static List<LifeSymbolBean> getDummySymbols() {
		List<LifeSymbolBean> symbols = new ArrayList();
		symbols.add(symbolBean);
		return symbols;
	}

	public static List<LifeSpecBean> getDummySpecialInvestigations() {
		List<LifeSpecBean> spin = new ArrayList();
		spin.add(specialInvestigation);
		return spin;
	}

	public static List<PolicyTypeBean> getDummyPolicyBenefits() {
		List<PolicyTypeBean> policyBenefits = new ArrayList();
		policyBenefits.add(policyBenefit);
		return policyBenefits;
	}

	public static List<PaymentMethodBean> getDummyPaymentMethods() {
		List<PaymentMethodBean> payment = new ArrayList();
		payment.add(paymentMethod);
		return payment;
	}

	public static List<ImpairmentCodeBean> getDummyImpairmentCodes() {
		List<ImpairmentCodeBean> impairmentCodes = new ArrayList();
		;
		impairmentCodes.add(impairmentCode);
		return impairmentCodes;
	}

	public static List<ClaimCauseBean> getDummyClaimCauses() {
		List<ClaimCauseBean> claimCauses = new ArrayList();
		claimCauses.add(claimCause);
		return claimCauses;
	}

	public static List<ClaimStatusBean> getDummyClaimStatuses() {
		List<ClaimStatusBean> claimStatuses = new ArrayList();
		claimStatuses.add(claimStatus);
		return claimStatuses;
	}

	public static List<ClaimCategoryBean> getDummyClaimCategories() {
		List<ClaimCategoryBean> claimCategories = new ArrayList();
		claimCategories.add(claimCategory);
		return claimCategories;
	}

	public static List<ClaimTypeBean> getDummyClaimTypes() {
		List<ClaimTypeBean> claimTypes = new ArrayList();
		claimTypes.add(claimType);
		return claimTypes;
	}

	public static List<NoteBean> getDummyNote() {
		List<NoteBean> notes = new ArrayList<>();
		NoteBean note = new NoteBean();
		note.setNoteText("a");
		note.setNotificationID("12".toUpperCase());
		note.setPersonID("123");
		note.setScratchpad("Y");
		notes.add(note);
		return notes;
	}

	public static List<NotificationTypeBean> getDummyNotificationTypes() {
		List<NotificationTypeBean> notificationTypes = new ArrayList<>();
		notificationTypes.add(notificationTypeBean);
		return notificationTypes;
	}

	public static List<NotificationStatusBean> getDummyNotificationStatus() {
		List<NotificationStatusBean> notificationStatus = new ArrayList<>();
		notificationStatus.add(notificationStatusBean);
		return notificationStatus;
	}
	
	public static List<ReadingsCriteriaBean> getDummyReadings() {
		List<ReadingsCriteriaBean> reading = new ArrayList<>();
		reading.add(readings);
		return reading;
	}

	public static List<OfficeBean> getDummyOffice() {
		List<OfficeBean> offices = new ArrayList<>();
		OfficeBean office = new OfficeBean();
		office.setId("456");
		office.setName("Peter");
		office.setLocationID("780");
		office.setOfficeID("234");
		office.setDescription("Old mutual group");
		offices.add(office);
		return offices;
	}
}
