package com.idega.block.cal.business;

import java.util.List;

import org.jdom.Document;

public interface ScheduleSession {
	public Document setCheckedParameters(String id, List<String> checkedParameters);
	public Document getSchedule(String id, List<CalEntry> result, boolean usePreviousEntries);
	public List getCalendarParameters(String id);
	public Document changeModeToDay(String id);
	public Document changeModeToWorkweek(String id);			
	public Document changeModeToWeek(String id);			
	public Document changeModeToMonth(String id);			
	public Document getNext(String id);
	public Document getPrevious(String id);	
}
