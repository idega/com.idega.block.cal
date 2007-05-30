package com.idega.block.cal.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;

import com.idega.business.IBOHomeImpl;

public class CalServiceHomeImpl extends IBOHomeImpl implements CalServiceHome {


	private static final long serialVersionUID = 7141978995132493723L;

	public Class getBeanInterfaceClass() {
		return CalService.class;
	}

	public CalService create() throws CreateException {
		return (CalService) super.createIBO();
	}

}
