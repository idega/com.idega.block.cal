package com.idega.block.cal.business;

import javax.ejb.CreateException;

import com.idega.business.IBOHomeImpl;

public class ScheduleSessionHomeImpl extends IBOHomeImpl implements ScheduleSessionHome {


	private static final long serialVersionUID = 7141978995132493723L;

	public Class getBeanInterfaceClass() {
		return ScheduleSession.class;
	}

	public ScheduleSession create() throws CreateException {
		return (ScheduleSession) super.createIBO();
	}

}
