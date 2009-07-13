package com.idega.block.cal.data;


import java.util.Collection;
import com.idega.user.data.User;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CalendarLedger extends IDOEntity {
	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getLedgerID
	 */
	public int getLedgerID();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getDate
	 */
	public Timestamp getDate();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getGroupID
	 */
	public int getGroupID();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getCoachGroupID
	 */
	public int getCoachGroupID();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getUsers
	 */
	public Collection getUsers();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#getCoachID
	 */
	public int getCoachID();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#setDate
	 */
	public void setDate(Timestamp date);

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#setGroupID
	 */
	public void setGroupID(int groupID);

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#setCoachGroupID
	 */
	public void setCoachGroupID(int coachGroupID);

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#addUser
	 */
	public void addUser(User user);

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#setCoachID
	 */
	public void setCoachID(int coachID);

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#removeUserRelation
	 */
	public void removeUserRelation();

	/**
	 * @see com.idega.block.cal.data.CalendarLedgerBMPBean#removeOneUserRelation
	 */
	public void removeOneUserRelation(User user);
}