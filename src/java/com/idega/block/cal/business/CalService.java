package com.idega.block.cal.business;

import java.util.List;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.cal.bean.CalendarPropertiesBean;

public interface CalService {
	
	public List<CalendarLedgersAndTypes> getCalendarParameters(String id);
	
	public List<CalendarLedgersAndTypes> getRemoteCalendarParameters(String id, String login, String password);
	
	public boolean canUseRemoteServer(String server);
	
	public List<CalScheduleEntry> getEntries(List<String> calendarAttributes);
	
	public List<CalScheduleEntry> getRemoteEntries(List<String> calendarAttributes, String login, String password);

	public CalendarPropertiesBean getCalendarProperties(String instanceId);
	
	public List<AdvancedProperty> getAvailableCalendarEventTypes();
	
	public List<AdvancedProperty> getAvailableCalendarEventTypesWithLogin(String login, String password);
	
	public List<String> getCalendarInformation();
	
	public boolean addUniqueIdsForCalendarGroups(String instanceId, List<String> ids);
	
	public boolean addUniqueIdsForCalendarLedgers(String instanceId, List<String> ids);
	
	public boolean addUniqueIdsForCalendarEvents(String instanceId, List<String> ids);
	
	public List<CalScheduleEntry> getCalendarEntries(String login, String password, String instanceId, Integer cacheTime, boolean remoteMode);
	
	public List<AdvancedProperty> getAvailableLedgers();
	
	public List<AdvancedProperty> getAvailableLedgersWithLogin(String login, String password);
}
