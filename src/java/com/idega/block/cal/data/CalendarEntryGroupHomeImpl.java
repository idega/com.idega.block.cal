package com.idega.block.cal.data;

import java.util.Collection;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOStoreException;



public class CalendarEntryGroupHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryGroupHome {

	private static final long serialVersionUID = -3379199403679533775L;

	@Override
	public CalendarEntryGroup update(Integer primaryKey, String name, Integer ledgerId) {
		CalendarEntryGroup entity = findByPrimaryKey(primaryKey);
		if (entity == null) {
			entity = create();
		}

		entity.setName(name);

		if (ledgerId != null && ledgerId > -1) {
			entity.setLedgerID(ledgerId);
		}
		
		try {
			entity.store();
			return entity;
		} catch (IDOStoreException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Failed to store " + getEntityInterfaceClass() 
							+ " cause of: ", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.IDOFactory#getEntityInterfaceClass()
	 */
	protected Class getEntityInterfaceClass() {
		return CalendarEntryGroup.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryGroupHome#create()
	 */
	public CalendarEntryGroup create() {
		try {
			return (CalendarEntryGroup) super.createIDO();
		} catch (CreateException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Failed to create " + getEntityInterfaceClass() 
							+ " cause of: ", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryGroupHome#findByPrimaryKey(java.lang.Object)
	 */
	public CalendarEntryGroup findByPrimaryKey(Object pk){
		if (pk != null) {
			try {
				return (CalendarEntryGroup) super.findByPrimaryKeyIDO(pk);
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, "Failed to get " + getEntityInterfaceClass() 
								+ "by primary key: " + pk);
			}
		}

		return null;
	}

 public Collection findEntryGroupsByLedgerID(int ledgerID) throws javax.ejb.FinderException{
 	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
 	java.util.Collection ids = ((CalendarEntryGroupBMPBean)entity).ejbFindEntryGroupByLedgerID(ledgerID);
 	this.idoCheckInPooledEntity(entity);
 	return this.getEntityCollectionForPrimaryKeys(ids);
 	
 	
 }



}