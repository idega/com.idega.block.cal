package com.idega.block.cal.business;

import java.util.List;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.SpringBeanName;
import com.idega.cal.bean.CalendarPropertiesBean;

@SpringBeanName("calendar")
public interface CalService {
	
	public boolean canUseRemoteServer(String server);

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
	
	public boolean removeCelandarEntriesFromCache(String instanceId);
	
	public CalendarPropertiesBean reloadProperties(String instanceId);
}
