package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.FinderException;


public interface CalendarEntry extends com.idega.data.IDOEntity
{
 public int getEntryID();
 public Timestamp getDate();
 public int getDay();
 public String getDescription();
 public String getLocation();
 public Timestamp getEndDate();
 public String getEntryType();
 public String getEntryTypeName();
 public int getEntryTypeID();
 public int getGroupID();
 public int getLedgerID();
 public String getName();
 public String getRepeat();
 public int getUserID();
 public Collection getUsers();
 public int getEntryGroupID();
 public String getCalendarId();
 public String getExternalEventId();
 public String getEventRecurrence();
 public void setDate(Timestamp p0);
 public void setDescription(String p0);
 public void setLocation(String p0);
 public void setEndDate(Timestamp p0);
 public void setEntryType(String p0);
 public void setEntryTypeID(int p0);
 public void setGroupID(int p0);
 public void setLedgerID(int p0);
 public void setName(String p0);
 public void setRepeat(String p0);
 public void setUserID(int p0);
 public void setEntryGroupID(int p0);
 public void setCalendarId(String calendarId);
 public void setExternalEventId(String externalEvent);
 public void setEventRecurrence(String eventRecurrence);

 public Collection<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(List<String> eventsIds, List<String> groupsIds);

 public Collection<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds);

 public Collection<Object> ejbFindEntriesByCriteria(String calendarId, List<String> groupsIds, List<String> userIds, Timestamp from, Timestamp to) throws FinderException;

	/**
	 * 
	 * @param calendarEntryTypes is {@link Collection} of 
	 * {@link CalendarEntryType#getPrimaryKey()}, not <code>null</code>;
	 * @return {@link Collection} of {@link CalendarEntry#getPrimaryKey()}
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<Object> ejbFindByTypes(List<String> calendarEntryTypes);

	/**
	 * 
	 * @return link to view of this event
	 */
	String getLink();

	/**
	 * 
	 * @param link to view of this event 
	 */
	void setLink(String link);
}
