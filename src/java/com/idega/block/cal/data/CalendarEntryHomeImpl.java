package com.idega.block.cal.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.block.calendar.bean.ExcludedPeriod;
import com.idega.block.calendar.bean.Recurrence;
import com.idega.block.calendar.data.AttendeeEntity;
import com.idega.block.calendar.data.dao.AttendeeDAO;
import com.idega.block.calendar.data.dao.ExcludedPeriodDAO;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.dao.GroupDAO;
import com.idega.user.dao.UserDAO;
import com.idega.user.data.bean.Group;
import com.idega.user.data.bean.User;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;
import com.idega.util.timer.DateUtil;



public class CalendarEntryHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryHome {

	private static final long serialVersionUID = 6804879831234569243L;

	private CalendarEntryGroupHome calendarEntryGroupHome;

	private CalendarEntryTypeHome calendarEntryTypeHome;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ExcludedPeriodDAO excludedPeriodDAO;

	@Autowired
	private AttendeeDAO attendeeDAO;

	private AttendeeDAO getAttendeeDAO() {
		if (this.attendeeDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.attendeeDAO;
	}

	private ExcludedPeriodDAO getExcludedPeriodDAO() {
		if (this.excludedPeriodDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.excludedPeriodDAO;
	}

	private UserDAO getUserDAO() {
		if (this.userDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.userDAO;
	}

	private GroupDAO getGroupDAO() {
		if (this.groupDAO == null) {
			ELUtil.getInstance().autowire(this);
		}

		return this.groupDAO;
	}

	private UserBusiness userBusiness;

	private UserBusiness getUserBusiness() {
		if (this.userBusiness == null) {
			try {
				this.userBusiness = IBOLookup.getServiceInstance(
						IWMainApplication.getDefaultIWApplicationContext(), 
						UserBusiness.class);
			} catch (IBOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + UserBusiness.class + " cause of: ", e);
			}
		}

		return this.userBusiness;
	}

	private CalendarEntryTypeHome getCalendarEntryTypeHome() {
		if (this.calendarEntryTypeHome == null) {
			try {
				this.calendarEntryTypeHome = (CalendarEntryTypeHome) IDOLookup
						.getHome(CalendarEntryType.class);
			} catch (RemoteException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, "Unable to find "
						+ CalendarEntryTypeHome.class + ": ", e);
			}
		}

		return this.calendarEntryTypeHome;
	}

	private CalendarEntryGroupHome getCalendarEntryGroupHome() {
		if (this.calendarEntryGroupHome == null) {
			try {
				this.calendarEntryGroupHome = (CalendarEntryGroupHome) IDOLookup
						.getHome(CalendarEntryGroup.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + CalendarEntryGroupHome.class +
						" cause of: ", e);
			}
		}

		return this.calendarEntryGroupHome;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#update(com.idega.user.data.bean.User, java.lang.String, com.idega.block.cal.data.CalendarEntryType, java.sql.Timestamp, java.sql.Timestamp, com.idega.user.data.bean.Group, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.idega.block.cal.data.CalendarEntryGroup)
	 */
	@Override
	public CalendarEntry update(
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
			CalendarEntryGroup reccurenceGroup) {

		/* Calendar group can't be null */
		if (reccurenceGroup == null) {
			reccurenceGroup = getCalendarEntryGroupHome().update(null,
					CalendarEntryCreator.noRepeatFieldParameterName,
					Integer.valueOf(ledger));
		}

		/* Calendar type can't be null */
		if (entryType == null) {
			entryType = getCalendarEntryTypeHome().update("general");
		}

		CalendarEntry entry = null;

		/*
		 * Search for existing entry only when there is no recurrence,
		 * otherwise the existing entries will be overwritten. Services like
		 * Google creates only one entry of event with the id, Idega creates
		 * multiple entries in a calendar entry group.
		 */
		if (reccurenceGroup.getName().equalsIgnoreCase("none")) {
			entry = findEntryByExternalId(externalEventId);
		}

		if (entry == null) {
			entry = create();
		}

		entry.setName(headline);
		entry.setDate(new Timestamp(startTime.getTime()));
		entry.setEndDate(new Timestamp(endTime.getTime()));
		entry.setCalendarId(calendarId);
		entry.setExternalEventId(externalEventId);
		entry.setDescription(description);
		entry.setLocation(location);
		
		if (!StringUtil.isEmpty(link)) {
			entry.setLink(link);
		}

		if (user != null) {
			entry.setUserID(user.getId());
		}

		if(attendeesGroup != null) {
			entry.setGroupID(attendeesGroup.getID());
		}

		if(Integer.parseInt(ledger) != -1) {
			entry.setLedgerID(Integer.parseInt(ledger));
		}

		entry.setEntryGroupID(reccurenceGroup.getEntryGroupID());

		if (entryType != null) {
			entry.setEntryTypeID((Integer) entryType.getPrimaryKey());
			entry.setEntryType(entryType.getName());
		}

		try {
			entry.store();
			reccurenceGroup.addEntry(entry);

			return entry;
		} catch(IDOStoreException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Failed to store entity, cause of:", e);
		}

		return null;
	}

	/**
	 *
	 * <p>Calculates next day of week to store</p>
	 * @param iteratedDate is a starting point to calculate, not <code>null</code>
	 * @param recurrence is rules defining how the result should be calculated,
	 * not <code>null</code>;
	 * @return iterated date;
	 */
	private LocalDate getNextWeekDate(LocalDate iteratedDate, Recurrence recurrence) {
		int currentDayValue = iteratedDate.getDayOfWeek().getValue();

		TreeSet<Integer> selectedDaysOfWeek = recurrence.getWeekDays().getSelectedValues();
		if (!ListUtil.isEmpty(selectedDaysOfWeek)) {

			/*
			 * Get next day on the same week
			 */
			Iterator<Integer> iterator = selectedDaysOfWeek.iterator();
			while (iterator.hasNext()) {
				Integer selectedDay = iterator.next();
				if (selectedDay > currentDayValue) {
					return iteratedDate.plusDays(selectedDay - currentDayValue);
				}
			}

			/*
			 * Get day on the next week
			 */
			iterator = selectedDaysOfWeek.iterator();
			iteratedDate = iteratedDate.plusDays(7 - currentDayValue);
			iteratedDate = iteratedDate.plusWeeks(recurrence.getRate() - 1);
			iteratedDate = iteratedDate.plusDays(iterator.next());
			return iteratedDate;
		}

		return iteratedDate.plusWeeks(recurrence.getRate());
	}

	public List<CalendarEntry> update(
			User user,
			String headline,
			CalendarEntryType entryType,
			Date startDate,
			Date endDate,
			Group attendeesGroup,
			String ledger,
			String description,
			String location,
			String calendarId,
			String externalEventId,
			String link,
			Recurrence reccurence) {

		ArrayList<CalendarEntry> entries = new ArrayList<CalendarEntry>();

		/*
		 * Cleaning old records before creating new ones
		 */
		CalendarEntryGroup reccurenceGroup = null;
		if (!StringUtil.isEmpty(externalEventId)) {
			CalendarEntry existingEntry = findEntryByExternalId(externalEventId);
			if (existingEntry != null) {
				reccurenceGroup = getCalendarEntryGroupHome().findByPrimaryKey(
						existingEntry.getEntryGroupID());
				if (reccurenceGroup != null) {
					Integer recurrenceGroupId = Integer.valueOf(
							reccurenceGroup.getPrimaryKey().toString());
					try {
						getAttendeeDAO().removeByEventGroup(recurrenceGroupId);
						getExcludedPeriodDAO().removeByEventGroup(recurrenceGroupId);
						getCalendarEntryGroupHome().purge(recurrenceGroupId);
					} catch (Exception e) {
						java.util.logging.Logger.getLogger(getClass().getName()).log(
								Level.WARNING, "Failed to remove old records, cause of:", e);
						return entries;
					}
				}
			}
		}

		/*
		 * Creating new calendar recurrence group
		 */
		reccurenceGroup = getCalendarEntryGroupHome().update(null,
				reccurence.getType(),
				Integer.valueOf(ledger));
		
		if (reccurence.getFrom() == null) {
			reccurence.setFrom(startDate);
		}

		if (reccurence.getTo() == null) {
			reccurence.setTo(endDate);
		}

		LocalDate iterator = DateUtil.getDate(reccurence.getFrom());
		LocalDate end = DateUtil.getDate(reccurence.getTo());

		LocalTime startTime = DateUtil.getTime(startDate);
		long difference = endDate.getTime() - startDate.getTime();

		/*
		 * Creating new events
		 */
		while (iterator.isBefore(end) || iterator.isEqual(end)) {
			Date momentStart = DateUtil.getDate(startTime, iterator);
			Date momentEnd = new Date(momentStart.getTime() + difference);

			boolean excluded = Boolean.FALSE;
			for (ExcludedPeriod period : reccurence.getExcludedPeriods()) {
				if (period.getFrom().before(momentStart) && momentStart.before(period.getTo())) {
					excluded = Boolean.TRUE;
				}
			}

			if (!excluded) {
				CalendarEntry entry = update(
						user,
						headline,
						entryType,
						momentStart,
						momentEnd,
						attendeesGroup,
						ledger,
						description,
						location,
						calendarId,
						externalEventId,
						link,
						reccurenceGroup);
				if (entry != null) {
					entries.add(entry);
				}
			}

			if (reccurenceGroup.getName().equalsIgnoreCase(CalendarEntryCreator.noRepeatFieldParameterName)) {
				break;
			}

			if (reccurenceGroup.getName().equalsIgnoreCase(CalendarEntryCreator.dailyFieldParameterName)) {
				iterator = iterator.plusDays(reccurence.getRate());
			}

			if (reccurenceGroup.getName().equalsIgnoreCase(CalendarEntryCreator.weeklyFieldParameterName)) {
				iterator = getNextWeekDate(iterator, reccurence);
			}

			if (reccurenceGroup.getName().equalsIgnoreCase(CalendarEntryCreator.monthlyFieldParameterName)) {
				iterator = iterator.plusMonths(reccurence.getRate());
			}

			if (reccurenceGroup.getName().equalsIgnoreCase(CalendarEntryCreator.yearlyFieldParameterName)) {
				iterator = iterator.plusYears(reccurence.getRate());
			}
		}

		for (ExcludedPeriod period : reccurence.getExcludedPeriods()) {
			getExcludedPeriodDAO().update(null, 
					Integer.valueOf(reccurenceGroup.getPrimaryKey().toString()), 
					period.getFrom(), 
					period.getTo());
		}

		return entries;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#update(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<CalendarEntry> update(
			Integer userId,
			String headline,
			Integer entryTypeId,
			Date startTime,
			Date endTime,
			Integer attendeesGroupId,
			String ledger,
			String description,
			String location,
			String calendarId,
			String externalEventId,
			String link,
			Recurrence recurrence) {
		return update(
				getUserDAO().getUser(userId),
				headline,
				getCalendarEntryTypeHome().findByPrimaryKey(entryTypeId),
				startTime,
				endTime,
				getGroupDAO().findGroup(attendeesGroupId),
				ledger,
				description,
				location,
				calendarId,
				externalEventId,
				link,
				recurrence);
	}

	@Override
	protected Class<CalendarEntry> getEntityInterfaceClass() {
		return CalendarEntry.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#create()
	 */
	@Override
	public CalendarEntry create() {
		try {
			return (CalendarEntry) super.createIDO();
		} catch (CreateException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Failed to create entity, cause of:", e);

		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#findEntryByExternalId(java.util.Collection)
	 */
	@Override
	public Collection<CalendarEntry> findEntryByExternalId(Collection<String> externalEntryID) {
		CalendarEntryBMPBean entity = (CalendarEntryBMPBean) idoCheckOutPooledEntity();
		Collection<Object> ids = (entity).ejbFindEntryByExternalId(externalEntryID);

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Failed to get " + getEntityInterfaceClass() +
					" by primary keys: " + ids);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#findEntryByExternalId(java.lang.String)
	 */
	@Override
	public CalendarEntry findEntryByExternalId(String externalEntryID) {
		Collection<CalendarEntry> entries = findEntryByExternalId(
				Arrays.asList(externalEntryID));
		if (!ListUtil.isEmpty(entries)) {
			return entries.iterator().next();
		}

		return null;
	}

@Override
public java.util.Collection findEntryByTimestamp(java.sql.Timestamp p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByTimestamp(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

@Override
public java.util.Collection findEntriesBetweenTimestamps(java.sql.Timestamp p0, java.sql.Timestamp p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryBetweenTimestamps(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
@Override
public java.util.Collection findEntriesByLedgerID(int p0) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByLedgerID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}
@Override
public java.util.Collection findEntriesByEntryGroupID(int p0) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByEntryGroupID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);


}

@Override
public java.util.Collection findEntriesByICGroup(int p0) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByICGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

@Override
public java.util.Collection findEntriesByEvents(List eventsList) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByEvents(eventsList);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#findByPrimaryKey(java.lang.Object)
	 */
	@Override
	public CalendarEntry findByPrimaryKey(Object pk) {
		if (pk != null) {
			try {
				return (CalendarEntry) super.findByPrimaryKeyIDO(pk);
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, "Failed to get entity by primary key: " + pk);
			}
		}

		return null;
	}

 @Override
public java.util.Collection findEntriesByLedgerId(List listOfLedgerIds) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByICGroup(listOfLedgerIds);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);

 }

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#findByTypes(java.util.List)
	 */
	@Override
	public Collection<CalendarEntry> findByTypes(List<String> calendarEntryTypes) {
		CalendarEntryBMPBean entity = (CalendarEntryBMPBean) this
				.idoCheckOutPooledEntity();
		Collection<Object> ids = entity.ejbFindByTypes(calendarEntryTypes);
		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING,
					"Failed to get " + getEntityInterfaceClass()
							+ " by primary keys: " + ids);
		}

		return Collections.emptyList();
	}

 @Override
public Collection<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(List<String> eventsIds, List<String> groupsIds) {
	 IDOEntity entity = this.idoCheckOutPooledEntity();
	 Collection<CalendarEntry> ids = ((CalendarEntryBMPBean)entity).getEntriesByEventsIdsAndGroupsIds(eventsIds, groupsIds);
	 this.idoCheckInPooledEntity(entity);
	 try {
		return this.getEntityCollectionForPrimaryKeys(ids);
	 } catch (Exception e) {
		e.printStackTrace();
		return null;
	 }
 }

 @Override
public Collection<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds) {
	 IDOEntity entity = this.idoCheckOutPooledEntity();
	 Collection<CalendarEntry> ids = ((CalendarEntryBMPBean)entity).getEntriesByLedgersIdsAndGroupsIds(ledgersIds, groupsIds);
	 this.idoCheckInPooledEntity(entity);
	 try {
		return this.getEntityCollectionForPrimaryKeys(ids);
	 } catch (Exception e) {
		e.printStackTrace();
		return null;
	 }
 }

 @Override
public Collection<CalendarEntry> getEntriesByLedgersIds(List<String> ledgersIds) {
	 IDOEntity entity = this.idoCheckOutPooledEntity();
	 Collection<CalendarEntry> ids = null;
	 try {
		 ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByLedgersIds(ledgersIds);
	 } catch (Exception e) {
		 e.printStackTrace();
	 }
	 this.idoCheckInPooledEntity(entity);
	 try {
		return this.getEntityCollectionForPrimaryKeys(ids);
	 } catch (Exception e) {
		e.printStackTrace();
		return null;
	 }
 }

 @Override
public Collection<CalendarEntry> findEntriesByLedgerIdsOrGroupsIds(List<String> ledgersIds, List groupsIds, Timestamp from, Timestamp to) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection<CalendarEntry> ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByLedgerIdsOrGroupsIds(ledgersIds, groupsIds, from, to);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
 }

 @Override
public Collection<CalendarEntry> findEntriesByCriteria(String calendarId, List<String> groupsIds, List<String> userIds, Timestamp from, Timestamp to) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByCriteria(calendarId, groupsIds, userIds, from, to);
		return this.getEntityCollectionForPrimaryKeys(ids);
 }

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#findAllBy(
	 * 		java.lang.String,
	 * 		java.util.List,
	 * 		java.util.List,
	 * 		java.sql.Timestamp,
	 * 		java.sql.Timestamp,
	 * 		boolean
	 * )
	 */
	@Override
	public Collection<CalendarEntry> findAllBy(
			Collection<String> calendarId,
			Collection<Integer> groupsIds,
			Collection<Integer> recurrenceGroups,
			Integer eventTypeId,
			List<String> userIds,
			Date from,
			Date to,
			boolean extendedResultSet) {
		CalendarEntryBMPBean entity = (CalendarEntryBMPBean) idoCheckOutPooledEntity();
		Collection<Object> ids = entity.ejbFindBy(calendarId, groupsIds, 
				recurrenceGroups, eventTypeId, userIds, from, to, 
				extendedResultSet);

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Failed to get entities by priamry keys: " + ids);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#remove(com.idega.block.cal.data.CalendarEntry)
	 */
	@Override
	public void purge(CalendarEntry entity) {
		if (entity != null) {
			getCalendarEntryGroupHome().purge(entity.getEntryGroupID());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#remove(java.lang.Integer)
	 */
	@Override
	public void purge(Integer primaryKey) {
		purge(findByPrimaryKey(primaryKey));
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#findByInvitee(com.idega.user.data.bean.User, java.util.Date, java.util.Date, java.lang.Integer)
	 */
	@Override
	public Collection<CalendarEntry> findByInvitee(
			com.idega.user.data.bean.User user, 
			Date from, 
			Date to, 
			Integer typeId) {
		ArrayList<Integer> calendarEntryGroups = new ArrayList<Integer>();

		List<AttendeeEntity> attendees = getAttendeeDAO().findByInvitee(user);
		for (AttendeeEntity attendee : attendees) {
			calendarEntryGroups.add(attendee.getEventGroupId());
		}

		if (!ListUtil.isEmpty(calendarEntryGroups)) {
			return findAllBy(null, null, 
					calendarEntryGroups, typeId, null, from, to, true);
		}

		return Collections.emptyList();
	}

	@Override
	public Collection<CalendarEntry> findAll(User user, Date from, Date to, Integer type) {
		ArrayList<CalendarEntry> result = new ArrayList<CalendarEntry>();
		
		if (user != null) {
			ArrayList<Integer> gruopsIds = new ArrayList<Integer>();

			Collection<com.idega.user.data.Group> groups = null;
			try {
				groups = getUserBusiness().getUserGroups(user.getId());
			} catch (Exception e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get groups for user by name: " + user.getName());
			}

			if (!ListUtil.isEmpty(groups)) {
				for (com.idega.user.data.Group group : groups) {
					gruopsIds.add((Integer) group.getPrimaryKey());
				}
			}

			Collection<CalendarEntry> groupEvents = findAllBy(null, gruopsIds, 
					null, type, null, from, to, true);
			if (!ListUtil.isEmpty(groupEvents)) {
				result.addAll(groupEvents);
			}

			Collection<CalendarEntry> eventInvites = findByInvitee(user, from, 
					to, type);
			if (!ListUtil.isEmpty(eventInvites)) {
				result.addAll(eventInvites);
			}
		}

		return result;
	}
}