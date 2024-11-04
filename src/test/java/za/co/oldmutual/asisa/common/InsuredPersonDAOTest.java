package za.co.oldmutual.asisa.common;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.StringUtils;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.dao.InsuredPersonDAO;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.ResourceUpdationFailedException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.common.validation.UserLoginIdentifier;
import za.co.oldmutual.asisa.enquiry.IPEnquiryCriteriaBean;

public class InsuredPersonDAOTest extends AbstractTest {
	@InjectMocks
	private InsuredPersonDAO insuredPersonDAO;

	@Mock
	NamedParameterJdbcOperations namedParameterJdbcTemplate;

	@Mock
	UserLoginIdentifier userLoginIdentifier;

	InsuredPersonBean insuredPersonBean;
	Map<String, Object> responseObject = new HashMap<>();

	@Override
	@Before
	public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
		super.setUp();
		insuredPersonBean = CommonTestData.getDummyInsuredPerson();
	}

	@Test
	public void getIPSearchCriteriaTest() throws ParseException, ResourceUpdationFailedException {
		IPEnquiryCriteriaBean iPEnquiryCriteriaBean;
		iPEnquiryCriteriaBean = insuredPersonDAO.getIPSearchCriteria(insuredPersonBean);
		assert (!StringUtils.isEmpty(iPEnquiryCriteriaBean));
	}

	@Test
	public void insertInsuredPersonTest() throws ParseException, ResourceUpdationFailedException {
		Mockito.when(namedParameterJdbcTemplate.update(Mockito.anyString(),
				Mockito.any(BeanPropertySqlParameterSource.class))).thenReturn(1);
		Mockito.when(userLoginIdentifier.fetchUserDetails()).thenReturn("XY52340");
		String personId = insuredPersonDAO.insertInsuredPerson(insuredPersonBean);
		assert (!StringUtils.isEmpty(personId));
	}
}
