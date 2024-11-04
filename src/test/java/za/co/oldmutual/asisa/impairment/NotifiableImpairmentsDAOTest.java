package za.co.oldmutual.asisa.impairment;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.CommonQueries;
import za.co.oldmutual.asisa.common.dao.InsuredPersonDAO;
import za.co.oldmutual.asisa.common.dao.NoteDAO;
import za.co.oldmutual.asisa.common.dao.NotificationDAO;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.email.MailService;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaBean;
import za.co.oldmutual.asisa.enquiry.IPEnquiryDAO;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentBean;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentRowMapper;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsDAO;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsDAOQueries;
import za.co.oldmutual.asisa.impairments.NotifiableImpairmentsRequest;
import za.co.oldmutual.asisa.impairments.UpdateNotifiableImpairmentRequest;
import za.co.oldmutual.asisa.refdata.RefDataDAO;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

public class NotifiableImpairmentsDAOTest extends AbstractTest {

	@Mock
	private IPEnquiryDAO insuredPersonEnquiryDAO;

	@Mock
	InsuredPersonDAO insuredPersonDAO;

	@Mock
	NotificationDAO notificationDAO;

	@Mock
	ReferenceDataCache referenceDataCache;

	@Mock
	UserLoginIdentifier userLoginIdentifier;

	@Mock
	NoteDAO noteDAO;

	@Mock
	RefDataDAO refDataDAO;

	@InjectMocks
	private NotifiableImpairmentsDAO notifiableImpairmentsDAO;

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	MailService mailService;

	@Mock
	NotifiableImpairmentRowMapper notifiableImpairmentRowMapper;

	NotifiableImpairmentsRequest notifiableImpairmentsRequest;
	Map<String, Object> responseObject = new HashMap<>();
	UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequest;
	UpdateNotifiableImpairmentRequest updateNotifiableImpairmentRequestForNote;
	List<NotifiableImpairmentBean> notifiableImpairmentBeanList;
	List<NotifiableImpairmentBean> notifiableImpairments = new ArrayList<>();

	private final ResultSet rs = mock(ResultSet.class);

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException, SpecialCharactersFoundException {
		super.setUp();
		notifiableImpairmentsRequest = CommonTestData.getDummyNotifiableImpairmentsRequest();
		notifiableImpairmentBeanList = CommonTestData.getDummyNotifiableImpairmentBean();
		updateNotifiableImpairmentRequest = CommonTestData.getDummyUpdateNotifiableImpairmentRequest();
		updateNotifiableImpairmentRequestForNote = CommonTestData
				.getDummyUpdateNotifiableImpairmentRequestForNoteOnly();
		Mockito.when(insuredPersonEnquiryDAO.getInsuredPersonID(
				insuredPersonDAO.getIPSearchCriteria(notifiableImpairmentsRequest.getInsuredPerson()))).thenReturn("1");
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableImpairmentsRequest.getInsuredPerson()))
				.thenReturn("1");
		Mockito.when(
				notificationDAO.insertImpairmentNotification(notifiableImpairmentsRequest.getNotifications().get(0)))
				.thenReturn("1");
		Map<String, Object> model = new HashMap<>();
		model.put("reason", "change in data");
		Mockito.when(mailService.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.any(Map.class)))
				.thenReturn(true);
	}

	@Test
	public void addNotifiableImpairments() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		responseObject = notifiableImpairmentsDAO.addNotifiableImpairments(notifiableImpairmentsRequest);
		assertEquals("Impairments saved successfully", responseObject.get("successMessage").toString());
	}

	@Test
	public void addPersonAndScratchpad() throws ParseException, ResourceUpdationFailedException {
		InsuredPersonBean insuredPerson = new InsuredPersonBean();
		insuredPerson.setPersonID("0");
		Mockito.when(insuredPersonEnquiryDAO.getInsuredPersonID(
				insuredPersonDAO.getIPSearchCriteria(notifiableImpairmentsRequest.getInsuredPerson()))).thenReturn("0");
		notifiableImpairmentsRequest.getNotifications().forEach(i -> i.setNotifiableImpairments(notifiableImpairments));
		notifiableImpairmentsRequest.getNotifications().forEach(i -> i.setNotes(CommonTestData.getDummyNote()));
		notifiableImpairmentsRequest.getNotifications().forEach(i -> i.setPolicyNumber("1234"));
		notifiableImpairmentsRequest.setInsuredPerson(insuredPerson);
		responseObject = notifiableImpairmentsDAO.addNotifiableImpairments(notifiableImpairmentsRequest);
		assertNotNull(responseObject);
	}

	@Test
	public void addScratchpadForExistingPerson() throws ParseException, ResourceUpdationFailedException {
		notifiableImpairmentsRequest.getNotifications().forEach(i -> i.setNotifiableImpairments(notifiableImpairments));
		notifiableImpairmentsRequest.getNotifications().forEach(i -> i.setNotes(CommonTestData.getDummyNote()));
		notifiableImpairmentsRequest.getNotifications().forEach(i -> i.setPolicyNumber("1234"));
		responseObject = notifiableImpairmentsDAO.addNotifiableImpairments(notifiableImpairmentsRequest);
		assertNotNull(responseObject);
	}

	@Test
	public void addNotifiableImpairmentsCheckException() throws ParseException, ResourceUpdationFailedException {
		try {
			responseObject = notifiableImpairmentsDAO.addNotifiableImpairments(null);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deleteNotifiableImpairments() throws ResourceUpdationFailedException {
		updateNotifiableImpairmentRequest.setDelete(true);
		updateNotifiableImpairmentRequest.setUpdateReason("deleteing");

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(CommonQueries.UPDATE_NOTIFICATION_QUERY.toString()),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(NotifiableImpairmentsDAOQueries.UPDATE_NOTIFIABLE_IMPAIRMENT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(NotifiableImpairmentsDAOQueries.FETCH_DETAIL_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(notifiableImpairmentBeanList);

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(NotifiableImpairmentsDAOQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		responseObject = notifiableImpairmentsDAO.updateNotifiableImpairment(updateNotifiableImpairmentRequest);
		assertEquals("Impairments deleted successfully", responseObject.get("successMessage").toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateNotifiableImpairments() throws ResourceUpdationFailedException {
		List<Map<String, Object>> check = new ArrayList<>();
		Map<String, Object> fields = new HashMap<>();
		fields.put("READINGS", "25/100");
		fields.put("TIME_SIGNAL", "2");
		check.add(fields);

		Mockito.when(namedParameterJdbcTemplate.queryForList(
				Mockito.eq(NotifiableImpairmentsDAOQueries.FETCH_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(check);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(CommonQueries.UPDATE_NOTIFICATION_QUERY.toString()),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(NotifiableImpairmentsDAOQueries.UPDATE_REASON_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(NotifiableImpairmentsDAOQueries.FETCH_DETAIL_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(notifiableImpairmentBeanList);

		Mockito.when(namedParameterJdbcTemplate.update(
				Mockito.eq(NotifiableImpairmentsDAOQueries.INSERT_NOTIFIABLE_IMPAIRMENT_AUDIT_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		responseObject = notifiableImpairmentsDAO.updateNotifiableImpairment(updateNotifiableImpairmentRequest);
		assertEquals("Impairments updated successfully", responseObject.get("successMessage").toString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateNotifiableImpairmentsForAddingNoteOnly() throws ResourceUpdationFailedException {
		List<Map<String, Object>> result = new ArrayList<>();
		Map<String, Object> obj = new HashMap<>();
		obj.put("READINGS", "100");
		obj.put("TIME_SIGNAL", "200");
		result.add(obj);

		Mockito.when(namedParameterJdbcTemplate.queryForList(
				Mockito.eq(NotifiableImpairmentsDAOQueries.FETCH_IMPAIRMENT_QUERY),
				Mockito.any(MapSqlParameterSource.class))).thenReturn(result);

		Mockito.when(namedParameterJdbcTemplate.query(Mockito.eq(NotifiableImpairmentsDAOQueries.FETCH_DETAIL_QUERY),
				Mockito.any(MapSqlParameterSource.class), Mockito.any(RowMapper.class)))
				.thenReturn(notifiableImpairmentBeanList);

		Mockito.when(namedParameterJdbcTemplate.update(Mockito.eq(CommonQueries.INSERT_NOTE_QUERY),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);

		responseObject = notifiableImpairmentsDAO.updateNotifiableImpairment(updateNotifiableImpairmentRequestForNote);
		assertEquals("Notes Or Scratchpad inserted successfully!", responseObject.get("successMessage").toString());
	}

	@Test
	public void updateOrDeleteNotifiableImpairmentsCheckException() throws ResourceUpdationFailedException {
		try {
			responseObject = notifiableImpairmentsDAO.updateNotifiableImpairment(null);
		} catch (Exception e) {
			assertThatExceptionOfType(ResourceUpdationFailedException.class);
		}
	}

	@Test
	public void addNotifiableImpairmentsNewPerson() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(insuredPersonDAO.getIPSearchCriteria(notifiableImpairmentsRequest.getInsuredPerson()))
				.thenReturn(Mockito.mock(IPEnquiryCriteriaBean.class));
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		Mockito.when(insuredPersonEnquiryDAO.getInsuredPersonID(Mockito.any())).thenReturn("0");
		Mockito.when(insuredPersonDAO.insertInsuredPerson(notifiableImpairmentsRequest.getInsuredPerson()))
				.thenReturn("1");
		responseObject = notifiableImpairmentsDAO.addNotifiableImpairments(notifiableImpairmentsRequest);
		assertEquals("Impairments saved successfully", responseObject.get("successMessage").toString());
	}

}
