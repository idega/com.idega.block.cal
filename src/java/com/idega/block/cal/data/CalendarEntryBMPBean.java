
package com.idega.block.cal.data;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.block.text.data.LocalizedText;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOUtil;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;
import com.idega.user.data.bean.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

public class CalendarEntryBMPBean extends GenericEntity implements com.idega.block.cal.data.CalendarEntry {

	private static final long serialVersionUID = 8762106863386057912L;

	public CalendarEntryBMPBean(){
		super();
	}

	public CalendarEntryBMPBean(int id)throws SQLException{
		super(id);
	}

//  public void insertStartData()throws Exception{
//    CalendarBusiness.initializeCalendarEntry();
//  }

	@Override
	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameName(),"CalEntryName",true,true,String.class);
		addAttribute(getColumnNameEntryTypeID(),"CalEntryType",true,true,Integer.class,"many-to-one",CalendarEntryType.class);
		addAttribute(getColumnNameEntryTypeName(),"CalEntryTypeName",true,true,String.class);
		addAttribute(getColumnNameEntryDate(),"CalEntryDate",true,true,Timestamp.class);
		addAttribute(getColumnNameEntryEndDate(),"CalEntryEndDate",true,true,Timestamp.class);
		addAttribute(getColumnNameGroupID(), "Group", true, true, Integer.class);
		addAttribute(getColumnNameLedgerID(),"CalLedgerID",true,true,Integer.class);
	    addAttribute(getColumnNameRepeat(), "CalEntryRepeat", true, true, String.class);
	    addAttribute(getColumnNameDescription(), "CalEntryDescription",true,true,String.class);
	    addAttribute(getColumnNameLocation(), "CalEntryLocation", true,true,String.class);
	    addAttribute(getColumnNameUserID(), "CalEntryUserID", true, true, Integer.class);
	    addAttribute(getColumnNameEntryGroupID(), "CalEntryGroup", true, true, Integer.class);
	    addAttribute(getColumnNameCalendarId(), "CalendarId", true,true,String.class);
	    addAttribute(getColumnNameExternalEventId(), "CalExtEventId", true,true,String.class);
	    addAttribute(getColumnNameRecurrence(), "CalEventRecurrence", true,true,String.class);
	    addManyToManyRelationShip(CalendarEntryGroup.class);
	    addManyToManyRelationShip(LocalizedText.class);
	    addManyToManyRelationShip(User.class);
	    setNullable(getColumnNameEntryTypeID(),false);
	}

	public static String getEntityTableName() { return "CAL_ENTRY"; }
	public static String getColumnNameEntryID() { return "CAL_ENTRY_ID"; }
	public static String getColumnNameEntryTypeID() { return com.idega.block.cal.data.CalendarEntryTypeBMPBean.getColumnNameCalendarTypeID(); }
	public static String getColumnNameEntryTypeName() {return com.idega.block.cal.data.CalendarEntryTypeBMPBean.getColumnNameName();}
	public static String getColumnNameEntryDate() { return "CAL_ENTRY_DATE"; }
	public static String getColumnNameEntryEndDate() { return "CAL_ENTRY_END_DATE"; }
    public static String getColumnNameUserID(){ return com.idega.user.data.UserBMPBean.getColumnNameUserID();}
	public static String getColumnNameGroupID() { return com.idega.user.data.GroupBMPBean.getColumnNameGroupID(); }
	public static String getColumnNameLedgerID() { return com.idega.block.cal.data.CalendarLedgerBMPBean.getColumnNameLedgerID();}
	public static String getColumnNameName() { return "CAL_ENTRY_NAME"; }
	public static String getColumnNameDescription() { return "CAL_ENTRY_DESCRIPTION"; }
	public static String getColumnNameLocation() { return "CAL_ENTRY_LOCATION"; }
	public static String getColumnNameRepeat() { return "CAL_ENTRY_REPEAT"; }
	public static String getColumnNameCalendarId() { return "CAL_CALENDAR_ID"; }
	public static String getColumnNameExternalEventId() { return "CAL_EXT_EVENT_ID"; }
	public static String getColumnNameRecurrence() { return "CAL_EVENT_RECURRENCE"; }
	public static String getColumnNameEntryGroupID() { return com.idega.block.cal.data.CalendarEntryGroupBMPBean.getColumnNameEntryGroupID(); }

  @Override
	public String getIDColumnName(){
		return getColumnNameEntryID();
	}

	@Override
	public String getEntityName(){
		return getEntityTableName();
	}
	@Override
	public int getEntryID() {
		return getIntColumnValue(getColumnNameEntryID());
	}

	//GET
  @Override
public int getEntryTypeID() {
    return getIntColumnValue(getColumnNameEntryTypeID());
  }
  @Override
public String getEntryType() {
  	return getStringColumnValue(getColumnNameEntryTypeID());
  }
  @Override
public String getEntryTypeName() {
  	return getStringColumnValue(getColumnNameEntryTypeName());
  }
  @Override
public String getRepeat() {
  	return getStringColumnValue(getColumnNameRepeat());
  }

  @Override
public Timestamp getDate(){
		return (Timestamp) getColumnValue(getColumnNameEntryDate());
	}
  @Override
public int getDay() {
  	return getDate().getDate();
  }
	@Override
	public Timestamp getEndDate(){
		return (Timestamp) getColumnValue(getColumnNameEntryEndDate());
	}

  @Override
public int getUserID() {
    return getIntColumnValue(getColumnNameUserID());
  }

  @Override
public int getGroupID() {
    return getIntColumnValue(getColumnNameGroupID());
  }
  @Override
public int getLedgerID() {
  	return getIntColumnValue(getColumnNameLedgerID());
  }
  @Override
	public String getName() {
  	return getStringColumnValue(getColumnNameName());
  }

  @Override
public String getDescription() {
  	return getStringColumnValue(getColumnNameDescription());
  }

  @Override
public String getLocation() {
  	return getStringColumnValue(getColumnNameLocation());
  }

  @Override
public Collection getUsers() {
  	try {
  		return idoGetRelatedEntities(User.class);
  	} catch(IDORelationshipException e) {
  		System.out.println("Couldn't find users for calendar " + toString());
  		e.printStackTrace();
  		return Collections.EMPTY_LIST;
  	}
  }

  @Override
public int getEntryGroupID() {
  	return getIntColumnValue(getColumnNameEntryGroupID());
  }

  @Override
  public String getCalendarId() {
  	return getStringColumnValue(getColumnNameCalendarId());
  }

  @Override
  public String getExternalEventId() {
  	return getStringColumnValue(getColumnNameExternalEventId());
  }

  @Override
  public String getEventRecurrence() {
  	return getStringColumnValue(getColumnNameRecurrence());
  }

  //SET
  @Override
public void setEntryTypeID(int entryTypeID) {
      setColumn(getColumnNameEntryTypeID(),entryTypeID);
  }
  @Override
public void setEntryType(String entryType) {
  	setColumn(getColumnNameEntryTypeName(),entryType);
  }
  @Override
public void setRepeat(String repeat) {
  	setColumn(getColumnNameRepeat(),repeat);
  }

	@Override
	public void setDate(Timestamp date){
			setColumn(getColumnNameEntryDate(), date);
	}

	@Override
	public void setEndDate(Timestamp date){
			setColumn(getColumnNameEntryEndDate(), date);
	}

  @Override
public void setUserID(int userID) {
      setColumn(getColumnNameUserID(),userID);
  }

  @Override
public void setGroupID(int groupID) {
      setColumn(getColumnNameGroupID(),groupID);
  }
  @Override
public void setLedgerID(int ledgerID) {
  	setColumn(getColumnNameLedgerID(),ledgerID);
  }
  @Override
	public void setName(String name) {
  		setColumn(getColumnNameName(), name);
  }

  @Override
public void setDescription(String description) {
  	setColumn(getColumnNameDescription(), description);
  }

  @Override
public void setLocation(String location) {
  	setColumn(getColumnNameLocation(), location);
  }

  @Override
public void setEntryGroupID(int entryGroupID) {
  	setColumn(getColumnNameEntryGroupID(), entryGroupID);
  }

  @Override
  public void setCalendarId(String calendarId) {
  	setColumn(getColumnNameCalendarId(), calendarId);
  }

  @Override
  public void setExternalEventId(String externalEvent) {
  	setColumn(getColumnNameExternalEventId(), externalEvent);
  }

  @Override
  public void setEventRecurrence(String eventRecurrence) {
  	setColumn(getColumnNameRecurrence(), eventRecurrence);
  }

//add a user to the middle table
  public void addUser(User user) {
  	try {
  		idoAddTo(user);
  	} catch(IDOAddRelationshipException e) {
  		System.out.println("Could not add user to entry");
  		e.printStackTrace();
  	}
  }

  //ejbFind...
  public Collection ejbFindEntries() throws FinderException{
  	List result = new ArrayList(super.idoFindAllIDsOrderedBySQL("CAL_ENTRY_NAME"));
  	return result;
  }
  public Collection ejbFindEntryByName(String name) throws FinderException{
  	IDOQuery query = idoQueryGetSelect();
  	query.appendWhereEqualsQuoted("CAL_ENTRY_NAME",name);
  	return super.idoFindPKsByQuery(query);
  }

	/**
	 *
	 * @param externalIds is {@link Collection} of
	 * {@link CalendarEntry#getExternalEventId()}, not <code>null</code>;
	 * @return {@link Collection} of {@link CalendarEntry#getPrimaryKey()}s
	 * or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindEntryByExternalId(
			Collection<String> externalIds) {
		if (!ListUtil.isEmpty(externalIds)) {
			String commaSeparatedList = IDOUtil.getInstance()
					.convertCollectionOfStringsToCommaseparatedString(
							externalIds);

			StringBuilder query = new StringBuilder();
			query.append("SELECT ce.CAL_ENTRY_ID FROM CAL_ENTRY ce ");
			query.append("WHERE ce.CAL_EXT_EVENT_ID IN (")
					.append(commaSeparatedList).append(");");

			try {
				return idoFindPKsBySQL(query.toString());
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get primary keys by query: " + query.toString());
			}
		}

		return Collections.emptyList();
	}

  public Collection ejbFindEntryById(int id) throws FinderException{
  	Collection result = new ArrayList(1);
  	result.add(idoFindOnePKByColumnBySQL(getIDColumnName(),Integer.toString(id)));
  	return result;
  }
  public Collection ejbFindEntryByTimestamp(Timestamp stamp) throws FinderException {
  	//yyyy-mm-dd hh:mm:ss.fffffffff
  	IDOQuery query = idoQueryGetSelect();
  	query.appendWhereEqualsTimestamp("CAL_ENTRY_DATE",stamp);
  	return super.idoFindPKsByQuery(query);
  }
  public Collection ejbFindEntryBetweenTimestamps(Timestamp fromStamp, Timestamp toStamp) throws FinderException{
  	IDOQuery query = idoQueryGetSelect();
  	query.appendWhere();
  	query.appendWithinStamps("CAL_ENTRY_DATE",fromStamp,toStamp);
  	query.appendOrderBy("CAL_ENTRY_DATE");
  	return super.idoFindPKsByQuery(query);
  }
  public Collection ejbFindEntryByLedgerID(int ledgerID) throws FinderException {
  	IDOQuery query = idoQueryGetSelect();
  	query.appendWhereEquals("CAL_LEDGER_ID",ledgerID);
  	return super.idoFindPKsByQuery(query);
  }
  public Collection ejbFindEntryByEntryGroupID(int entryGroupID) throws FinderException {
  	IDOQuery query = idoQueryGetSelect();
  	query.appendWhereEquals("CAL_ENTRY_GROUP_ID",entryGroupID);
  	query.appendOrderBy("CAL_ENTRY_DATE");
  	return super.idoFindPKsByQuery(query);
  }
  public Collection ejbFindEntriesByICGroup(int entryGroupID) throws FinderException {
	  	IDOQuery query = idoQueryGetSelect();
	  	query.appendWhereEquals("IC_GROUP_ID",entryGroupID);
	  	return super.idoFindPKsByQuery(query);
  }

  public Collection ejbFindEntriesByEvents(List eventsList) throws FinderException {
	  	IDOQuery query = idoQueryGetSelect();
	  	query.appendWhereEquals("CAL_TYPE_NAME","\""+eventsList.get(0)+"\"");
	  	for (int i = 1; i < eventsList.size(); i++) {
	  		query.appendOrEquals("CAL_TYPE_NAME", "\""+eventsList.get(i)+"\"");
		}
	  	return super.idoFindPKsByQuery(query);
  }

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.data.CalendarEntry#ejbFindByTypes(java.util.List)
	 */
	@Override
	public Collection<Object> ejbFindByTypes(List<String> calendarEntryTypes) {
		if (!ListUtil.isEmpty(calendarEntryTypes)) {
			String commaSeparatedList = IDOUtil.getInstance()
					.convertCollectionOfStringsToCommaseparatedString(
							calendarEntryTypes);

			StringBuilder query = new StringBuilder();
			query.append("SELECT ce.CAL_ENTRY_ID FROM CAL_ENTRY ce ");
			query.append("WHERE ce.CAL_TYPE_ID IN (")
					.append(commaSeparatedList).append(");");

			try {
				return idoFindPKsBySQL(query.toString());
			} catch (FinderException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING,
						"Failed to get entities by query: '" + query + "'");
			}
		}

		return Collections.emptyList();
	}

  private Collection<CalendarEntry> ejbFindEntriesByEventsIdsAndGroupsIds(List<String> eventsIds, List<String> groupsIds) throws Exception {
	  IDOQuery query = idoQueryGetSelect();

	  query.appendWhere();
	  query.appendLeftParenthesis();
	  for (int i = 0; i < groupsIds.size(); i++) {
		  query.appendEquals(getColumnNameGroupID(), groupsIds.get(i));

		  if ((i + 1) < groupsIds.size()) {
			  query.appendOr();
		  }
	  }

	  query.appendRightParenthesis();
	  query.appendAnd();
	  query.appendLeftParenthesis();

	  for (int i = 0; i < eventsIds.size(); i++) {
		  query.appendEquals(getColumnNameEntryTypeID(), eventsIds.get(i));

		  if ((i + 1) < eventsIds.size()) {
			  query.appendOr();
		  }
	  }
	  query.appendRightParenthesis();

	  return super.idoFindPKsByQuery(query);
  }

  private Collection<CalendarEntry> ejbFindEntriesByLedgerIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds) throws Exception {
	  IDOQuery query = idoQueryGetSelect();

	  query.appendWhere();
	  query.appendLeftParenthesis();
	  for (int i = 0; i < groupsIds.size(); i++) {
		  query.appendEquals(getColumnNameGroupID(), groupsIds.get(i));

		  if ((i + 1) < groupsIds.size()) {
			  query.appendOr();
		  }
	  }

	  query.appendRightParenthesis();
	  query.appendAnd();
	  query.appendLeftParenthesis();

	  for (int i = 0; i < ledgersIds.size(); i++) {
		  query.appendEquals(getColumnNameLedgerID(), ledgersIds.get(i));

		  if ((i + 1) < ledgersIds.size()) {
			  query.appendOr();
		  }
	  }
	  query.appendRightParenthesis();

	  return super.idoFindPKsByQuery(query);
  }

  public Collection<CalendarEntry> ejbFindEntriesByLedgersIds(List<String> ledgersIds) throws Exception {
	  IDOQuery query = idoQueryGetSelect();

	  query.appendWhere();
	  query.append(getColumnNameLedgerID());
	  query.appendInCollection(ledgersIds);

	  return super.idoFindPKsByQuery(query);
  }

  //DELETE
	@Override
	public void delete() throws SQLException{
    removeFrom(GenericEntity.getStaticInstance(LocalizedText.class));
		super.delete();
	}

  public static CalendarEntry getStaticInstance() {
    return (CalendarEntry) GenericEntity.getStaticInstance(CalendarEntry.class);
  }
  public Collection ejbFindEntriesByICGroup(List listOfLedgerIds){
	  if(listOfLedgerIds.isEmpty())
		  return new ArrayList();

	  IDOQuery query = idoQueryGetSelect();

	  query.appendWhereEquals("CAL_LEDGER_ID",listOfLedgerIds.get(0));
	  if(listOfLedgerIds.size() > 1){
		  for(int i = 1; i < listOfLedgerIds.size(); i++){
			  query.appendOr().append("CAL_LEDGER_ID").appendEqualSign().append(listOfLedgerIds.get(i));
		  }
	  }

	  try {
		return super.idoFindPKsByQuery(query);
	} catch (FinderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new ArrayList();
	}
  }

  @Override
public Collection<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(List<String> eventsIds, List<String> groupsIds) {
	  try {
		  return ejbFindEntriesByEventsIdsAndGroupsIds(eventsIds, groupsIds);
	  } catch (Exception e) {
		  e.printStackTrace();
	  }

	  return null;
  }

 public Collection<CalendarEntry> ejbFindEntriesByLedgerIdsOrGroupsIds(List<String> ledgersIds, List groupsIds, Timestamp from, Timestamp to) throws FinderException {
	  Table table = new Table(this);
	  SelectQuery query = new SelectQuery(table);
	  query.addColumn(new Column(table, getIDColumnName()));

	  boolean useLedgers = !ListUtil.isEmpty(ledgersIds);
	  boolean useGroups = !ListUtil.isEmpty(groupsIds);

	  InCriteria ledIn = useLedgers ? new InCriteria(new Column(table, getColumnNameLedgerID()), ledgersIds) : null;
	  InCriteria grpIn = useGroups ? new InCriteria(new Column(table, getColumnNameGroupID()), groupsIds) : null;

	  if (useLedgers && useGroups) {
		  OR or = new OR(ledIn, grpIn);
		  query.addCriteria(or);
	  } else if (useLedgers) {
		  query.addCriteria(ledIn);
	  } else if (useGroups) {
		  query.addCriteria(grpIn);
	  }
	  Column dateCol = new Column(table, "CAL_ENTRY_DATE");
	  query.addCriteria(new MatchCriteria(dateCol, MatchCriteria.GREATEREQUAL, from));
	  query.addCriteria(new MatchCriteria(dateCol, MatchCriteria.LESSEQUAL, to));

	  Order order = new Order(dateCol, true);
	  query.addOrder(order);

	  return this.idoFindPKsByQuery(query);
  }

  @Override
public Collection<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds) {
	try {
		  return ejbFindEntriesByLedgerIdsAndGroupsIds(ledgersIds, groupsIds);
	  } catch (Exception e) {
		  e.printStackTrace();
	  }

	  return null;
  }

  @Override
  public Collection<Object> ejbFindEntriesByCriteria(String calendarId, List<String> groupsIds, List<String> userIds, Timestamp from, Timestamp to) throws FinderException {
	  Table table = new Table(this);
	  SelectQuery query = new SelectQuery(table);
	  query.addColumn(new Column(table, getIDColumnName()));

	  boolean useGroups = !ListUtil.isEmpty(groupsIds);
	  boolean useUsers = !ListUtil.isEmpty(userIds);

	  InCriteria groupsIn = useGroups ? new InCriteria(new Column(table, getColumnNameGroupID()), groupsIds) : null;
	  InCriteria usersIn = useUsers ? new InCriteria(new Column(table, getColumnNameUserID()), userIds) : null;

	  if (useGroups) {
		  query.addCriteria(groupsIn);
	  }
	  if (useUsers) {
		  query.addCriteria(usersIn);
	  }

	  Column calendarIdCol = new Column(table, getColumnNameCalendarId());
	  if (!StringUtil.isEmpty(calendarId)) {
		  query.addCriteria(new MatchCriteria(calendarIdCol, MatchCriteria.EQUALS, calendarId));
	  }

	  Column dateCol = new Column(table, getColumnNameEntryDate());
	  if (from != null) {
		  query.addCriteria(new MatchCriteria(dateCol, MatchCriteria.GREATEREQUAL, from));
	  }
	  if (to != null) {
		  query.addCriteria(new MatchCriteria(dateCol, MatchCriteria.LESSEQUAL, to));
	  }

	  Order order = new Order(dateCol, true);
	  query.addOrder(order);

	  return this.idoFindPKsByQuery(query);
  }

	/**
	 *
	 * @param calendarId, skipped if <code>null</code>;
	 * @param groupsIds is {@link Collection} of {@link Group#getId()},
	 * skipped if <code>null</code>;
	 * @param userIds is {@link Collection} of {@link User#getId()},
	 * skipped if <code>null</code>;
	 * @param from is start {@link Date} of event, skipped if <code>null</code>;
	 * @param to is end {@link Date} of event, skipped if <code>null</code>;
	 * @param extendedResultSet when <code>true</code> events,
	 * which are already happening will be included or which going to end later,
	 * but it going to start today;
	 * @return {@link Collection} of {@link CalendarEntry#getPrimaryKey()} or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindBy(
			String calendarId,
			List<String> groupsIds,
			Integer eventTypeId,
			List<String> userIds,
			Timestamp from,
			Timestamp to,
			boolean extendedResultSet) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ce.CAL_ENTRY_ID FROM CAL_ENTRY ce ");

		/*
		 * Lazy bones
		 */
		query.append("WHERE ce.CAL_CALENDAR_ID IS NOT NULL ");

		/*
		 * Calendar id
		 */
		if (!StringUtil.isEmpty(calendarId)) {
			query.append("AND ce.CAL_CALENDAR_ID = '").append(calendarId).append("' ");
		}

		/*
		 * Group id
		 */
		if (!ListUtil.isEmpty(groupsIds)) {
			String commaSeparatedList = IDOUtil.getInstance()
					.convertCollectionOfStringsToCommaseparatedString(groupsIds);
			query.append("AND ce.IC_GROUP_ID IN (").append(commaSeparatedList).append(") ");
		}

		if (eventTypeId != null) {
			query.append("AND ce.").append(com.idega.block.cal.data.CalendarEntryTypeBMPBean.getColumnNameCalendarTypeID()).append(" = ").append(eventTypeId).append(" ");
		}

		/*
		 * User id
		 */
		if (!ListUtil.isEmpty(userIds)) {
			String commaSeparatedList = IDOUtil.getInstance()
					.convertCollectionOfStringsToCommaseparatedString(userIds);
			query.append("AND ce.IC_USER_ID IN (").append(commaSeparatedList).append(") ");
		}

		/*
		 * Date from
		 */
		if (from != null) {
			IWTimestamp fromTimestamp = new IWTimestamp(from);
			query.append("AND (ce.CAL_ENTRY_DATE >= '").append(fromTimestamp.toSQLString()).append("' ");
			if (extendedResultSet) {
				query.append("OR (");
				query.append("ce.CAL_ENTRY_DATE < '").append(fromTimestamp.toSQLString());
				query.append("' AND ");
				query.append("ce.CAL_ENTRY_END_DATE > '").append(fromTimestamp.toSQLString());
				query.append("')");
			}

			query.append(") ");
		}

		/*
		 * Date to
		 */
		if (to != null) {
			IWTimestamp toTimestamp = new IWTimestamp(to);
			query.append("AND (ce.CAL_ENTRY_END_DATE <= '").append(toTimestamp.toSQLString()).append("' ");
			if (extendedResultSet) {
				query.append("OR (");
				query.append("ce.CAL_ENTRY_END_DATE > '").append(toTimestamp.toSQLString());
				query.append("' AND ");
				query.append("ce.CAL_ENTRY_DATE < '").append(toTimestamp.toSQLString());
				query.append("')");
			}

			query.append(") ");
		}

		/*
		 * Order
		 */
		query.append("ORDER BY ce.CAL_ENTRY_DATE; ");

		try {
			return idoFindPKsBySQL(query.toString());
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"Failed to get results by query: '" + query.toString() + "'");
		}

		return Collections.emptyList();
	}
}