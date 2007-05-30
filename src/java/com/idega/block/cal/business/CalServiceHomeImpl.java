package com.idega.block.cal.business;

import javax.ejb.CreateException;

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
