package za.co.oldmutual.asisa.history;

import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.oldmutual.asisa.AbstractTest;
import za.co.oldmutual.asisa.CommonTestData;
import za.co.oldmutual.asisa.common.bean.InsuredPersonBean;
import za.co.oldmutual.asisa.common.validation.ResourceNotFoundException;
import za.co.oldmutual.asisa.common.validation.SpecialCharactersFoundException;
import za.co.oldmutual.asisa.iphistory.IPClaimHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPHistoryController;
import za.co.oldmutual.asisa.iphistory.IPHistoryDAO;
import za.co.oldmutual.asisa.iphistory.IPImpairmentHistoryBean;
import za.co.oldmutual.asisa.iphistory.IPNoteHistoryBean;
import za.co.oldmutual.asisa.refdata.RefDataDAO;


public class IPHistoryControllerTest extends AbstractTest {
  @Mock
  RefDataDAO refDataDAO;

  @Mock
  IPHistoryDAO iPHistoryDAO;

  @Mock
  IPClaimHistoryBean iPClaimHistoryBean;

  @Mock
  IPImpairmentHistoryBean iPImpairmentHistoryBean;

  @Mock
  IPNoteHistoryBean iPNoteHistoryBean;

  @InjectMocks
  IPHistoryController iPHistoryController;

  IPHistoryBean ipHistoryBean = new IPHistoryBean();
  InsuredPersonBean insuredPersonHistoryResponse =
      CommonTestData.getDummyInsuredPersonBeanResponse();
  List<IPNoteHistoryBean> noteHistory = CommonTestData.getDummyNoteHistoryResponse();
  List<IPImpairmentHistoryBean> impairmentHistory =
      CommonTestData.getDummyImpairmentHistoryResponse();
  List<IPClaimHistoryBean> claimHistory = CommonTestData.getDummyClaimHistoryResponse();

  @Override
  @Before
  public void setUp() throws ParseException, ResourceNotFoundException,SpecialCharactersFoundException  {
    super.setUp();
    ipHistoryBean.setInsuredPerson(insuredPersonHistoryResponse);
    ipHistoryBean.setClaimHistory(claimHistory);
    ipHistoryBean.setImpairmentHistory(impairmentHistory);
    ipHistoryBean.setNoteHistory(noteHistory);
    ipHistoryBean.setScratchpadHistory(noteHistory);
  }

  @Test
  public void getHistory() throws Exception {
    String identityTypeCode = "4";
    String identityNumber = "123450000";
    String personId = "123";
    Mockito
        .when(
            iPHistoryDAO.getInsuredPersonsHistory(identityTypeCode, identityNumber, personId))
        .thenReturn(ipHistoryBean);
    ResponseEntity<IPHistoryBean> result =
        iPHistoryController.history(identityTypeCode, identityNumber, personId);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(ipHistoryBean.toString(), content);
  }

  @Test
  public void invalidHistoryRequest() throws Exception {
    String identityTypeCode = "4";
    String identityNumber = "1234";
    String personId = "123";
    ipHistoryBean.setScratchpadHistory(null);
    ipHistoryBean.setClaimHistory(null);
    ipHistoryBean.setImpairmentHistory(null);
    ipHistoryBean.setInsuredPerson(null);
    ipHistoryBean.setNoteHistory(null);
    Mockito
        .when(
            iPHistoryDAO.getInsuredPersonsHistory(identityTypeCode, identityNumber, personId))
        .thenReturn(ipHistoryBean);
    ResponseEntity<IPHistoryBean> result =
        iPHistoryController.history(identityTypeCode, identityNumber, personId);
    assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
    String content = result.getBody().toString();
    assertEquals(ipHistoryBean.toString(), content);
  }
}
