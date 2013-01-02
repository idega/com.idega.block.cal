package com.idega.block.cal.business.events;

import java.sql.Timestamp;
import java.util.List;

import com.idega.block.cal.data.CalendarEntry;
import com.idega.user.data.User;

public interface EventsProvider {

	public List<CalendarEntry> getEvents(User user, Timestamp from, Timestamp to);

}