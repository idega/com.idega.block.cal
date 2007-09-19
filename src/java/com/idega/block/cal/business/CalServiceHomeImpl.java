package com.idega.block.cal.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CalServiceHomeImpl extends IBOHomeImpl implements CalServiceHome {
	public Class getBeanInterfaceClass() {
		return CalService.class;
	}

	public CalService create() throws CreateException {
		return (CalService) super.createIBO();
	}
}