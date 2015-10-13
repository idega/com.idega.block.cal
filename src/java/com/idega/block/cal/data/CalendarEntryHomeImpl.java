package com.idega.block.cal.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOEntity;



public class CalendarEntryHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryHome
{

	private static final long serialVersionUID = 6804879831234569243L;


@Override
protected Class<CalendarEntry> getEntityInterfaceClass(){
  return CalendarEntry.class;
 }


 @Override
public CalendarEntry create() throws javax.ejb.CreateException{
  return (CalendarEntry) super.createIDO();
 }

	@Override
	public Collection<CalendarEntry> findEntryByExternalId(String externalEntryID) throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntryByExternalId(externalEntryID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
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

 @Override
public CalendarEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
	return (CalendarEntry) super.findByPrimaryKeyIDO(pk);
 }
 @Override
public java.util.Collection findEntriesByLedgerId(List listOfLedgerIds) throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryBMPBean)entity).ejbFindEntriesByICGroup(listOfLedgerIds);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);

 }

 @Override
public Collection<CalendarEntry> getEntriesByEventsIds(List<String> eventsIds) {
	 IDOEntity entity = this.idoCheckOutPooledEntity();
	 Collection<CalendarEntry> ids = ((CalendarEntryBMPBean)entity).getEntriesByEventsIds(eventsIds);
	 this.idoCheckInPooledEntity(entity);
	 try {
		return this.getEntityCollectionForPrimaryKeys(ids);
	 } catch (Exception e) {
		e.printStackTrace();
		return null;
	 }
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
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);

 }

}