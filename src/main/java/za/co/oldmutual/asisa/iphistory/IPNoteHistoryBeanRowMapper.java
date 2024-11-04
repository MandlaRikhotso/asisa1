package za.co.oldmutual.asisa.iphistory;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

@Component
public class IPNoteHistoryBeanRowMapper implements RowMapper<IPNoteHistoryBean> {

	@Autowired
	private ReferenceDataCache referenceDataCache;

	@Override
	public IPNoteHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		IPNoteHistoryBean ipNoteHistoryBean = new IPNoteHistoryBean();
		
		 ipNoteHistoryBean.setBusinessUnit(referenceDataCache.populateDescFromCode(rs.getString("BUSINESS_UNIT"), RefTypeEnum.OFFICE));
		 ipNoteHistoryBean.setNotificationSource(ipNoteHistoryBean.getBusinessUnit());
		 ipNoteHistoryBean.setOmUserId(rs.getString("OMUSERID"));
		 ipNoteHistoryBean.setRole(rs.getString("ROLE"));
		 ipNoteHistoryBean.setScratchpad(rs.getString("SCRATCHPAD"));
		 ipNoteHistoryBean.setDateCreated(rs.getString("DATE_CREATED"));
		 ipNoteHistoryBean.setNoteId(rs.getInt("NOTE_ID"));
		 ipNoteHistoryBean.setNoteText(rs.getString("NOTE_TEXT"));
		 ipNoteHistoryBean.setCreatedBy(rs.getString("CREATED_BY").toUpperCase());
		 ipNoteHistoryBean.setPolicyNumber(rs.getString("POLICYNUMBER"));
		 
		return ipNoteHistoryBean;
	}

}
