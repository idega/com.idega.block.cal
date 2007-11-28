package com.idega.block.cal.business;

import java.util.ArrayList;
import java.util.List;

import com.idega.block.cal.bean.CalendarManagerBean;
import com.idega.block.cal.data.CalendarEntryBMPBean;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.cal.bean.CalendarPropertiesBean;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupService;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class CalServiceBean implements CalService {
	
	private CalBusiness calBusiness = null;
	private GroupService groupService = null;
	
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
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return null;
		}
		
		if (groupService.isLoggedUser(iwc, login)) {
			return getCalendarParameters(id);
		}
		
		if (groupService.logInUser(iwc, login, password)) {
			return getCalendarParameters(id);
		}
		
		return null;
	}
	
	public List<CalScheduleEntry> getRemoteEntries(List<String> attributes, String login, String password) {
		if (login == null || password == null) {
			return null;
		}

		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return null;
		}
		
		if (groupService.isLoggedUser(iwc, login)) {
			return getEntries(attributes);
		}
		
		if (groupService.logInUser(iwc, login, password)) {
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
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return false;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return false;
		}
	
		List<String> scripts = new ArrayList<String>();
		scripts.add("/dwr/engine.js");
		scripts.add(CalendarConstants.CALENDAR_SERVICE_DWR_INTERFACE_SCRIPT);
		
		return groupService.canMakeCallToServerAndScript(server, scripts);
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
	public List<AdvancedProperty> getAvailableCalendarEventTypes() {
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
			if (o instanceof CalendarEntryType) {
				calendarEntryType = (CalendarEntryType) o;
				types.add(new AdvancedProperty(calendarEntryType.getId(), calendarEntryType.getName()));
			}
		}
		
		
		return types;
	}
	
	public List<AdvancedProperty> getAvailableCalendarEventTypesWithLogin(String login, String password) {
		if (login == null || password == null) {
			return null;
		}
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return null;
		}
		if (!groupService.isLoggedUser(iwc, login)) {
			if (!groupService.logInUser(iwc, login, password)) {
				return null;
			}
		}
		
		return getAvailableCalendarEventTypes();
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
	
	private GroupService getGroupService(IWApplicationContext iwac) {
		if (groupService == null) {
			try {
				groupService = (GroupService) IBOLookup.getServiceInstance(iwac, GroupService.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return groupService;
	}
}
