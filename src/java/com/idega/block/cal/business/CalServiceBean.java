package com.idega.block.cal.business;

import java.util.Collection;

import com.idega.business.IBOServiceBean;

public class CalServiceBean extends IBOServiceBean implements CalService {

	public void setConnectionData(String serverName, String login,
			String password) {
		// TODO Auto-generated method stub
System.out.println(serverName);
System.out.println(login);
System.out.println(password);
	}
	
	public Collection getTopGroupNodes(){
		GroupHelperBusinessBean groupHelper = new GroupHelperBusinessBean();
		Collection nodes = groupHelper.getTopGroupNodes();
		return nodes;
	}
	public String getDivId(){
		return CalendarConstants.groupsDivId;
	}

}
