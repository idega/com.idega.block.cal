package com.idega.block.cal.data;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOStoreException;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;


public class CalendarEntryTypeHomeImpl extends com.idega.data.IDOFactory implements CalendarEntryTypeHome
{
 protected Class getEntityInterfaceClass(){
  return CalendarEntryType.class;
 }


	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryTypeHome#create()
	 */
	@Override
	public CalendarEntryType create() {
		try {
			return (CalendarEntryType) super.createIDO();
		} catch (CreateException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to create entity " + getEntityInterfaceClass() + 
					" cause of: ", e);
		}

		return null;
	}

public java.util.Collection findTypeById(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryTypeBMPBean)entity).ejbFindTypeById(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryTypeHome#findTypesByName(java.lang.String)
	 */
	@Override
	public Collection<CalendarEntryType> findTypesByName(String name) {
		if (!StringUtil.isEmpty(name)) {
			CalendarEntryTypeBMPBean entity = (CalendarEntryTypeBMPBean) this
					.idoCheckOutPooledEntity();
			Collection<Object> ids = entity.ejbFindTypeByName(name);

			try {
				return getEntityCollectionForPrimaryKeys(ids);
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get " + getEntityInterfaceClass()
								+ " by primary keys: " + ids);
			}
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryTypeHome#getEntryTypeByName(java.lang.String)
	 */
	@Override
	public CalendarEntryType findTypeByName(String entryTypeName) {
		Collection<?> typesByName = findTypesByName(entryTypeName);
		if (ListUtil.isEmpty(typesByName)) {
			for (Object o : typesByName) {
				if (o instanceof CalendarEntryType) {
					return (CalendarEntryType) o;
				}
			}
		}

		return null;
	}

public java.util.Collection findTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CalendarEntryTypeBMPBean)entity).ejbFindTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public CalendarEntryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (CalendarEntryType) super.findByPrimaryKeyIDO(pk);
 }

	
	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntryTypeHome#update(java.lang.String)
	 */
	@Override
	public CalendarEntryType update(String name) {
		CalendarEntryType entity = findTypeByName(name);
		if (entity == null) {
			entity = create();
		}

		if (!StringUtil.isEmpty(name)) {
			entity.setName(name);

			try {
				entity.store();
				return entity;
			} catch(IDOStoreException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to store " + getEntityInterfaceClass() + 
						" cause of: ", e);
			}
		}

		return null;
	}
}