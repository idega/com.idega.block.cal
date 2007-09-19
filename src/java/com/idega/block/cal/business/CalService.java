package com.idega.block.cal.business;


import java.rmi.RemoteException;
import java.util.List;

import com.idega.business.IBOService;

public interface CalService extends IBOService {
	/**
	 * @see com.idega.block.cal.business.CalServiceBean#setConnectionData
	 */
	public void setConnectionData(String serverName, String login, String password) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getCalendarParameters
	 */
	public List getCalendarParameters(String id) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getRemoteCalendarParameters
	 */
	public List getRemoteCalendarParameters(String id, String login, String password) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getRemoteEntries
	 */
	public List getRemoteEntries(List attributes, String login, String password) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalServiceBean#getEntries
	 */
	public List getEntries(List attributes) throws RemoteException;
}