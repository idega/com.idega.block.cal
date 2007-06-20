
package com.idega.block.cal.business;

import java.util.List;

import org.jdom.Document;

import com.idega.business.IBOSession;

public interface ScheduleSession extends IBOSession{

	public Document getSchedule(String id, List<ScheduleEntry> result, boolean usePreviousEntries);
	public Document changeModeToDay(String id);
	public Document changeModeToWorkweek(String id);			
	public Document changeModeToWeek(String id);			
	public Document changeModeToMonth(String id);			
	public Document getNext(String id);
	public Document getPrevious(String id);	
	public Document getScheduleDOM(List<ScheduleEntry> entries, String scheduleId);
	
	//to CalService
	
}
