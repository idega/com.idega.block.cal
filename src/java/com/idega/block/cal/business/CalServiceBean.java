package com.idega.block.cal.business;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.cal.data.CalendarEntryBMPBean;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.block.cal.data.CalendarManagerBean;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.cal.bean.CalendarPropertiesBean;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class CalServiceBean implements CalService {
	
	private LoginTableHome loginHome = null;
	private LoginBusinessBean loginBean = null;
	private CalBusiness calBusiness = null;
	
	public void setConnectionData(String serverName, String login, String password) {
	}
	
	@SuppressWarnings("unchecked")
	public List<CalendarLedgersAndTypes> getCalendarParameters(String id){
		List<CalendarLedgersAndTypes> calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return calendarParameters;
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return calendarParameters;
		}
	
		
		List ledgersByGroupId = calBusiness.getLedgersByGroupId(id);
		List allEntryTypes = calBusiness.getAllEntryTypes();
		
		Object o = null;
		if (ledgersByGroupId != null) {
			for (int i = 0; i < ledgersByGroupId.size(); i++) {
				o = ledgersByGroupId.get(i);
				if (o instanceof CalendarLedgerBMPBean) {
					CalendarLedgerBMPBean ledger = (CalendarLedgerBMPBean) o;
					calendarParameters.add(new CalendarLedgersAndTypes(String.valueOf(ledger.getID()), ledger.getName(), "L"));
				}
			}
		}
		
		if (allEntryTypes != null) {
			for (int i = 0; i < allEntryTypes.size(); i++) {
				o = allEntryTypes.get(i);
				if (o instanceof CalendarEntryTypeBMPBean) {
					CalendarEntryTypeBMPBean entryType = (CalendarEntryTypeBMPBean) o;
					calendarParameters.add(new CalendarLedgersAndTypes(String.valueOf(entryType.getID()), entryType.getName(), "T"));
				}
			}
		}
				
		return calendarParameters;
	}
	public List<CalendarLedgersAndTypes> getRemoteCalendarParameters(String id, String login, String password){
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
	
	public List<CalScheduleEntry> getRemoteEntries(List<String> attributes, String login, String password) {
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
	
	@SuppressWarnings("unchecked")
	public List<CalScheduleEntry> getEntries(List<String> attributes) {
		List<CalScheduleEntry> result = new ArrayList<CalScheduleEntry>();
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return result;
		}
		
		String parameter = null;
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		
		List<String> listOfLedgerIds = new ArrayList<String>(); 
		List<String> listOfEntryTypesIds = new ArrayList<String>();
		
		if (attributes == null)
			return result;
		
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
		if (entriesToDisplay == null) {
			return result;
		}
		
		for (int i = 0; i < entriesToDisplay.size(); i++) {
			CalendarEntryBMPBean entry = (CalendarEntryBMPBean)entriesToDisplay.get(i);
			if (checkIfTypeIsCorrect(entry, listOfEntryTypesIds)) {
				CalScheduleEntry calEntry = new CalScheduleEntry();
				calEntry.setId(entry.getStringColumnValue("CAL_ENTRY_ID"));
				calEntry.setEntryName(entry.getStringColumnValue("CAL_ENTRY_NAME"));
				
				calEntry.setEntryDate(entry.getStringColumnValue("CAL_ENTRY_DATE"));
				calEntry.setEntryEndDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE"));
				
				calEntry.setEntryTime(getTime(entry.getStringColumnValue("CAL_ENTRY_DATE")));
				calEntry.setEntryTime(getTime(entry.getStringColumnValue("CAL_ENTRY_END_DATE")));
				calEntry.setEntryTypeName(entry.getStringColumnValue("CAL_TYPE_NAME"));
				calEntry.setRepeat(entry.getStringColumnValue("CAL_ENTRY_REPEAT"));
				calEntry.setEntryDescription(entry.getStringColumnValue("CAL_ENTRY_DESCRIPTION"));
				result.add(calEntry);
			}
		}
		return result;
	}
	
	private boolean checkIfTypeIsCorrect(CalendarEntryBMPBean entry, List<String> entryTypesIds) {
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
		String interfaceScript = new StringBuffer(server).append(CalendarConstants.CALENDAR_SERVICE_DWR_INTERFACE_SCRIPT).toString();
		
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
	
	private CalendarManagerBean getBean() {
		Object o = WFUtil.getBeanInstance(CalendarConstants.CALENDAR_MANAGER_BEAN_ID);
		if (!(o instanceof CalendarManagerBean)) {
			return null;
		}
		return (CalendarManagerBean) o;
	}
	
	public CalendarPropertiesBean getCalendarProperties(String instanceId) {
		if (instanceId == null) {
			return null;
		}
		CalendarManagerBean bean = getBean();
		if (bean == null) {
			return null;
		}
		
		CalendarPropertiesBean properties = bean.getCalendarProperties(instanceId);	
		return properties;
	}

	private String getTime(String entryDate){
		return entryDate.substring(11,16);
	}
	
	@SuppressWarnings("unchecked")
	public List<AdvancedProperty> getAvailableCalendarEventTypes(List<String> eventTypes) {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return null;
		}
		
		List eventsTypes = null;
		try {
			eventsTypes = calBusiness.getAllEntryTypes();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (eventsTypes == null) {
			return null;
		}
		
		List<AdvancedProperty> types = new ArrayList<AdvancedProperty>();
		Object o = null;
		CalendarEntryType calendarEntryType = null;
		for (int i = 0; i < eventsTypes.size(); i++) {
			o = eventsTypes.get(i);
			System.out.println("Object in calendar: " + o);
			if (o instanceof CalendarEntryType) {
				calendarEntryType = (CalendarEntryType) o;
				types.add(new AdvancedProperty(calendarEntryType.getId(), calendarEntryType.getName()));
			}
		}
		
		
		return types;
	}
	
	private CalBusiness getCalBusiness(IWApplicationContext iwac) {
		if (calBusiness == null) {
			try {
				calBusiness = (CalBusiness) IBOLookup.getServiceInstance(iwac, CalBusiness.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return calBusiness;
	}
}
