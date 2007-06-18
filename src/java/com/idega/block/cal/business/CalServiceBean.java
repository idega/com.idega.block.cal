package com.idega.block.cal.business;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.myfaces.custom.schedule.HtmlSchedule;

import com.idega.block.cal.data.CalendarEntryBMPBean;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.business.IBOServiceBean;

public class CalServiceBean extends IBOServiceBean implements CalService {

	private HtmlSchedule schedule = null;
	private SimpleDateFormat simpleDate = null;
	private int dateMode = -1;
	private static final int DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final int WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;
	private static final int MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 30;
	
//	private ScheduleModel scheduleModel = null;
	
	public void setConnectionData(String serverName, String login,
			String password) {
		// TODO Auto-generated method stub
	}
	
	public List<CalendarEntryBMPBean> getEntriesToDisplay(List<String> listOfEntryTypesIds, List<String> listOfLedgerIds){
		return null;
	}
	
	public List<CalendarLedgerBMPBean> getLedgersByGroupId(String id){
		return null;
	}
	
	public List<CalendarEntryTypeBMPBean> getAllEntryTypes(){
		return null;
	}
	
	
//	public List getCalendarParametersList(){
//		
//		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
//		
//		IWContext iwc = IWContext.getInstance();
//		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
//		List <CalendarLedgerBMPBean>allLedgers = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllLedgers();
//		
//		List <CalendarEntryTypeBMPBean>allEntryTypes = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllEntryTypes();
//		
//		for (int i = 0; i < allLedgers.size(); i++) {
//			CalendarLedgerBMPBean ledger = allLedgers.get(i);
//			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
//		}
//		
//		for (int i = 0; i < allEntryTypes.size(); i++) {
//			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
//			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
//		}
//
//		return calendarParameters;
//	}
	

	
//	private String getDate(String entryDate){
//		String date = entryDate.substring(0, 10);
//		return date.replaceAll("-", "");
//	}
//
//	private String getTime(String entryDate){
//		return entryDate.substring(11,16);
//	}
	


}
