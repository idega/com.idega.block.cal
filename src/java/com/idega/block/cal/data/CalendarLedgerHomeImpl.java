package com.idega.block.cal.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

public class CalendarLedgerHomeImpl extends com.idega.data.IDOFactory implements CalendarLedgerHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarLedger.class;
 }


 public CalendarLedger create() throws javax.ejb.CreateException{
  return (CalendarLedger) super.createIDO();
 }


 public CalendarLedger findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarLedger) super.findByPrimaryKeyIDO(pk);
 }
 
 public java.util.Collection findLedgers()throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((CalendarLedgerBMPBean)entity).ejbFindLedgers();
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 }
 
 public CalendarLedger findLedgerByName(String name) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	Object id = ((CalendarLedgerBMPBean)entity).ejbFindLedgerByName(name);
 	this.idoCheckInPooledEntity(entity);
 	return (CalendarLedger) super.findByPrimaryKeyIDO(id);//getEntityCollectionForPrimaryKeys(ids);
 	
 }
 
 public java.util.Collection findLedgersByGroupId(String id)throws javax.ejb.FinderException{
	 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	 	java.util.Collection ids = ((CalendarLedgerBMPBean)entity).ejbFindLedgersByGroupId(id);
	 	this.idoCheckInPooledEntity(entity);
	 	return this.getEntityCollectionForPrimaryKeys(ids);
	 }


public List<CalendarLedger> findLedgersByCoachId(String coachId) throws FinderException {
	if (coachId == null) {
		return null;
	}
	
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((CalendarLedgerBMPBean)entity).ejbFindLedgersByCoachId(coachId);
 	this.idoCheckInPooledEntity(entity);
 	Collection ledgers = this.getEntityCollectionForPrimaryKeys(ids);
 	
 	if (ids == null) {
 		return null;
 	}
 	List<CalendarLedger> ledgersInList = new ArrayList<CalendarLedger>();
 	Object o = null;
 	for (Iterator it = ledgers.iterator(); it.hasNext();) {
 		o = it.next();
 		
 		if (o instanceof CalendarLedger) {
 			ledgersInList.add((CalendarLedger) o);
 		}
 	}
 	return ledgersInList;
}


public List<CalendarLedger> findLedgersByCoachIdAndGroupsIds(String coachId,
		List<String> groupsIds) throws FinderException {
	// TODO Auto-generated method stub
	return null;
}

}