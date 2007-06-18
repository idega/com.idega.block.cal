package com.idega.block.cal.business;

import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface ScheduleSessionHome extends IBOHome {
	public ScheduleSession create() throws CreateException, RemoteException;
}