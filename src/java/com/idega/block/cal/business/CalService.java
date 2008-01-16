package com.idega.block.cal.business;


import java.util.List;
import com.idega.business.IBOSession;
import java.rmi.RemoteException;

public interface CalService extends IBOSession {
	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getAvailableCalendarEventTypesWithLogin
	 */
	public List getAvailableCalendarEventTypesWithLogin(String login,
			String password) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getAvailableLedgersWithLogin
	 */
	public List getAvailableLedgersWithLogin(String login, String password)
			throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#addUniqueIdsForCalendarGroups
	 */
	public Boolean addUniqueIdsForCalendarGroups(String instanceId, List ids)
			throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#addUniqueIdsForCalendarLedgers
	 */
	public Boolean addUniqueIdsForCalendarLedgers(String instanceId, List ids)
			throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#addUniqueIdsForCalendarEvents
	 */
	public Boolean addUniqueIdsForCalendarEvents(String instanceId, List ids)
			throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getCalendarEntries
	 */
	public List getCalendarEntries(String login, String password,
			String instanceId, Integer cacheTime, Boolean remoteMode)
			throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#removeCelandarEntriesFromCache
	 */
	public boolean removeCelandarEntriesFromCache(String instanceId)
			throws RemoteException;
}