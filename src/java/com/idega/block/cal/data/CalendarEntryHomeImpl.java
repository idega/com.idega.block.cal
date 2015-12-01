package com.idega.block.cal.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.user.dao.GroupDAO;
import com.idega.user.dao.UserDAO;
import com.idega.user.data.bean.Group;
import com.idega.user.data.bean.User;
import com.idega.util.ListUtil;
import com.idega.util.expression.ELUtil;



public class CalendarEntryHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryHome {	

	private static final long serialVersionUID = 6804879831234569243L;

	private CalendarEntryGroupHome calendarEntryGroupHome;

	private CalendarEntryTypeHome calendarEntryTypeHome;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private UserDAO userDAO;

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

		CalendarEntry entry = findEntryByExternalId(externalEventId);
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

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryHome#update(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CalendarEntry update(
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
			String reccurenceGroupName) {
		return update(
				getUserDAO().getUser(userId), 
				headline, 
				getCalendarEntryTypeHome().findByPrimaryKey(entryTypeId), 
				startTime, 
				endTime, 
				getGroupDAO().findGroup(attendeesGroupId), 
				ledger, description, location, calendarId, externalEventId, 
				getCalendarEntryGroupHome().update(null, reccurenceGroupName, Integer.valueOf(ledger)));
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
			String calendarId,
			List<String> groupsIds, 
			List<String> userIds, 
			Timestamp from,
			Timestamp to, 
			boolean extendedResultSet) {
		CalendarEntryBMPBean entity = (CalendarEntryBMPBean) idoCheckOutPooledEntity();
		Collection<Object> ids = entity.ejbFindBy(calendarId, groupsIds, 
				userIds, from, to, extendedResultSet);

		try {
			return getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to get entities by priamry keys: " + ids);
		}

		return Collections.emptyList();
	}
}