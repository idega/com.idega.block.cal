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
	public Document getSchedule(String id, List<ScheduleEntry> result, boolean usePreviousEntries) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.ScheduleSessionBean#getScheduleDOM
	 */
	public Document getScheduleDOM(List<ScheduleEntry> entries, String id) throws RemoteException;
}