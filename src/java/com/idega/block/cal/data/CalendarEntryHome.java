package com.idega.block.cal.data;

import java.util.List;


public interface CalendarEntryHome extends com.idega.data.IDOHome
{
 public CalendarEntry create() throws javax.ejb.CreateException;
 public CalendarEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findEntryByTimestamp(java.sql.Timestamp p0)throws javax.ejb.FinderException;
 public java.util.Collection findEntriesBetweenTimestamps(java.sql.Timestamp p0, java.sql.Timestamp p1) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByLedgerID(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByEntryGroupID(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByICGroup(int p0) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByEvents(List eventsList) throws javax.ejb.FinderException;
 public java.util.Collection findEntriesByLedgerId(List listOfLedgerIds) throws javax.ejb.FinderException;
}