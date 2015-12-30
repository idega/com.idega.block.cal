package com.idega.block.cal.data;

import java.util.Collection;


public interface CalendarEntryGroupHome extends com.idega.data.IDOHome
{
	public CalendarEntryGroup create() throws javax.ejb.CreateException;

	/**
	 * 
	 * @param pk is {@link CalendarEntryGroup#getPrimaryKey()}, not <code>null</code>;
	 * @return entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CalendarEntryGroup findByPrimaryKey(Object pk);

	public Collection findEntryGroupsByLedgerID(int ledgerID)
			throws javax.ejb.FinderException;

	/**
	 * 
	 * <p>Creates or updates entity</p>
	 * @param primaryKey is {@link CalendarEntryGroup#getPrimaryKey()}, updating
	 * is done, when not <code>null</code>;
	 * @param name is name of recurrence, one of:
	 * <li>none</li>
	 * <li>daily</li>
	 * <li>weekly</li>
	 * <li>monthly</li>
	 * <li>yearly</li>
	 * @param ledgerId
	 * @return entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	CalendarEntryGroup update(Integer primaryKey, String name, Integer ledgerId, String timezone);

	/**
	 * 
	 * <p>Removed record with all his children</p>
	 * @param id is {@link CalendarEntryGroup#getPrimaryKey()} to remove, 
	 * not <code>null</code>;
	 * @throws IllegalStateException when system fails to clean data;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	void purge(Integer id) throws IllegalStateException;
}