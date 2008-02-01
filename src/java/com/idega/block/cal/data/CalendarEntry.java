package com.idega.block.cal.data;


import java.util.Collection;
import java.util.List;
import com.idega.user.data.User;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CalendarEntry extends IDOEntity {
	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntryID
	 */
	public int getEntryID();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntryTypeID
	 */
	public int getEntryTypeID();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntryType
	 */
	public String getEntryType();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntryTypeName
	 */
	public String getEntryTypeName();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getRepeat
	 */
	public String getRepeat();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getDate
	 */
	public Timestamp getDate();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getDay
	 */
	public int getDay();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEndDate
	 */
	public Timestamp getEndDate();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getUserID
	 */
	public int getUserID();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getGroupID
	 */
	public int getGroupID();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getLedgerID
	 */
	public int getLedgerID();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getLocation
	 */
	public String getLocation();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getUsers
	 */
	public Collection getUsers();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntryGroupID
	 */
	public int getEntryGroupID();

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setEntryTypeID
	 */
	public void setEntryTypeID(int entryTypeID);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setEntryType
	 */
	public void setEntryType(String entryType);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setRepeat
	 */
	public void setRepeat(String repeat);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setDate
	 */
	public void setDate(Timestamp date);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setEndDate
	 */
	public void setEndDate(Timestamp date);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setGroupID
	 */
	public void setGroupID(int groupID);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setLedgerID
	 */
	public void setLedgerID(int ledgerID);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setLocation
	 */
	public void setLocation(String location);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#setEntryGroupID
	 */
	public void setEntryGroupID(int entryGroupID);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#addUser
	 */
	public void addUser(User user);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntriesByEventsIdsAndGroupsIds
	 */
	public Collection getEntriesByEventsIdsAndGroupsIds(List eventsIds,
			List groupsIds);

	/**
	 * @see com.idega.block.cal.data.CalendarEntryBMPBean#getEntriesByLedgersIdsAndGroupsIds
	 */
	public Collection getEntriesByLedgersIdsAndGroupsIds(List ledgersIds,
			List groupsIds);
}