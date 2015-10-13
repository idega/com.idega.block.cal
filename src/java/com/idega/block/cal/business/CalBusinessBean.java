/*
 * Created on Feb 4, 2004
 */
package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceEntityHome;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.AttendanceMarkHome;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryGroup;
import com.idega.block.cal.data.CalendarEntryGroupHome;
import com.idega.block.cal.data.CalendarEntryHome;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarEntryTypeHome;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.cal.data.CalendarLedgerHome;
import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.block.cal.presentation.CalendarWindowPlugin;
import com.idega.block.category.business.CategoryBusiness;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.CoreUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author
 */
public class CalBusinessBean extends IBOServiceBean implements CalBusiness,UserGroupPlugInBusiness{

	private static final long serialVersionUID = 3817371057903141346L;
	public static final String ROLE_LEDGER_ADMIN = "ledger_admin";
	//GET methods for Entries
	/**
	 * @return a calendar entry with the specific entryID
	 */
	@Override
	public CalendarEntry getEntry(int entryID) {
		CalendarEntry entry = null;
		Integer id = new Integer(entryID);
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			entry = entryHome.findByPrimaryKey(id);
		} catch(FinderException e) {
			entry = null;
		} catch(RemoteException re) {
			re.printStackTrace();
		}
		return entry;
	}

	/**
	 * @return A calendar entry with the specific external entry id
	 */
	@Override
	public CalendarEntry getEntryByExternalId(String externalEntryID) {
		CalendarEntry entry = null;
		try {
			if (!StringUtil.isEmpty(externalEntryID)) {
				CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
				Collection<CalendarEntry> entryList = entryHome.findEntryByExternalId(externalEntryID);
				if (entryList != null && !entryList.isEmpty()) {
					Iterator<CalendarEntry> iter = entryList.iterator();
					entry = iter.next();
				}
			}
		} catch(FinderException e) {
			entry = null;
		} catch(RemoteException re) {
			re.printStackTrace();
		}
		return entry;
	}

	/**
	 *
	 */
	@Override
	public Collection getEntriesByTimestamp(Timestamp stamp) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntryByTimestamp(stamp));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<CalendarEntry> getUserEntriesBetweenTimestamps(User user, Timestamp fromStamp, Timestamp toStamp, IWContext iwc) {
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			List<CalendarLedger> ledgers = getUserLedgers(user, iwc);
			List<String> ledgersIds = new ArrayList<String>();
			if (ledgers != null) {
				Iterator<CalendarLedger> ledgerIter = ledgers.iterator();
				while (ledgerIter.hasNext()) {
					ledgersIds.add((ledgerIter.next()).getPrimaryKey().toString());
				}
			}
			List<String> groupsIds = getUserBusiness(iwc).getAllUserGroupsIds(user, iwc);
			return new ArrayList<CalendarEntry>(entryHome.findEntriesByLedgerIdsOrGroupsIds(ledgersIds, groupsIds, fromStamp,toStamp));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	@Override
	public Collection getEntriesBetweenTimestamps(Timestamp fromStamp, Timestamp toStamp) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesBetweenTimestamps(fromStamp,toStamp));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Collection getEntriesByLedgerID(int ledgerID) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByLedgerID(ledgerID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Collection getEntriesByEntryGroupID(int entryGroupID) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByEntryGroupID(entryGroupID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Collection getPracticesByLedgerID(int ledgerID) {
		List list = new ArrayList();
		List entries = (List) getEntriesByLedgerID(ledgerID);
		Iterator entryIter = entries.iterator();
		while(entryIter.hasNext()) {
			CalendarEntry entry = (CalendarEntry) entryIter.next();
			if(entry.getEntryTypeName().equals("practice")) {
				list.add(entry);
			}
		}
		return list;
	}
	@Override
	public Collection getMarkedEntriesByUserIDandLedgerID(int userID,int ledgerID) {
		List list = new ArrayList();
		List entries = (List) getPracticesByLedgerID(ledgerID);
		Iterator entIter = entries.iterator();
		while(entIter.hasNext()) {
			CalendarEntry entry = (CalendarEntry) entIter.next();
			AttendanceEntity attendance = getAttendanceByUserIDandEntry(userID,entry);
			if(attendance != null) {
				if(attendance.getAttendanceMark() != null && !attendance.getAttendanceMark().equals("")) {
					list.add(attendance);
				}
			}

		}
		return list;
	}
	@Override
	public Collection getPracticesByLedIDandMonth(int ledgerID, int month, int year) {
		List list = new ArrayList();
		List practices = (List) getPracticesByLedgerID(ledgerID);
		Iterator prIter = practices.iterator();
		while(prIter.hasNext()) {
			CalendarEntry entry = (CalendarEntry) prIter.next();
			Timestamp entryDate = entry.getDate();
			IWTimestamp i = new IWTimestamp(entryDate);
			int m = i.getMonth();
			int y = i.getYear();
			if(m == month && y == year) {
				list.add(entry);
			}
		}
		return list;
	}
	//GET methods for EntryGroup
	@Override
	public CalendarEntryGroup getEntryGroup(int entryGroupID) {

		CalendarEntryGroup entryGroup = null;
		Integer id = new Integer(entryGroupID);
		try {
			CalendarEntryGroupHome entryGroupHome = (CalendarEntryGroupHome) getIDOHome(CalendarEntryGroup.class);
			entryGroup = entryGroupHome.findByPrimaryKey(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return entryGroup;
	}

	@Override
	public Collection getEntryGroupsByLedgerID(int ledgerID) {
		List list = null;
		try {
			CalendarEntryGroupHome entryGroupHome = (CalendarEntryGroupHome) getIDOHome(CalendarEntryGroup.class);
			list = new ArrayList(entryGroupHome.findEntryGroupsByLedgerID(ledgerID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;


	}

	private CalendarEntryTypeHome calendarEntryTypeHome = null;

	/**
	 * FIXME document this
	 * @return
	 * @author <a href="mailto:martynas@idega.com">Martynas Stakė</a>
	 */
	protected CalendarEntryTypeHome getCalendarEntryTypeHome() {
		if (this.calendarEntryTypeHome == null) {
			try {
				this.calendarEntryTypeHome = (CalendarEntryTypeHome) getIDOHome(
						CalendarEntryType.class);
			} catch (RemoteException e) {
				getLogger().log(Level.WARNING, "Unable to find "
						+ CalendarEntryTypeHome.class + ": ", e);
			}
		}

		return this.calendarEntryTypeHome;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.business.CalBusiness#getEntryTypeByName(
	 * 		java.lang.String
	 * )
	 */
	@Override
	public CalendarEntryType getEntryTypeByName(String entryTypeName) {
		if (StringUtil.isEmpty(entryTypeName)) {
			return null;
		}

		Collection<?> typesByName = null;
		try {
			typesByName = getCalendarEntryTypeHome()
					.findTypeByName(entryTypeName);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING,
					"Unable to find such type '" +
					entryTypeName + "' cause of: ", e);
		}

		if (ListUtil.isEmpty(typesByName)) {
			return null;
		}

		for (Object o : typesByName) {
			if (o instanceof CalendarEntryType) {
				return (CalendarEntryType) o;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.business.CalBusiness#getAllEntryTypes()
	 */
	@Override
	public List<CalendarEntryType> getAllEntryTypes() {
		Collection<?> types = null;
		try {
			types = getCalendarEntryTypeHome().findTypes();
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, "Unable to find " +
					CalendarEntryType.class + "'s, cause of: ", e);
		}

		if (ListUtil.isEmpty(types)) {
			return null;
		}

		List<CalendarEntryType> parametrizedTypes =
				new ArrayList<CalendarEntryType>(types.size());
		for (Object type : types) {
			if (type instanceof CalendarEntryType) {
				parametrizedTypes.add((CalendarEntryType) type);
			}
		}

		return parametrizedTypes;
	}

	//GET methods for Ledger
	/**
	 *
	 * @param entryID
	 * @return
	 */
	@Override
	public CalendarLedger getLedger(int ledgerID) {
		CalendarLedger ledger = null;
		Integer id = new Integer(ledgerID);
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledger = ledgerHome.findByPrimaryKey(id);
		} catch(Exception e) {
			ledger = null;
		}
		return ledger;
	}

	@Override
	public int getLedgerIDByName(String name) {

		CalendarLedger ledger =null;
		int ledgerID = 0;
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledger = ledgerHome.findLedgerByName(name);
			ledgerID = ledger.getLedgerID();
			return ledgerID;

		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 *
	 */
	@Override
	public List getAllLedgers() {
		List ledgers = null;
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledgers = new ArrayList(ledgerHome.findLedgers());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ledgers;
	}
	//GET methods for Attendance
	@Override
	public AttendanceEntity getAttendanceEntity(int attendanceID) {
		AttendanceEntity attendance = null;
		Integer id = new Integer(attendanceID);
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			attendance = attendanceHome.findByPrimaryKey(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return attendance;
	}
	@Override
	public AttendanceEntity getAttendanceByUserIDandEntry(int userID, CalendarEntry entry) {
		AttendanceEntity attendance = null;
//		Timestamp stamp = entry.getDate();
		int entryID = entry.getEntryID();
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			attendance = attendanceHome.findAttendanceByUserIDandEntryID(userID,entryID);
		} catch(Exception e) {
			attendance = null;
		}
		return attendance;

	}
	@Override
	public List getAttendancesByLedgerID(int ledgerID) {

		List attendances = null;
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			attendances = new ArrayList(attendanceHome.findAttendancesByLedgerID(ledgerID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return attendances;
	}
	@Override
	public int getNumberOfPractices(int ledgerID) {
		List attendances = getAttendancesByLedgerID(ledgerID);
		return attendances.size();
	}
	@Override
	public List getAttendanceMarks(int userID, int ledgerID, String mark) {
		List marks = null;
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			marks = new ArrayList(attendanceHome.findAttendanceByMark(userID,ledgerID,mark));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return marks;
	}
	//get methods for marks
	@Override
	public List getAllMarks() {

		List marks = null;
		try {
			AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
			marks = new ArrayList(markHome.findMarks());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return marks;
	}
	@Override
	public AttendanceMark getMark(int markID) {
		AttendanceMark mark = null;
		Integer mID = new Integer(markID);
		try {
			AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
			mark = markHome.findByPrimaryKey(mID);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mark;
	}

	@Override
	public void deleteEntry(int entryID) {
		CalendarEntry entry = getEntry(entryID);
		int entryGroupID = entry.getEntryGroupID();
		CalendarEntryGroup entryGroup = getEntryGroup(entryGroupID);
		if (entry != null) {
			try {
				entryGroup.removeOneEntryRelation(entry);
				entry.remove();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	@Override
	public void deleteEntryGroup(int entryGroupID) {
		CalendarEntryGroup entryGroup = getEntryGroup(entryGroupID);
		try {
			entryGroup.removeEntryRelation();
			entryGroup.remove();
		}catch (Exception e) {
			e.printStackTrace();
		}
		Collection entries = getEntriesByEntryGroupID(entryGroupID);
		Iterator entIter = entries.iterator();
		while(entIter.hasNext()) {
			CalendarEntry e = (CalendarEntry) entIter.next();
			try {
				e.remove();
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	@Override
	public void deleteEntryGroupByEntryID(int entryID) {
		CalendarEntry entry = getEntry(entryID);
		if(entry != null) {
			deleteEntryGroup(entry.getEntryGroupID());
		}
	}
	@Override
	public void deleteLedger(int ledgerID) {
		CalendarLedger led = getLedger(ledgerID);
		if(led != null) {
			/*
			 * get all the attendance markings for this ledger and delete them
			 */
			Collection att = getAttendancesByLedgerID(ledgerID);
			Iterator attIter = att.iterator();
			while(attIter.hasNext()) {
				AttendanceEntity attendance = (AttendanceEntity) attIter.next();
				try {
					attendance.remove();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			/*
			 * get all entryGroups for this ledger and delete them
			 */
//			Collection entries = getEntriesByLedgerID(ledgerID);
			Collection entryGroups = getEntryGroupsByLedgerID(ledgerID);
			Iterator entIter = entryGroups.iterator();
			while(entIter.hasNext()) {
				CalendarEntryGroup entryGroup = (CalendarEntryGroup) entIter.next();
				deleteEntryGroup(entryGroup.getEntryGroupID());
			}
			try {
				led.removeUserRelation();
				led.remove();
			}catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}



	@Override
	public void deleteUserFromLedger(int userID, int ledgerID, IWContext iwc) {
		UserBusiness userBiz = getUserBusiness(iwc);
		//GroupBusiness groupBiz = getGroupBusiness(iwc);
		CalendarLedger ledger = getLedger(ledgerID);
		User user = null;
		try{
			user = 	userBiz.getUser(userID);
		}catch (Exception e) {

		}
		if(user !=null) {
			Collection att = getAttendancesByLedgerID(ledgerID);
			Iterator attIter = att.iterator();
			while(attIter.hasNext()) {
				AttendanceEntity attendance = (AttendanceEntity) attIter.next();
				if(attendance.getUserID() == userID) {
					try {
						attendance.remove();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			try {
				ledger.removeOneUserRelation(user);
			}catch (Exception e) {
				e.printStackTrace();
			}
			//try {
			//	userBiz.removeUserFromGroup(user,groupBiz.getGroupByGroupID(ledger.getGroupID()),iwc.getCurrentUser());
			//}catch (Exception e) {
			//	e.printStackTrace();
			//}
			//This delete was preformed weather the user had the privileges or not

		}
	}
	@Override
	public void deleteMark(int markID) {
		AttendanceMark mark = getMark(markID);
		if (mark != null) {
			try {
				mark.remove();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.cal.business.CalBusiness#createNewEntryType(
	 * 		java.lang.String
	 * )
	 */
	@Override
	public boolean createNewEntryType(String typeName) {
		if (StringUtil.isEmpty(typeName)) {
			return Boolean.FALSE;
		}

		List<CalendarEntryType> types = getAllEntryTypes();
		if (!ListUtil.isEmpty(types)) {
			for(Iterator<CalendarEntryType> typeIter = types.iterator(); typeIter.hasNext();) {
				CalendarEntryType type = typeIter.next();
				if(type.getName().equals(typeName)) {
					return Boolean.FALSE;
				}
			}
		}

		CalendarEntryType type = null;
		try {
			type = getCalendarEntryTypeHome().create();
			type.setName(typeName);
			type.store();
		} catch(Exception e) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * startDate and endDate have to be of the form  yyyy-MM-dd hh:mm:ss.S
	 */
	@Override
	public void createNewEntry(String headline, User user, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String ledger, String description, String location) {
		createNewEntry(headline,user, type, repeat, startDate, startHour, startMinute, endDate, endHour, endMinute, attendees, ledger, description, location, null);
	}

	/**
	 * startDate and endDate have to be of the form  yyyy-MM-dd hh:mm:ss.S
	 */
	@Override
	public void createNewEntry(String headline, User user, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String ledger, String description, String location, String recurrence) {
		CalendarEntryGroup entryGroup = null;
//		if(repeat != null && !repeat.equals("")) {
			try {
				CalendarEntryGroupHome entryGroupHome = (CalendarEntryGroupHome) getIDOHome(CalendarEntryGroup.class);
				entryGroup = entryGroupHome.create();
				entryGroup.setName(repeat);
				if(Integer.parseInt(ledger) != -1) {
					entryGroup.setLedgerID(Integer.parseInt(ledger));
				}
				entryGroup.store();
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
			IWTimestamp st = new IWTimestamp(startDate);
		Timestamp startTime = st.getTimestamp();
		//modifications of the time properties of the start timestamp
		if(startHour != null && !startHour.equals("")) {
			Integer sH =new Integer(startHour);
			startTime.setHours(sH.intValue());
		}
		if(startMinute != null && !startMinute.equals("")) {
			Integer sM = new Integer(startMinute);
			startTime.setMinutes(sM.intValue());
		}
		startTime.setSeconds(0);
//		startTime.setNanos(0);
		IWTimestamp et = new IWTimestamp(endDate);
		Timestamp endTime = et.getTimestamp();
		//modifications of the time properties of the end timestamp
		if(endHour != null && !endHour.equals("")) {
			Integer eH =new Integer(endHour);
			endTime.setHours(eH.intValue());
		}
		if(endMinute != null && !endMinute.equals("")) {
			Integer eM =new Integer(endMinute);
			endTime.setMinutes(eM.intValue());
		}
		endTime.setSeconds(0);
//		endTime.setNanos(0);

		GregorianCalendar startCal = new GregorianCalendar(startTime.getYear(),startTime.getMonth(),startTime.getDate(),startTime.getHours(),startTime.getMinutes());
		GregorianCalendar endCal = new GregorianCalendar(endTime.getYear(),endTime.getMonth(),endTime.getDate(),endTime.getHours(),endTime.getMinutes());

		long start = startCal.getTimeInMillis();
		long end = endCal.getTimeInMillis();

		long year365 = 365L*24L*60L*60L*1000L;
		long year366 = 366L*24L*60L*60L*1000L;
		long month31 = 31L*24L*60L*60L*1000L;
		long month30 = 30L*24L*60L*60L*1000L;
		long month29 = 29L*24L*60L*60L*1000L;
		long month28 = 28L*24L*60L*60L*1000L;
		long week = 7L*24L*60L*60L*1000L;
		long day = 24L*60L*60L*1000L;

		Integer groupID = null;
		if(attendees != null && !attendees.equals("")) {
			groupID = new Integer(attendees);
		}
		else {
			groupID = new Integer(-1);
		}
		Integer userID = null;
		if(user != null) {
			userID = (Integer) user.getPrimaryKey();
		}
		else {
			userID = new Integer(-1);
		}
		while(start < end) {
			Timestamp endOfEntryTime = Timestamp.valueOf(startTime.toString());
			if(endHour != null && !endHour.equals("")) {
				Integer eH =new Integer(endHour);
				endOfEntryTime.setHours(eH.intValue());
			}
			if(endMinute != null && !endMinute.equals("")) {
				Integer eM =new Integer(endMinute);
				endOfEntryTime.setMinutes(eM.intValue());
			}
			endOfEntryTime.setSeconds(0);


			try {
				CalendarEntryType entryType = getEntryTypeByName(type);
				Integer entryTypePK = (Integer) entryType.getPrimaryKey();
				CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
				CalendarEntry entry = entryHome.create();
				entry.setName(headline);
				entry.setUserID(userID.intValue());
				entry.setEntryTypeID(entryTypePK.intValue());
				entry.setEntryType(entryType.getName());
				entry.setRepeat(repeat);
				entry.setDate(startTime);
				entry.setEndDate(endOfEntryTime);
				if(groupID != null) {
					entry.setGroupID(groupID.intValue());
				}
				if(Integer.parseInt(ledger) != -1) {
					entry.setLedgerID(Integer.parseInt(ledger));
				}
				entry.setDescription(description);
				entry.setLocation(location);
				entry.store();
				if(entryGroup !=null) {
					entryGroup.addEntry(entry);
					entry.setEntryGroupID(entryGroup.getEntryGroupID());
					entry.store();
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
			if(repeat.equals(CalendarEntryCreator.yearlyFieldParameterName)) {
				if(startTime.getYear()%4 == 0) {
					start += year366;
				}
				else {
					start += year365; //start up one year = 31536000000 milliseconds
				}
				startCal.set(startTime.getYear()+1,startTime.getMonth(),startTime.getDate());

			}

			else if(repeat.equals(CalendarEntryCreator.monthlyFieldParameterName)) {
				//if December
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add 1 to the year and set the month to January
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,startTime.getDate());
				}
				else {
					int month = startTime.getMonth();
					//if the month has 31 days
					if(month == Calendar.JANUARY ||
							month == Calendar.MARCH ||
							month == Calendar.MAY ||
							month == Calendar.AUGUST ||
							month == Calendar.OCTOBER) {
						//and the date is the 31st
						if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
							//in this case the 2 months are added because the next months after
							//January, March, May, August and October do not have the 31st!
							startCal.set(startTime.getYear(),startTime.getMonth()+2,startTime.getDate());
							start += month31*2L; //start up two 31 day months
						}
						else {
							startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
							start += month31; //start up one 31 day month
						}

					}
					else if(startTime.getMonth() == Calendar.JULY) {
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month31;//start up one 31 day month
					}

					else if(startTime.getMonth() == Calendar.FEBRUARY) {
						//leap year
						if(startTime.getYear()%4 == 0) {
							start += month29;//start up 29 day month
						}
						else {
							//"ordinary" February
							start += month28;//start up 28 day month
						}
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
					}
					else {
						//this case is for months APRIL, JUNE, SEPTEMBER and NOVEMBER
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month30;	//start up 30 day month
					}
				}
			}
			else if(repeat.equals(CalendarEntryCreator.weeklyFieldParameterName)) {
				startCal.add(Calendar.DAY_OF_MONTH,7);
				start += week;
			}
			else if(repeat.equals(CalendarEntryCreator.noRepeatFieldParameterName)) {
				start = end;
				//do nothing - the entry is just saved for the current day
			}
			//if the last day of the month
			else if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
				//if the the last day of month and last month of year
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add one to year, set month = January and day = 1
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,1);
				}
				else {
					//if last day of month and not last month of year
					//year is same, add 1 to month and day = 1
					startCal.set(startTime.getYear(),startTime.getMonth()+1,1);
				}
				start += day; //start up one day = 86400000 milliseconds
			}
			else {
				//if not the last day of month
				//and not the last month of year
				//year is the same, month is the same, 1 added to day
				startCal.set(startTime.getYear(),startTime.getMonth(),startTime.getDate()+1);
				start += day; //start up one day = 86400000 milliseconds
			}

			Date sd = startCal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			String f = format.format(sd);

			startTime = Timestamp.valueOf(f);
			int year = sd.getYear() + 1900;
			startTime.setYear(year);


		}

	}
	@Override
	public void updateEntry(int entryID, String headline, User user, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String ledger, String description, String location, String oneOrMany) {
		CalendarEntry entry = getEntry(entryID);

		IWTimestamp startD = new IWTimestamp(startDate);
		Timestamp startTime = startD.getTimestamp();//Timestamp.valueOf(startDate);
		//modifications of the time properties of the start timestamp
		if(startHour != null && !startHour.equals("")) {
			Integer sH =new Integer(startHour);
			startTime.setHours(sH.intValue());
		}
		if(startMinute != null && !startMinute.equals("")) {
			Integer sM = new Integer(startMinute);
			startTime.setMinutes(sM.intValue());
		}
		startTime.setSeconds(0);
//		startTime.setNanos(0);

		IWTimestamp endD = new IWTimestamp(endDate);
		Timestamp endTime = endD.getTimestamp();//Timestamp.valueOf(endDate);
		//modifications of the time properties of the end timestamp
		if(endHour != null && !endHour.equals("")) {
			Integer eH =new Integer(endHour);
			endTime.setHours(eH.intValue());
		}
		if(endMinute != null && !endMinute.equals("")) {
			Integer eM =new Integer(endMinute);
			endTime.setMinutes(eM.intValue());
		}
		endTime.setSeconds(0);
//		endTime.setNanos(0);

		GregorianCalendar startCal = new GregorianCalendar(startTime.getYear(),startTime.getMonth(),startTime.getDate(),startTime.getHours(),startTime.getMinutes());
		GregorianCalendar endCal = new GregorianCalendar(endTime.getYear(),endTime.getMonth(),endTime.getDate(),endTime.getHours(),endTime.getMinutes());

		long start = startCal.getTimeInMillis();
		long end = endCal.getTimeInMillis();


		long year365 = 365L*24L*60L*60L*1000L;
		long year366 = 366L*24L*60L*60L*1000L;
		long month31 = 31L*24L*60L*60L*1000L;
		long month30 = 30L*24L*60L*60L*1000L;
		long month29 = 29L*24L*60L*60L*1000L;
		long month28 = 28L*24L*60L*60L*1000L;
		long week = 7L*24L*60L*60L*1000L;
		long day = 24L*60L*60L*1000L;

		Integer groupID = null;
		if(attendees != null && !attendees.equals("")) {
			groupID = new Integer(attendees);
		}
		Integer userID = null;
		if(user != null) {
			userID = (Integer) user.getPrimaryKey();
		}
		else {
			userID = new Integer(-1);
		}

		while(start < end) {
			Timestamp endOfEntryTime = Timestamp.valueOf(startTime.toString());
			if(endHour != null && !endHour.equals("")) {
				Integer eH =new Integer(endHour);
				endOfEntryTime.setHours(eH.intValue());
			}
			if(endMinute != null && !endMinute.equals("")) {
				Integer eM =new Integer(endMinute);
				endOfEntryTime.setMinutes(eM.intValue());
			}
			endOfEntryTime.setSeconds(0);

			try {
				CalendarEntryType entryType = getEntryTypeByName(type);
				Integer entryTypePK = (Integer) entryType.getPrimaryKey();
				Integer ledgerID = new Integer(ledger);
				entry.setName(headline);
				entry.setUserID(userID.intValue());
				entry.setEntryTypeID(entryTypePK.intValue());
				entry.setEntryType(entryType.getName());
				entry.setRepeat(repeat);
				entry.setDate(startTime);
				entry.setEndDate(endOfEntryTime);
				if(groupID != null) {
					entry.setGroupID(groupID.intValue());
				}
				if(ledgerID.intValue() != -1) {
					entry.setLedgerID(ledgerID.intValue());
				}
				entry.setDescription(description);
				entry.setLocation(location);
				entry.store();

			} catch(Exception e) {
				e.printStackTrace();
			}
			if(repeat.equals(CalendarEntryCreator.yearlyFieldParameterName)) {
				if(startTime.getYear()%4 == 0) {
					start += year366;
				}
				else {
					start += year365; //start up one year = 31536000000 milliseconds
				}
				startCal.set(startTime.getYear()+1,startTime.getMonth(),startTime.getDate());

			}

			else if(repeat.equals(CalendarEntryCreator.monthlyFieldParameterName)) {
				//if December
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add 1 to the year and set the month to January
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,startTime.getDate());
				}
				else {
					int month = startTime.getMonth();
					//if the month has 31 days
					if(month == Calendar.JANUARY ||
							month == Calendar.MARCH ||
							month == Calendar.MAY ||
							month == Calendar.AUGUST ||
							month == Calendar.OCTOBER) {
						//and the date is the 31st
						if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
							//in this case the 2 months are added because the next months after
							//January, March, May, August and October do not have the 31st!
							startCal.set(startTime.getYear(),startTime.getMonth()+2,startTime.getDate());
							start += month31*2L; //start up two 31 day months
						}
						else {
							startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
							start += month31; //start up one 31 day month
						}

					}
					else if(startTime.getMonth() == Calendar.JULY) {
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month31;//start up one 31 day month
					}

					else if(startTime.getMonth() == Calendar.FEBRUARY) {
						//leap year
						if(startTime.getYear()%4 == 0) {
							start += month29;//start up 29 day month
						}
						else {
							//"ordinary" February
							start += month28;//start up 28 day month
						}
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
					}
					else {
						//this case is for months APRIL, JUNE, SEPTEMBER and NOVEMBER
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month30;	//start up 30 day month
					}
				}
			}
			else if(repeat.equals(CalendarEntryCreator.weeklyFieldParameterName)) {
				startCal.add(Calendar.DAY_OF_MONTH,7);
				start += week;
			}
			//if the last day of the month
			else if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
				//if the the last day of month and last month of year
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add one to year, set month = January and day = 1
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,1);
				}
				else {
					//if last day of month and not last month of year
					//year is same, add 1 to month and day = 1
					startCal.set(startTime.getYear(),startTime.getMonth()+1,1);
				}
				start += day; //start up one day = 86400000 milliseconds
			}
			else {
				//if not the last day of month
				//and not the last month of year
				//year is the same, month is the same, 1 added to day
				startCal.set(startTime.getYear(),startTime.getMonth(),startTime.getDate()+1);
				start += day; //start up one day = 86400000 milliseconds
			}

			Date sd = startCal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			String f = format.format(sd);

			startTime = Timestamp.valueOf(f);
			int year = sd.getYear() + 1900;
			startTime.setYear(year);
		}
	}
	@Override
	public void createNewLedger(String name, int groupID, String coachName, String date,int coachGroupID) {
		IWContext iwc = CoreUtil.getIWContext();
		Collection users = null;
		Group group = null;
		User user = null;
		User coach = null;
		IWTimestamp iwstamp = new IWTimestamp(date);
		Timestamp stamp = iwstamp.getTimestamp();
		stamp.setHours(0);
		stamp.setMinutes(0);
		stamp.setSeconds(0);
		stamp.setNanos(0);

		try {
			group = getGroupBusiness(iwc).getGroupByGroupID(groupID);
			users = this.getUserBusiness(iwc).getUsersInGroup(group);
			coach = iwc.getCurrentUser();
		} catch (Exception e){
			e.printStackTrace();
		}

		try {
			Integer coachID = (Integer) coach.getPrimaryKey();
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			CalendarLedger ledger = ledgerHome.create();
			ledger.setName(name);
			ledger.setGroupID(groupID);
			ledger.setCoachGroupID(coachGroupID);
			ledger.setCoachID(coachID.intValue());
			ledger.setDate(stamp);
			ledger.store();
			Iterator userIter = users.iterator();
			while(userIter.hasNext()) {
				user = (User) userIter.next();
				ledger.addUser(user);
			}


		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void createNewMark(int markID, String markName, String description) {
		try {
			AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
			AttendanceMark mark = null;
			if(markID == -1) {
				mark = markHome.create();
			}
			else {
				mark = markHome.findByPrimaryKey(new Integer(markID));
			}
			mark.setMark(markName);
			mark.setMarkDescription(description);
			mark.store();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public Collection getUsersInLedger(int ledgerID) {
		CalendarLedger ledger = getLedger(ledgerID);
		Collection users = ledger.getUsers();
//		User user = null;
//		Iterator userIter = users.iterator();
//		while(userIter.hasNext()) {
//			user = (User) userIter.next();
//		}
		return users;
	}

	@Override
	public void saveAttendance(int userID, int ledgerID, CalendarEntry entry, String mark) {

		try {
			AttendanceEntity attendance = getAttendanceByUserIDandEntry(userID,entry);
			if(attendance == null) {
				AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
				attendance = attendanceHome.create();
			}
			Timestamp date = entry.getDate();
			int entryID = entry.getEntryID();
			attendance.setUserID(userID);
			attendance.setLedgerID(ledgerID);
			attendance.setEntryID(entryID);
			attendance.setAttendanceDate(date);
			attendance.setAttendanceMark(mark);
			attendance.store();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateAttendance(int attendanceID, int userID, int ledgerID, CalendarEntry entry, String mark) {
		AttendanceEntity attendance = getAttendanceEntity(attendanceID);
		try {
			Timestamp date = entry.getDate();
			int entryID = entry.getEntryID();
			attendance.setUserID(userID);
			attendance.setLedgerID(ledgerID);
			attendance.setEntryID(entryID);
			attendance.setAttendanceDate(date);
			attendance.setAttendanceMark(mark);
			attendance.store();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteEntryType(int typeID) {
		CalendarEntryType type = CalendarFinder.getInstance().getEntryType(typeID);
		if (type != null) {
			try {
				CalendarEntry[] entries = (CalendarEntry[]) com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance().findAllByColumn(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryTypeID(), typeID);
				if (entries != null) {
					for (int a = 0; a < entries.length; a++) {
						entries[a].remove();
					}
				}

				type.remove();
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Unable to remove type: ", e);
			}
		}
	}

	@Override
	public boolean deleteBlock(int iObjectInstanceId) {
		return CategoryBusiness.getInstance().deleteBlock(iObjectInstanceId);
	}
	@Override
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		GroupBusiness groupBiz = null;
		if (groupBiz == null) {
			try {
				groupBiz = com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return groupBiz;
	}
	protected UserBusiness getUserBusiness(IWApplicationContext iwc) {
		UserBusiness userBusiness = null;
		if (userBusiness == null) {
			try {
				userBusiness = com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return userBusiness;
	}

	@Override
	public Collection getEntriesByICGroup(int groupId){
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByICGroup(groupId));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Collection getEntriesByEvents(List eventsList){
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByEvents(eventsList));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<CalendarEntry> getEntriesByEventsIds(List<String> eventsIds) {
		List<CalendarEntry> entries = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			Collection<CalendarEntry> foundEntries = entryHome.getEntriesByEventsIds(eventsIds);
			if (foundEntries != null) {
				entries = new ArrayList(foundEntries);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	@Override
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	@Override
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	@Override
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	@Override
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	@Override
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	@Override
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	@Override
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	@Override
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	@Override
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new CalendarWindowPlugin());
		return list;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	@Override
	public List getGroupToolbarElements(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	@Override
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	@Override
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
	 */
	@Override
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
		return null;
	}

	@Override
	public List getLedgersByGroupId(String groupId){
		List ledgers = null;
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledgers = new ArrayList(ledgerHome.findLedgersByGroupId(groupId));
		}catch(Exception e) {
			e.printStackTrace();
		}

		return ledgers;
	}

	@Override
	public List getEntriesByLedgersAndEntryTypes(List<String> listOfEntryTypesIds, List<String> listOfLedgerIds){
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByLedgerId(listOfLedgerIds));
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<CalendarEntry> getEntriesByEventsIdsAndGroupsIds(List<String> eventsIds, List<String> groupsIds) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.getEntriesByEventsIdsAndGroupsIds(eventsIds, groupsIds));
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<CalendarEntry> getEntriesByLedgersIdsAndGroupsIds(List<String> ledgersIds, List<String> groupsIds) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.getEntriesByLedgersIdsAndGroupsIds(ledgersIds, groupsIds));
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<CalendarLedger> getUserLedgers(User user, IWContext iwc) {
		if (user == null) {
			return null;
		}

		List<String> groupsIds = null;
		try {
			groupsIds = getUserBusiness(iwc).getAllUserGroupsIds(user, iwc);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		CalendarLedgerHome ledgerHome = null;
		try {
			ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		if (groupsIds == null || groupsIds.size() == 0) {
			try {
				return ledgerHome.findLedgersByCoachId(user.getId());
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}

		try {
			return ledgerHome.findLedgersByCoachIdAndGroupsIds(user.getId(), groupsIds);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<CalendarLedger> getUserLedgers(String userId, IWContext iwc) {
		if (userId == null) {
			return null;
		}

		try {
			return getUserLedgers(getUserBusiness(IWMainApplication.getDefaultIWApplicationContext()).getUser(Integer.valueOf(userId)), iwc);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	@Override
	public List<CalendarEntry> getEntriesByLedgers(List<String> ledgersIds) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.getEntriesByLedgersIds(ledgersIds));
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<CalendarEntry> getEntriesByCriteria(String calendarId, List<String> groupsIds, List<String> userIds, Timestamp from, Timestamp to) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByCriteria(calendarId, groupsIds, userIds, from, to));
		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
