package za.co.oldmutual.asisa.common;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.claims.ClaimNotificationBean;
import za.co.oldmutual.asisa.common.bean.NoteBean;
import za.co.oldmutual.asisa.common.dao.NotificationDAO;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.impairments.ImpairmentNotificationBean;
import za.co.oldmutual.asisa.refdata.bean.PolicyTypeBean;

public class NotificationDAOTest extends AbstractTest {
	@InjectMocks
	private NotificationDAO notificationDAO;

	@Mock
	UserLoginIdentifier userLoginIdentifier;

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;
	ImpairmentNotificationBean impairmentNotificationBean = new ImpairmentNotificationBean();
	PolicyTypeBean policyBenefit = new PolicyTypeBean();
	NoteBean noteBean = new NoteBean();

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
		super.setUp();
		policyBenefit.setCode("7");
		policyBenefit.setDescription("REPUDIATION");
		impairmentNotificationBean.setPolicyBenefit(policyBenefit);
		impairmentNotificationBean.setPolicyNumber("12");
		List<NoteBean> noteBeanList = new ArrayList<>();
		noteBean.setScratchpad("scratchpad");
		noteBean.setNoteText("Note Text");
		noteBeanList.add(noteBean);
		impairmentNotificationBean.setNotes(noteBeanList);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class)))
				.thenReturn(1);
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
	}

	@Test
	public void insertNoteTest() throws ParseException, ResourceUpdationFailedException {
		ClaimNotificationBean claimNotificationBean = CommonTestData.getDummyClaimNotificationBeanRequest();
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		String notificationID = notificationDAO.insertClaimNotification(claimNotificationBean);
		assert (!StringUtils.isEmpty(notificationID));
	}

	@Test
	public void insertScratchpadTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		String notificationID = notificationDAO.insertImpairmentNotification(impairmentNotificationBean);
		assert (!StringUtils.isEmpty(notificationID));
	}

	@Test
	public void insertClaimNotificationTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		ClaimNotificationBean claimNotificationBean = CommonTestData.getDummyClaimNotificationBeanRequest();
		notificationDAO.insertClaimNotification(claimNotificationBean);
	}

	@Test
	public void updateNotificationTest() throws ParseException, ResourceUpdationFailedException {
		notificationDAO.updateNotification("1", "UPDATE", "ACTIVE", "Y", "XY57850");
	}
}
