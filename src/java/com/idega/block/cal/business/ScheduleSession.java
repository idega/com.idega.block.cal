package com.idega.block.cal.business;


import java.util.List;
import org.jdom.Document;
import com.idega.business.IBOSession;
import java.rmi.RemoteException;

public interface ScheduleSession extends IBOSession {
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToDay
	 */
	public Document changeModeToDay(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToWorkweek
	 */
	public Document changeModeToWorkweek(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToWeek
	 */
	public Document changeModeToWeek(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToMonth
	 */
	public Document changeModeToMonth(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getNext
	 */
	public Document getNext(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getPrevious
	 */
	public Document getPrevious(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getSchedule
	 */
	public Document getSchedule(String id, List<CalScheduleEntry> result, boolean usePreviousEntries) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getScheduleDOM
	 */
	public Document getScheduleDOM(List<CalScheduleEntry> entries, String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#initializeSchedule
	 */
	public int initializeSchedule(List<CalScheduleEntry> entries, String id);
	
	public List<CalScheduleEntry> changeModeToDayAndDisplayEntriesAsList(String id);	
	
	public List<CalScheduleEntry> changeModeToWorkweekAndDisplayEntriesAsList(String id);

	public List<CalScheduleEntry> changeModeToWeekAndDisplayEntriesAsList(String id);
	
	public List<CalScheduleEntry> changeModeToMonthAndDisplayEntriesAsList(String id);
	
	public List<CalScheduleEntry> getPreviousAsList(String scheduleId);
	
	public List<CalScheduleEntry> getNextAsList(String scheduleId);
}

