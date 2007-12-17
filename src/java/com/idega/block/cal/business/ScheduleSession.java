package com.idega.block.cal.business;


import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import org.jdom.Document;

import com.idega.business.SpringBeanName;

@SpringBeanName("calendarSchedule")
public interface ScheduleSession {

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToDayAndGetScheduleDOM
	 */
	public Document changeModeToDayAndGetScheduleDOM(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToWorkweekAndGetScheduleDOM
	 */
	public Document changeModeToWorkweekAndGetScheduleDOM(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToWeekAndGetScheduleDOM
	 */
	public Document changeModeToWeekAndGetScheduleDOM(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToMonthAndGetScheduleDOM
	 */
	public Document changeModeToMonthAndGetScheduleDOM(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#switchToNextAndGetScheduleDOM
	 */
	public Document switchToNextAndGetScheduleDOM(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#switchToPreviousAndGetScheduleDOM
	 */
	public Document switchToPreviousAndGetScheduleDOM(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getScheduleDOM
	 */
	public Document getScheduleDOM(String id) throws RemoteException;
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToDayAndGetListOfEntries
	 */
	public List<CalScheduleEntry> changeModeToDayAndGetListOfEntries(String id);	
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToWorkweekAndGetListOfEntries
	 */
	public List<CalScheduleEntry> changeModeToWorkweekAndGetListOfEntries(String id);

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToWeekAndGetListOfEntries
	 */
	public List<CalScheduleEntry> changeModeToWeekAndGetListOfEntries(String id);
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#changeModeToMonthAndGetListOfEntries
	 */
	public List<CalScheduleEntry> changeModeToMonthAndGetListOfEntries(String id);
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#switchToPreviousAndGetListOfEntries
	 */
	public List<CalScheduleEntry> switchToPreviousAndGetListOfEntries(String scheduleId);
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#switchToNextAndGetListOfEntries
	 */
	public List<CalScheduleEntry> switchToNextAndGetListOfEntries(String scheduleId);
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#addEntries
	 */
	public boolean addEntries(List<CalScheduleEntry> entries, String id, boolean clearPreviousEntries);
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getListOfEntries
	 */
	public List<CalScheduleEntry> getListOfEntries(String id);
	
	public boolean addCalendarEntryForInfoWindow(CalScheduleEntry entry);
	
	public CalScheduleEntry getCalendarEntryForInfoWindow(String id);
	
	public CalScheduleEntry getCalendarEntry(String entryId, String scheduleId, Locale locale);
}

