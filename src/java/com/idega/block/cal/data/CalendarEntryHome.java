package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;

import com.google.api.services.calendar.model.Calendar;
import com.idega.block.calendar.bean.Recurrence;
import com.idega.user.data.bean.Group;
import com.idega.user.data.bean.User;


public interface CalendarEntryHome extends com.idega.data.IDOHome
{
 public CalendarEntry create();

	public CalendarEntry findByPrimaryKey(Object pk);

	/**
	 *
	 * @param externalIds
	 *            is {@link Collection} of
	 *            {@link CalendarEntry#getExternalEventId()}, not
	 *            <code>null</code>;
	 * @return {@link Collection} of {@link CalendarEntry#getPrimaryKey()}s or
	 *         {@link Collections#emptyList()} on failure;
	 */
	Collection<CalendarEntry> findEntryByExternalId(
			Collection<String> externalIds);

 public java.util.Collection findEntryByTimestamp(java.sql.Timestamp p0)throws javax.ejb.FinderException;
 public java.util.Collection findEntriesBetweenTimestamps(java.sql.Timestamp p0, java.sql.Timestamp p1) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByLedgerID(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByEntryGroupID(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByICGroup(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByEvents(List eventsList) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByLedgerId(List listOfLedgerIds) throws javax.ejb.FinderException;

	/**
	 *
	 * @param calendarEntryTypes is {@link Collection} of
	 * {@link CalendarEntryType#getPrimaryKey()}, not <code>null</code>;
	 * @return {@link Collection} of {@link CalendarEntry} or
	 *         {@link Collections#emptyList()} on failure;
	 */
	Collection<CalendarEntry> findByTypes(List<String> calendarEntryTypes);

	/**
	 *
	 * @param externalEntryID is {@link CalendarEntryType#getPrimaryKey()},
	 * not <code>null</code>;
	 * @return entity or <code>null</code> on failure;
	 */
	CalendarEntry findEntryByExternalId(String externalEntryID);

	/**
	 *
	 * <p>Created or updates {@link CalendarEntry}</p>
	 * @param headline is the name of event, not <code>null</code>;
	 * @param ledger
	 * @param description of event, skipped if <code>null</code>;
	 * @param location
	 * @param calendarId
	 * @param externalEventId
	 * @return entity or <code>null</code> on failure;
	 */
	CalendarEntry update(
			User user,
			String headline,
			CalendarEntryType entryType,
			Date startTime,
			Date endTime,
			Group attendeesGroup,
			String ledger,
			String description,
			String location,
			String calendarId,
			String externalEventId,
			String link,
			CalendarEntryGroup reccurenceGroup);

	/**
	 *
	 * <p>Created or updates {@link CalendarEntry}</p>
	 * @param userId is {@link User#getId()}, not <code>null</code>;
	 * @param headline is the name of event, not <code>null</code>;
	 * @param entryTypeId is {@link CalendarEntryType#getPrimaryKey()},
	 * usually one of:
	 * <li>general</li>
	 * <li>practice</li>, 'general' is default value;
	 * @param startTime is the date and time of event, not <code>null</code>;
	 * @param endTime is the date and time of event, not <code>null</code>;
	 * @param attendeesGroupId is {@link Group#getId()} of {@link User} who
	 * will attend this event, skipped if <code>null</code>;
	 * @param ledger
	 * @param description of event, skipped if <code>null</code>;
	 * @param location
	 * @param calendarId
	 * @param externalEventId
	 * @return entities or {@link Collections#emptyList()} on failure
	 */
	List<CalendarEntry> update(Integer userId, String headline, Integer entryTypeId,
			Date startTime, Date endTime, Integer attendeesGroupId,
			String ledger, String description, String location, String calendarId,
			String externalEventId,
			String link,
			Recurrence recurrence);


 public Collection<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(List<String> eventsIds, List<String> groupsIds);


 public Collection<CalendarEntry> findEntriesByLedgerIdsOrGroupsIds(List<String> ledgersIds, List groupsIds, Timestamp from, Timestamp to) throws FinderException;
 public Collection<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds);

 public Collection<CalendarEntry> getEntriesByLedgersIds(List<String> ledgersIds);

 public Collection<CalendarEntry> findEntriesByCriteria(String calendarId, List<String> groupsIds, List<String> userIds, Timestamp from, Timestamp to) throws FinderException;


	/**
	 *
	 * @param calendarId is {@link Collection} of {@link Calendar#getId()}, 
	 * skipped if <code>null</code>;
	 * @param groupsIds is {@link Collection} of {@link Group#getId()},
	 * skipped if <code>null</code>;
	 * @param recurrenceGroups is {@link Collection} of 
	 * {@link CalendarEntryGroup#getPrimaryKey()}s, 
	 * skipped if <code>null</code>;
	 * @param userIds is {@link Collection} of {@link User#getId()},
	 * skipped if <code>null</code>;
	 * @param from is start {@link Date} of event, skipped if <code>null</code>;
	 * @param to is end {@link Date} of event, skipped if <code>null</code>;
	 * @param extendedResultSet when <code>true</code> events,
	 * which are already happening will be included or which going to end later,
	 * but it going to start today;
	 * @return {@link Collection} of {@link CalendarEntry} or
	 * {@link Collections#emptyList()} on failure;
	 */
	public Collection<CalendarEntry> findAllBy(
			Collection<String> calendarId,
			Collection<Integer> groupsIds,
			Collection<Integer> recurrenceGroups,
			Integer eventTypeId,
			List<String> userIds,
			Date from,
			Date to,
			boolean extendedResultSet);

	/**
	 * 
	 * <p>Removes the event and all recurring events, if exist</p>
	 * @param primaryKey is {@link CalendarEntry#getPrimaryKey()}, 
	 * not <code>null</code>;
	 */
	void purge(Integer primaryKey);

	/**
	 * 
	 * <p>Removes the event and all recurring events, if exist</p>
	 * @param entity to remove, not <code>null</code>;
	 */
	void purge(CalendarEntry entity);

	/**
	 * 
	 * @param user is invitee to events, not <code>null</code>;
	 * @param from is date event starts, skipped if <code>null</code>
	 * @param to is date event ends, skipped if <code>null</code>
	 * @param typeId is {@link CalendarEntryType#getPrimaryKey()}, 
	 * skipped if <code>null</code>
	 * @return entities or {@link Collections#emptyList()} on failure;
	 */
	Collection<CalendarEntry> findByInvitee(
			User user, 
			Date from, 
			Date to,
			Integer typeId);

	/**
	 * 
	 * @param user
	 * @param from
	 * @param to
	 * @param type is {@link CalendarEntryType#getPrimaryKey()}, 
	 * skipped if <code>null</code>
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<CalendarEntry> findAll(User user, Date from, Date to,
			Integer type);

}