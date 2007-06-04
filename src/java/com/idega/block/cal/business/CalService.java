package com.idega.block.cal.business;

import java.util.Collection;
import java.util.List;

import com.idega.business.IBOService;

public interface CalService extends IBOService{
	public void setConnectionData(String serverName, String login, String password);
	
	public List getCalendarParametersList();
	
	public List getCalendarParameters(String id);
	
	public int setCheckedParameters(List checkedParameters);
	
//	public void getTopGroupNodes(String serverName, String login, String password);
	
//	public Collection getTopGroupNodes();
	
//	public String getDivId();
}
