package com.idega.block.cal.data;

import java.util.Collection;

public interface CalendarEntryTypeHome extends com.idega.data.IDOHome {

	/**
	 * 
	 * @return empty entity created in memory for storage or <code>null</code>
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CalendarEntryType create();

	public CalendarEntryType findByPrimaryKey(Object pk);

	public java.util.Collection findTypeById(int p0)
			throws javax.ejb.FinderException;

	/**
	 * 
	 * @param name is {@link CalendarEntryType#getName()}, 
	 * not <code>null</code>;
	 * @return entities or <code>null</code> on failure;
	 */
	public Collection<CalendarEntryType> findTypesByName(String name);

	public java.util.Collection findTypes() throws javax.ejb.FinderException;

	/**
	 * 
	 * @param entryTypeName is {@link CalendarEntryType#getName()}, 
	 * not <code>null</code>;
	 * @return random entity with name or <code>null</code> on failure;
	 */
	CalendarEntryType findTypeByName(String entryTypeName);

	/**
	 * 
	 * <p>Creates or updates entity</p>
	 * @param name to create or update entity, not <code>null</code>;
	 * @return entity or <code>null</code> on failure;
	 */
	CalendarEntryType update(String name);
}