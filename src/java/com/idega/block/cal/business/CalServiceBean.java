package com.idega.block.cal.business;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.myfaces.custom.schedule.HtmlSchedule;

import com.idega.block.cal.data.CalendarEntryBMPBean;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
//import com.idega.block.cal.data.ScheduleEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;

public class CalServiceBean extends IBOServiceBean implements CalService {

	private HtmlSchedule schedule = null;
	private SimpleDateFormat simpleDate = null;
	private int dateMode = -1;
	private static final int DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final int WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;
	private static final int MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 30;
	
	private LoginTableHome loginHome = null;
	private LoginBusinessBean loginBean = null;
//	private ScheduleModel scheduleModel = null;
	
	public void setConnectionData(String serverName, String login,
			String password) {
		// TODO Auto-generated method stub
	}
	
//	public List<ScheduleEntryBMPBean> getEntriesToDisplay(List<String> listOfEntryTypesIds, List<String> listOfLedgerIds){
//		return null;
//	}
//	
//	public List<CalendarLedgerBMPBean> getLedgersByGroupId(String id){
//		return null;
//	}
//	
//	public List<ScheduleEntryTypeBMPBean> getAllEntryTypes(){
//		return null;
//	}
	public List getCalendarParameters(String id){

		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = IWContext.getInstance();
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		
		CalBusiness calBusiness = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc);
		
		List ledgersByGroupId = calBusiness.getLedgersByGroupId(id);
		
		List allEntryTypes = calBusiness.getAllEntryTypes();
		
		for (int i = 0; i < ledgersByGroupId.size(); i++) {
			CalendarLedgerBMPBean ledger = (CalendarLedgerBMPBean)ledgersByGroupId.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = (CalendarEntryTypeBMPBean)allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
		}
				
		return calendarParameters;
	}
	public List getRemoteCalendarParameters(String id, String login, String password){
		if (login == null || password == null) {
			return null;
		}

		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		if (isLoggedUser(iwc, login)) {
			return getCalendarParameters(id);
		}
		
		if (logInUser(iwc, login, password)) {
			return getCalendarParameters(id);
		}
		
		return null;
	}
	
	private boolean isLoggedUser(IWContext iwc, String userName) {
		if (iwc == null || userName == null) {
			return false;
		}
		
		//	Geting current user
		User current = null;
		try {
			current = iwc.getCurrentUser();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (current == null) {	//	Not logged
			return false;
		}
		
		LoginTableHome loginHome = getLoginHome();
		if (loginHome == null) {
			return false;
		}
		
		int userId = 0;
		try {
			userId = Integer.valueOf(current.getId());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		
		//	Checking if current user is making request
		LoginTable login = null;
		try {
			login = loginHome.findLoginForUser(userId);
		} catch (FinderException e) {
			e.printStackTrace();
			return false;
		}
		if (userName.equals(login.getUserLogin())) {
			return true;
		}
		
		return false;
	}		
	
	private boolean logInUser(IWContext iwc, String login, String password) {
		if (iwc == null || login == null || password == null) {
			return false;
		}

		return getLoginBean(iwc).logInUser(iwc.getRequest(), login, password);
	}	
	
	private synchronized LoginBusinessBean getLoginBean(IWContext iwc) {
		if (loginBean == null) {
			try {
				loginBean = LoginBusinessBean.getLoginBusinessBean(iwc.getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return loginBean;
	}
	
	private synchronized LoginTableHome getLoginHome() {
		if (loginHome == null) {
			try {
				loginHome = (LoginTableHome) IDOLookup.getHome(LoginTable.class);
			} catch (IDOLookupException e) {
				e.printStackTrace();
			}
		}
		return loginHome;
	}
	
	public List<ScheduleEntry> getRemoteEntries(List<String> attributes, String login, String password){
		if (login == null || password == null) {
			return null;
		}

		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		if (isLoggedUser(iwc, login)) {
			return getEntries(attributes);
		}
		
		if (logInUser(iwc, login, password)) {
			return getEntries(attributes);
		}
		
		return null;
	}
	
	public List<ScheduleEntry> getEntries(List<String> attributes){
		IWContext iwc = IWContext.getInstance();
		String parameter = null;
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		
		List<String> listOfLedgerIds = new ArrayList<String>(); 
		List<String> listOfEntryTypesIds = new ArrayList<String>();
		
		for (int i = 0; i < attributes.size(); i++) {
			parameter = (String)(attributes.get(i));
			if(parameter.substring(0, 1).equals("L")){
				listOfLedgerIds.add(parameter.substring(1));
			}
			if(parameter.substring(0, 1).equals("T")){
				listOfEntryTypesIds.add(parameter.substring(1));
			}			
		}
		
		CalBusiness calBusiness = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc);
		
		List entriesToDisplay = calBusiness.getEntriesByLedgersAndEntryTypes(listOfEntryTypesIds, listOfLedgerIds);

		List<ScheduleEntry> result = new ArrayList<ScheduleEntry>();

		for (int i = 0; i < entriesToDisplay.size(); i++) {
			CalendarEntryBMPBean entry = (CalendarEntryBMPBean)entriesToDisplay.get(i);
			if(checkIfTypeIsCorrect(entry, listOfEntryTypesIds)){
//				result.add(new ScheduleEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), getDate(entry.getStringColumnValue("CAL_ENTRY_DATE")), getDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE")),getTime(entry.getStringColumnValue("CAL_ENTRY_DATE")), getTime(entry.getStringColumnValue("CAL_ENTRY_END_DATE")), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
				ScheduleEntry calEntry = new ScheduleEntry();
				calEntry.setEntryName(entry.getStringColumnValue("CAL_ENTRY_NAME"));
				
//				calEntry.setEntryDate(getDate(entry.getStringColumnValue("CAL_ENTRY_DATE")));
//				calEntry.setEntryEndDate(getDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE")));
				calEntry.setEntryDate(entry.getStringColumnValue("CAL_ENTRY_DATE"));
				calEntry.setEntryEndDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE"));
				
				calEntry.setEntryTime(getTime(entry.getStringColumnValue("CAL_ENTRY_DATE")));
				calEntry.setEntryTime(getTime(entry.getStringColumnValue("CAL_ENTRY_END_DATE")));
				calEntry.setEntryTypeName(entry.getStringColumnValue("CAL_TYPE_NAME"));
				calEntry.setRepeat(entry.getStringColumnValue("CAL_ENTRY_REPEAT"));
				result.add(calEntry);
//				result.add(new ScheduleEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), entry.getStringColumnValue("CAL_ENTRY_DATE"), entry.getStringColumnValue("CAL_ENTRY_END_DATE"), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
			}
		}
//		dateMode.put(id, ScheduleModel.MONTH);
		return result;
	}
	private boolean checkIfTypeIsCorrect(CalendarEntryBMPBean entry, List<String> entryTypesIds){
		String typeId = entry.getStringColumnValue("CAL_TYPE_ID");
		for (int i = 0; i < entryTypesIds.size(); i++) {
			if(entryTypesIds.get(i).equals(typeId))
				return true;
		}
		return false;
	}	

	/**
	 * Checks if can use DWR on remote server
	 */
	public boolean canUseRemoteServer(String server) {
		if (server == null) {
			return false;
		}
		
		if (server.endsWith("/")) {
			server = server.substring(0, server.lastIndexOf("/"));
		}
		
		String engineScript = new StringBuffer(server).append("/dwr/engine.js").toString();
		String interfaceScript = new StringBuffer(server).append(CoreConstants.SCHEDULE_SESSION_DWR_INTERFACE_SCRIPT).toString();
		
		return (existsFileOnRemoteServer(engineScript) && existsFileOnRemoteServer(interfaceScript));
	}
	/**
	 * Checks if file exists on server
	 * @param urlToFile
	 * @return
	 */
	private boolean existsFileOnRemoteServer(String urlToFile) {
		InputStream streamToFile = null;
		
		try {
			URL dwr = new URL(urlToFile);
			streamToFile = dwr.openStream();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if (streamToFile == null) {
			return false;
		}
		try {
			streamToFile.close();
		} catch (Exception e) {}
		
		return true;
	}	
	
//	public List getCalendarParametersList(){
//		
//		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
//		
//		IWContext iwc = IWContext.getInstance();
//		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
//		List <CalendarLedgerBMPBean>allLedgers = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllLedgers();
//		
//		List <ScheduleEntryTypeBMPBean>allEntryTypes = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllEntryTypes();
//		
//		for (int i = 0; i < allLedgers.size(); i++) {
//			CalendarLedgerBMPBean ledger = allLedgers.get(i);
//			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
//		}
//		
//		for (int i = 0; i < allEntryTypes.size(); i++) {
//			ScheduleEntryTypeBMPBean entryType = allEntryTypes.get(i);
//			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
//		}
//
//		return calendarParameters;
//	}
	

	
	private String getDate(String entryDate){
		String date = entryDate.substring(0, 10);
		return date.replaceAll("-", "");
	}

	private String getTime(String entryDate){
		return entryDate.substring(11,16);
	}
	


}
