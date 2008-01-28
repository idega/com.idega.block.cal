package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.core.cache.IWCacheManager2;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.GroupService;
import com.idega.user.data.Group;
import com.idega.util.CoreUtil;
import com.idega.util.IWTimestamp;

public class CalServiceBean extends IBOSessionBean implements CalService {

	private static final long serialVersionUID = 4793559510518957459L;
	
	private CalBusiness calBusiness = null;
	private GroupService groupService = null;
	private GroupBusiness groupBusiness = null;
	
	private String calendarCacheName = "calendarViewersUniqueIdsCache";
	private String eventsCacheName = "eventsForCalendarViewersUniqueIdsCache";
	private String ledgersCacheName = "ledgersForCalendarViewersUniqueIdsCache";
	
	private List getAvailableCalendarEventTypes() {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return null;
		}
		
		List eventsTypes = null;
		try {
			eventsTypes = calBusiness.getAllEntryTypes();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (eventsTypes == null) {
			return null;
		}
		
		List types = new ArrayList();
		Object o = null;
		CalendarEntryType calendarEntryType = null;
		for (int i = 0; i < eventsTypes.size(); i++) {
			o = eventsTypes.get(i);
			if (o instanceof CalendarEntryType) {
				calendarEntryType = (CalendarEntryType) o;
				types.add(new AdvancedProperty(calendarEntryType.getId(), calendarEntryType.getName()));
			}
		}
		
		return types;
	}
	
	public List getAvailableCalendarEventTypesWithLogin(String login, String password) {
		if (login == null || password == null) {
			return null;
		}
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return null;
		}
		if (!groupService.isLoggedUser(iwc, login)) {
			if (!groupService.logInUser(iwc, login, password)) {
				return null;
			}
		}
		
		return getAvailableCalendarEventTypes();
	}

	private List getAvailableLedgers() {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return null;
		}
		
		List userLedgers = calBusiness.getUserLedgers(iwc.getCurrentUser(), iwc);
		if (userLedgers == null) {
			return null;
		}
		
		List ledgers = new ArrayList();

		CalendarLedger ledger = null;
		for (int i = 0; i < userLedgers.size(); i++) {
			ledger = (CalendarLedger) userLedgers.get(i);
			ledgers.add(new AdvancedProperty(String.valueOf(ledger.getLedgerID()), ledger.getName()));
		}
		
		return ledgers;
	}
	
	public List getAvailableLedgersWithLogin(String login, String password) {
		if (login == null || password == null) {
			return null;
		}
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return null;
		}
		if (!groupService.isLoggedUser(iwc, login)) {
			if (!groupService.logInUser(iwc, login, password)) {
				return null;
			}
		}
		
		return getAvailableLedgers();
	}
	
	private CalBusiness getCalBusiness(IWApplicationContext iwac) {
		if (calBusiness == null) {
			try {
				calBusiness = (CalBusiness) IBOLookup.getServiceInstance(iwac, CalBusiness.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return calBusiness;
	}
	
	private GroupBusiness getGroupBusiness(IWApplicationContext iwac) {
		if (groupBusiness == null) {
			try {
				groupBusiness = (GroupBusiness) IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return groupBusiness;
	}
	
	private GroupService getGroupService(IWApplicationContext iwac) {
		if (groupService == null) {
			try {
				groupService = (GroupService) IBOLookup.getServiceInstance(iwac, GroupService.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return groupService;
	}
	
	private GroupService getGroupService() {
		if (groupService == null) {
			return getGroupService(CoreUtil.getIWContext());
		}
		return groupService;
	}
	
	public Boolean addUniqueIdsForCalendarGroups(String instanceId, List ids) {
		GroupService groupService = getGroupService();
		if (groupService == null) {
			return Boolean.FALSE;
		}
		
		return new Boolean(groupService.addUniqueIds(calendarCacheName, instanceId, ids));
	}
	
	public Boolean addUniqueIdsForCalendarLedgers(String instanceId, List ids) {
		if (ids == null || ids.size() == 0) {
			return Boolean.TRUE;
		}
		
		GroupService groupService = getGroupService();
		if (groupService == null) {
			return Boolean.FALSE;
		}
		
		return new Boolean(groupService.addUniqueIds(ledgersCacheName, instanceId, ids));
	}
	
	public Boolean addUniqueIdsForCalendarEvents(String instanceId, List ids) {
		if (ids == null || ids.size() == 0) {
			return Boolean.TRUE;
		}
		
		GroupService groupService = getGroupService();
		if (groupService == null) {
			return Boolean.FALSE;
		}
		
		return new Boolean(groupService.addUniqueIds(eventsCacheName, instanceId, ids));
	}
	
	
	public List getCalendarEntries(String login, String password, String instanceId, Integer cacheTime, Boolean remoteMode) {
		if (instanceId == null) {
			return null;
		}
		
		if (remoteMode == null && (login == null || password == null)) {
			return null;
		}
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return null;
		}
		
		if (remoteMode.booleanValue() && !groupService.isUserLoggedOn(iwc, login, password)) {
			return null;
		}
		
		List groupsUniqueIds = null;
		try {
			groupsUniqueIds = (List) groupService.getUniqueIds(calendarCacheName).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		List ledgersIds = null;
		try {
			ledgersIds = (List) groupService.getUniqueIds(ledgersCacheName).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		List eventsIds = null;
		try {
			eventsIds = (List) groupService.getUniqueIds(eventsCacheName).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return null;
		}
		GroupBusiness groupBusiness = getGroupBusiness(iwc);
		if (groupBusiness == null) {
			return null;
		}
		
		List groupsIds = null;
		if (groupsUniqueIds != null) {
			//	Getting ids for groups from unique ids
			groupsIds = new ArrayList();
			Group group = null;
			for (int i = 0; i < groupsUniqueIds.size(); i++) {
				group = null;
				
				try {
					group = groupBusiness.getGroupByUniqueId(groupsUniqueIds.get(i).toString());
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (FinderException e) {
					e.printStackTrace();
				}
				
				if (group != null) {
					groupsIds.add(group.getId());
				}
			}
		}
		
		List entriesByEvents = new ArrayList();
		List entriesByLedgers = new ArrayList();
		List entries = null;
		if (groupsIds == null || groupsIds.size() == 0) {
			if (ledgersIds == null || ledgersIds.size() == 0) {
				//	We don't want to get calendar entries only by events
				return null;
			}
			
			entries = calBusiness.getEntriesByLedgers(ledgersIds);
			if (entries == null) {
				return null;
			}
			entriesByLedgers.addAll(entries);
		}
		else {
			//	Events by type(s) and group(s)
			if (eventsIds != null && eventsIds.size() > 0) {
				entries = null;
				try {
					entries = calBusiness.getEntriesByEventsIdsAndGroupsIds(eventsIds, groupsIds);
				} catch(Exception e) {
					e.printStackTrace();
				}
				if (entries != null) {
					entriesByEvents.addAll(entries);
				}
			}
			
			//	Events by ledger(s) and group(s)
			if (ledgersIds != null && ledgersIds.size() > 0) {
				entries = null;
				try {
					entries = calBusiness.getEntriesByLedgersIdsAndGroupsIds(ledgersIds, groupsIds);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				if (entries != null) {
					entriesByLedgers.addAll(entries);
				}
			}
		}
		
		List allEntries = new ArrayList();
		allEntries.addAll(entriesByEvents);
		allEntries = getFilteredEntries(entriesByLedgers, allEntries);
		
		return getConvertedEntries(allEntries, iwc.getCurrentLocale());
	}
	
	private Map getCalendarCache(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		IWCacheManager2 cacheManager = IWCacheManager2.getInstance(iwc.getIWMainApplication());
		if (cacheManager == null) {
			return null;
		}
		
		Object o = null;
		
		try {
			o = cacheManager.getCache("cacheForCalendarViewerCalScheduleEntries");
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (o instanceof Map) {
			return (Map) o;
		}
		
		return null;
	}
	
	public boolean removeCelandarEntriesFromCache(String instanceId) {
		if (instanceId == null) {
			return false;
		}
		
		try {
			Map cache = getCalendarCache(CoreUtil.getIWContext());
			if (cache == null) {
				return true;
			}
			cache.remove(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private List getFilteredEntries(List source, List destination) {
		if (source != null) {
			for (int i = 0; i < source.size(); i++) {
				if (!(destination.contains(source.get(i)))) {
					destination.add(source.get(i));
				}
			}
		}
		
		return destination;
	}
	
	private List getConvertedEntries(List entries, Locale locale) {
		if (entries == null) {
			return null;
		}
		
		if (locale == null) {
			IWContext iwc = CoreUtil.getIWContext();
			if (iwc == null) {
				locale = Locale.ENGLISH;
			}
			else {
				locale = iwc.getCurrentLocale();
			}
		}
		
		List convertedEntries = new ArrayList();
		CalendarEntry entry = null;
		for (int i = 0; i < entries.size(); i++) {
			entry = (CalendarEntry) entries.get(i);
			CalScheduleEntry calEntry = new CalScheduleEntry();
			
			IWTimestamp date = new IWTimestamp(entry.getDate());
			IWTimestamp endDate = new IWTimestamp(entry.getEndDate());
			
			calEntry.setId(String.valueOf(entry.getEntryID()));
			calEntry.setEntryName(entry.getName());
			
			calEntry.setEntryDate(date.getDateString(CalendarConstants.DATE_PATTERN));
			calEntry.setEntryEndDate(endDate.getDateString(CalendarConstants.DATE_PATTERN));
			
			calEntry.setEntryTime(date.getLocaleTime(locale));
			calEntry.setEntryEndTime(endDate.getLocaleTime(locale));
			
			calEntry.setEntryTypeName(entry.getEntryTypeName());
			calEntry.setRepeat(entry.getRepeat());
			calEntry.setEntryDescription(entry.getDescription());
			
			convertedEntries.add(calEntry);
		}
		
		return convertedEntries;
	}
}
