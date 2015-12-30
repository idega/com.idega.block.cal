package com.idega.block.cal.data;

import java.util.TimeZone;


public interface CalendarEntryGroup extends com.idega.data.IDOEntity {
	
	public int getEntryGroupID();

	/**
	 * @return one of:
	 * <li>none</li>
	 * <li>daily</li>
	 * <li>weekly</li>
	 * <li>monthly</li>
	 * <li>yearly</li>
	 */
	String getName();
	public int getLedgerID();

	/**
	 * 
	 * @param name one of:
	 * <li>none</li>
	 * <li>daily</li>
	 * <li>weekly</li>
	 * <li>monthly</li>
	 * <li>yearly</li>
	 */
	void setName(String name);
	public void setLedgerID(int ledgerID);
	public void addEntry(CalendarEntry entry);
	public void removeEntryRelation();
	public void removeOneEntryRelation(CalendarEntry entry);

	/**
	 * @return {@link TimeZone#getID()} when entry was created;
	 */
	String getTimezone();

	/**
	 * @param name is {@link TimeZone#getID()} when entry was created;
	 */
	void setTimezone(String name);
}
