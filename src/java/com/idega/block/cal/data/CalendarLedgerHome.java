package com.idega.block.cal.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CalendarLedgerHome extends IDOHome {
	public CalendarLedger create() throws CreateException;

	public CalendarLedger findByPrimaryKey(Object pk) throws FinderException;

	public Collection findLedgers() throws FinderException;

	public CalendarLedger findLedgerByName(String name) throws FinderException;

	public Collection findLedgersByGroupId(String id) throws FinderException;

	public Collection findLedgersByCoachIdAndGroupsIds(String coachId,
			Collection groupsIds, Collection coachGroupsIds)
			throws FinderException;
}