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
	
	public List<AdvancedProperty> getAvailableCalendarEventTypes(List<String> eventTypes);
}
