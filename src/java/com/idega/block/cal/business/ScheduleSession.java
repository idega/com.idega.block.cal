package com.idega.block.cal.business;


import java.util.List;
import org.jdom.Document;
import com.idega.business.IBOSession;
import java.rmi.RemoteException;

public interface ScheduleSession extends IBOSession {

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
//	public Document getScheduleDOM(List<CalScheduleEntry> entries, String id) throws RemoteException;
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
	public int addEntries(List<CalScheduleEntry> entries, String id, boolean clearPreviousEntries);
	
	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getListOfEntries
	 */
	public List<CalScheduleEntry> getListOfEntries(String id);
}

