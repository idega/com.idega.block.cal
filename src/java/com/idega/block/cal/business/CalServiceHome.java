package com.idega.block.cal.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CalServiceHome extends IBOHome {
	public CalService create() throws CreateException, RemoteException;
}