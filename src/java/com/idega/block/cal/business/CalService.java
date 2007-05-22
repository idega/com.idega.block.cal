package com.idega.block.cal.business;

import java.util.Collection;

import com.idega.business.IBOService;

public interface CalService extends IBOService{
	public void setConnectionData(String serverName, String login, String password);
}
