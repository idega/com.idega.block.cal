package com.idega.block.cal.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CalendarLedgerHomeImpl extends IDOFactory implements
		CalendarLedgerHome {
	public Class getEntityInterfaceClass() {
		return CalendarLedger.class;
	}

	public CalendarLedger create() throws CreateException {
		return (CalendarLedger) super.createIDO();
	}

	public CalendarLedger findByPrimaryKey(Object pk) throws FinderException {
		return (CalendarLedger) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findLedgers() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CalendarLedgerBMPBean) entity).ejbFindLedgers();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public CalendarLedger findLedgerByName(String name) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CalendarLedgerBMPBean) entity).ejbFindLedgerByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findLedgersByGroupId(String id) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CalendarLedgerBMPBean) entity)
				.ejbFindLedgersByGroupId(id);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findLedgersByCoachIdAndGroupsIds(String coachId,
			Collection groupsIds, Collection coachGroupsIds)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CalendarLedgerBMPBean) entity)
				.ejbFindLedgersByCoachIdAndGroupsIds(coachId, groupsIds,
						coachGroupsIds);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}