package com.idega.block.cal.business;

import java.util.List;

import com.idega.user.bean.GroupsAndCalendarPropertiesBean;

public interface CalService {
	
//	public List<CalendarEntryBMPBean> getEntriesToDisplay(List<String> listOfEntryTypesIds, List<String> listOfLedgerIds);
	
//	public List<CalendarLedgerBMPBean> getLedgersByGroupId(String id);
	
//	public List<CalendarEntryTypeBMPBean> getAllEntryTypes();

//	public Document setCheckedParameters(String id, List<String> checkedParameters);
	public List<CalendarLedgersAndTypes> getCalendarParameters(String id);
	public List<CalendarLedgersAndTypes> getRemoteCalendarParameters(String id, String login, String password);
	public boolean canUseRemoteServer(String server);
	public List<CalScheduleEntry> getEntries(List<String> calendarAttributes);
	public List<CalScheduleEntry> getRemoteEntries(List<String> calendarAttributes, String login, String password);

	public GroupsAndCalendarPropertiesBean getCalendarProperties(String scheduleId);
	
	//public void setConnectionData(String serverName, String login, String password);
	
//	public List getCalendarParametersList();
//	
//	public List getCalendarParameters(String id);
//	
//	public Document setCheckedParameters(List checkedParameters);
//	
//	public Document changeModeToDay();
//	public Document changeModeToWorkweek();			
//	public Document changeModeToWeek();			
//	public Document changeModeToMonth();			
//	public Document getNext();
//	public Document getPrevious();	
	
//	public void getTopGroupNodes(String serverName, String login, String password);
	
//	public Collection getTopGroupNodes();
	
//	public String getDivId();
}
