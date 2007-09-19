package com.idega.block.cal.business;

import java.util.List;

public interface CalService {
	
	public List getCalendarParameters(String id);
	public List getRemoteCalendarParameters(String id, String login, String password);
	public List getEntries(List calendarAttributes);
	public List getRemoteEntries(List calendarAttributes, String login, String password);

	
}
