package com.idega.block.cal.data;

import java.util.List;


public interface CalendarLedgerHome extends com.idega.data.IDOHome
{
 public CalendarLedger create() throws javax.ejb.CreateException;
 public CalendarLedger findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findLedgers()throws javax.ejb.FinderException;
 public CalendarLedger findLedgerByName(String name) throws javax.ejb.FinderException;
 public java.util.Collection findLedgersByGroupId(String id)throws javax.ejb.FinderException;

 public List findLedgersByCoachId(String coachId) throws javax.ejb.FinderException;
 
 public List findLedgersByCoachIdAndGroupsIds(String coachId, List groupsIds) throws javax.ejb.FinderException;
}