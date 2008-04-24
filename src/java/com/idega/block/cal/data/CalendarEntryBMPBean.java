
package com.idega.block.cal.data;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.text.data.LocalizedText;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;

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
	public static String getColumnNameEntryGroupID() { return com.idega.block.cal.data.CalendarEntryGroupBMPBean.getColumnNameEntryGroupID(); }
	
  @Override
	public String getIDColumnName(){
		return getColumnNameEntryID();
	}
  
	@Override
	public String getEntityName(){
		return getEntityTableName();
	}
	public int getEntryID() {
		return getIntColumnValue(getColumnNameEntryID());
	}

	//GET
  public int getEntryTypeID() {
    return getIntColumnValue(getColumnNameEntryTypeID());
  }
  public String getEntryType() {
  	return getStringColumnValue(getColumnNameEntryTypeID());
  }
  public String getEntryTypeName() {
  	return getStringColumnValue(getColumnNameEntryTypeName());
  }
  public String getRepeat() {
  	return getStringColumnValue(getColumnNameRepeat());
  }

  public Timestamp getDate(){
		return (Timestamp) getColumnValue(getColumnNameEntryDate());
	}
  public int getDay() {
  	return getDate().getDate();
  }
	public Timestamp getEndDate(){
		return (Timestamp) getColumnValue(getColumnNameEntryEndDate());
	}

  public int getUserID() {
    return getIntColumnValue(getColumnNameUserID());
  }

  public int getGroupID() {
    return getIntColumnValue(getColumnNameGroupID());
  }
  public int getLedgerID() {
  	return getIntColumnValue(getColumnNameLedgerID());
  }
  @Override
	public String getName() {
  	return getStringColumnValue(getColumnNameName());
  }
  
  public String getDescription() {
  	return getStringColumnValue(getColumnNameDescription());
  }
  
  public String getLocation() {
  	return getStringColumnValue(getColumnNameLocation());
  }
    
  public Collection getUsers() {
  	try {
  		return idoGetRelatedEntities(User.class);
  	} catch(IDORelationshipException e) {
  		System.out.println("Couldn't find users for calendar " + toString());
  		e.printStackTrace();
  		return Collections.EMPTY_LIST;
  	}
  }
  
  public int getEntryGroupID() {
  	return getIntColumnValue(getColumnNameEntryGroupID());
  }

  //SET
  public void setEntryTypeID(int entryTypeID) {
      setColumn(getColumnNameEntryTypeID(),entryTypeID);
  }
  public void setEntryType(String entryType) {
  	setColumn(getColumnNameEntryTypeName(),entryType);
  }
  public void setRepeat(String repeat) {
  	setColumn(getColumnNameRepeat(),repeat);
  }

	public void setDate(Timestamp date){
			setColumn(getColumnNameEntryDate(), date);
	}

	public void setEndDate(Timestamp date){
			setColumn(getColumnNameEntryEndDate(), date);
	}

  public void setUserID(int userID) {
      setColumn(getColumnNameUserID(),userID);
  }

  public void setGroupID(int groupID) {
      setColumn(getColumnNameGroupID(),groupID);
  }
  public void setLedgerID(int ledgerID) {
  	setColumn(getColumnNameLedgerID(),ledgerID);
  }
  @Override
	public void setName(String name) {
  		setColumn(getColumnNameName(), name);
  }
  
  public void setDescription(String description) {
  	setColumn(getColumnNameDescription(), description);
  }
  
  public void setLocation(String location) {
  	setColumn(getColumnNameLocation(), location);
  }
  
  public void setEntryGroupID(int entryGroupID) {
  	setColumn(getColumnNameEntryGroupID(), entryGroupID);
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
  
  public Collection<CalendarEntry> getEntriesByEventsIds(List<String> eventsIds) {
	  try {
		return ejbFindEntriesByEventsIds(eventsIds);
	  } catch (Exception e) {
		e.printStackTrace();
		return null;
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

  private Collection<CalendarEntry> ejbFindEntriesByEventsIds(List<String> eventsIds) throws Exception {
	  	IDOQuery query = idoQueryGetSelect();	  	
	  	query.appendWhereEquals(getColumnNameEntryTypeID(), eventsIds.get(0));
	  	for (int i = 1; i < eventsIds.size(); i++) {
	  		query.appendOrEquals(getColumnNameEntryTypeID(), eventsIds.get(i));
		}
	  	return super.idoFindPKsByQuery(query);
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
	  
	  InCriteria ledIn = new InCriteria(new Column(table, getColumnNameLedgerID()), ledgersIds);
	  InCriteria grpIn = new InCriteria(new Column(table, getColumnNameGroupID()), groupsIds);

	  boolean useLedgers = ledgersIds != null && !ledgersIds.isEmpty();
	  boolean useGroups = ledgersIds != null && !ledgersIds.isEmpty();
	  
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

  public Collection<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds) {
	try {
		  return ejbFindEntriesByLedgerIdsAndGroupsIds(ledgersIds, groupsIds);
	  } catch (Exception e) {
		  e.printStackTrace();
	  }

	  return null;
  }
}