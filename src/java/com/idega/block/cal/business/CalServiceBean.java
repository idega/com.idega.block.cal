package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;

import org.apache.myfaces.custom.schedule.model.ScheduleModel;

import com.idega.block.cal.bean.CalendarManagerBean;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.cal.presentation.EntryInfoBlock;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.business.IBOLookup;
import com.idega.business.chooser.helper.CalendarsChooserHelper;
import com.idega.cal.bean.CalendarPropertiesBean;
import com.idega.core.builder.business.ICBuilderConstants;
import com.idega.core.cache.IWCacheManager2;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.GroupService;
import com.idega.user.data.Group;
import com.idega.util.CoreUtil;
import com.idega.util.IWTimestamp;
import com.idega.webface.WFUtil;

public class CalServiceBean implements CalService {
	
	private CalBusiness calBusiness = null;
	private GroupService groupService = null;
	private GroupBusiness groupBusiness = null;
	
	private String calendarCacheName = "calendarViewersUniqueIdsCache";
	private String eventsCacheName = "eventsForCalendarViewersUniqueIdsCache";
	private String ledgersCacheName = "ledgersForCalendarViewersUniqueIdsCache";
	
	/**
	 * Checks if can use DWR on remote server
	 */
	public boolean canUseRemoteServer(String server) {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return false;
		}
		
		GroupService groupService = getGroupService(iwc);
		if (groupService == null) {
			return false;
		}
	
		List<String> scripts = new ArrayList<String>();
		scripts.add("/dwr/engine.js");
		scripts.add(CalendarConstants.CALENDAR_SERVICE_DWR_INTERFACE_SCRIPT);
		
		return groupService.canMakeCallToServerAndScript(server, scripts);
	}
	
	private CalendarManagerBean getBean() {
		Object o = WFUtil.getBeanInstance(CalendarConstants.CALENDAR_MANAGER_BEAN_ID);
		if (!(o instanceof CalendarManagerBean)) {
			return null;
		}
		return (CalendarManagerBean) o;
	}
	
	public CalendarPropertiesBean getCalendarProperties(String instanceId) {
		if (instanceId == null) {
			return null;
		}
		CalendarManagerBean bean = getBean();
		if (bean == null) {
			return null;
		}
		
		return bean.getCalendarProperties(instanceId);
	}
	
	@SuppressWarnings("unchecked")
	public List<AdvancedProperty> getAvailableCalendarEventTypes() {
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
		
		List<AdvancedProperty> types = new ArrayList<AdvancedProperty>();
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
	
	public List<AdvancedProperty> getAvailableCalendarEventTypesWithLogin(String login, String password) {
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
	
	@SuppressWarnings("unchecked")
	public List<AdvancedProperty> getAvailableLedgers() {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return null;
		}
		
		List allLedgers = null;
		try {
			allLedgers = calBusiness.getAllLedgers();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (allLedgers == null) {
			return null;
		}
		
		List<AdvancedProperty> ledgers = new ArrayList<AdvancedProperty>();
		
		Object o = null;
		CalendarLedger ledger = null;
		for (int i = 0; i < allLedgers.size(); i++) {
			o = allLedgers.get(i);
			if (o instanceof CalendarLedger) {
				ledger = (CalendarLedger) o;
				ledgers.add(new AdvancedProperty(String.valueOf(ledger.getLedgerID()), ledger.getName()));
			}
		}
		
		return ledgers;
	}
	
	public List<AdvancedProperty> getAvailableLedgersWithLogin(String login, String password) {
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

	public List<String> getCalendarInformation() {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		IWBundle bundle = null;
		try {
			bundle = iwc.getIWMainApplication().getBundle(CalendarConstants.IW_BUNDLE_IDENTIFIER);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		IWResourceBundle iwrb = bundle.getResourceBundle(iwc);
		if (iwrb == null) {
			return null;
		}
		
		List<String> info = new ArrayList<String>();
		
		info.add(ICBuilderConstants.CALENDAR_EVENTS_ADVANCED_PROPERTY_KEY);								//	0
		info.add(ICBuilderConstants.CALENDAR_LEDGERS_ADVANCED_PROPERTY_KEY);							//	1
		
		info.add(iwrb.getLocalizedString("no_events_exist", "Sorry, there are no events created."));	//	2
		info.add(iwrb.getLocalizedString("no_ledgers_exist", "Sorry, there are no ledgers created."));	//	3
		
		info.add(calendarCacheName);																	//	4
		info.add(eventsCacheName);																		//	5
		info.add(ledgersCacheName);																		//	6
		
		info.add(String.valueOf(ScheduleModel.MONTH));													//	7
		info.add(String.valueOf(ScheduleModel.DAY));													//	8
		info.add(String.valueOf(ScheduleModel.WEEK));													//	9
		info.add(String.valueOf(ScheduleModel.WORKWEEK));												//	10
		
		info.add(iwrb.getLocalizedString("name", "Name"));												//	11
		info.add(iwrb.getLocalizedString("endDate", "End date"));										//	12
		info.add(iwrb.getLocalizedString("type", "Type"));												//	13
		info.add(iwrb.getLocalizedString("time", "Time"));												//	14
		info.add(iwrb.getLocalizedString("date", "Date"));												//	15
		info.add(iwrb.getLocalizedString("noEntriesToDisplay", "There are no entries to display"));		//	16
		info.add(iwrb.getLocalizedString("cantConnectTo", "can not connect to:"));						//	17
		info.add(iwrb.getLocalizedString("loadingMsg", "Loading..."));									//	18
		
		info.add(iwrb.getLocalizedString("previousLabel", "Previous"));									//	19
		info.add(iwrb.getLocalizedString("nextLabel", "Next"));											//	20
		info.add(iwrb.getLocalizedString("dayLabel", "Day"));											//	21
		info.add(iwrb.getLocalizedString("weekLabel", "Week"));											//	22
		info.add(iwrb.getLocalizedString("workweekLabel", "Work week"));								//	23
		info.add(iwrb.getLocalizedString("monthLabel", "Month"));										//	24
		
		info.add(CalendarConstants.SCHEDULE_ENTRY_STYLE_CLASS);											//	25
		
		BuilderLogic builder = BuilderLogic.getInstance();
		info.add(builder.getUriToObject(EntryInfoBlock.class));											//	26
		
		info.add(iwrb.getLocalizedString("calendar_entry_info", "Calendar entry information"));			//	27
		
		return info;
	}
	
	public boolean addUniqueIdsForCalendarGroups(String instanceId, List<String> ids) {
		GroupService groupService = getGroupService();
		if (groupService == null) {
			return false;
		}
		
		return groupService.addUniqueIds(calendarCacheName, instanceId, ids);
	}
	
	public boolean addUniqueIdsForCalendarLedgers(String instanceId, List<String> ids) {
		if (ids == null || ids.size() == 0) {
			return true;
		}
		
		GroupService groupService = getGroupService();
		if (groupService == null) {
			return false;
		}
		
		return groupService.addUniqueIds(ledgersCacheName, instanceId, ids);
	}
	
	public boolean addUniqueIdsForCalendarEvents(String instanceId, List<String> ids) {
		if (ids == null || ids.size() == 0) {
			return true;
		}
		
		GroupService groupService = getGroupService();
		if (groupService == null) {
			return false;
		}
		
		return groupService.addUniqueIds(eventsCacheName, instanceId, ids);
	}
	
	public CalendarPropertiesBean reloadProperties(String instanceId) {
		if (instanceId == null) {
			return null;
		}
		
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}
		
		BuilderLogic builder = BuilderLogic.getInstance();
		String pageKey = builder.getCurrentIBPage(iwc);
		
		String propertyName = new StringBuffer(":method:1:implied:void:setCalendarProperties:").append(CalendarPropertiesBean.class.getName()).append(":").toString();
		String values[] = builder.getPropertyValues(iwc.getIWMainApplication(), pageKey, instanceId, propertyName, null, true);
		if (values == null) {
			return null;
		}
		if (values.length == 0) {
			return null;
		}
		
		CalendarsChooserHelper helper = new CalendarsChooserHelper();
		CalendarPropertiesBean bean = helper.getExtractedPropertiesFromString(values[0]);
		
		if (bean == null) {
			return null;
		}
		Object[] parameters = new Object[2];
		parameters[0] = instanceId;
		parameters[1] = bean;
		
		Class<?>[] classes = new Class[2];
		classes[0] = String.class;
		classes[1] = CalendarPropertiesBean.class;
		
		WFUtil.invoke(CalendarConstants.CALENDAR_MANAGER_BEAN_ID, "addCalendarProperties", parameters, classes);
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	public List<CalScheduleEntry> getCalendarEntries(String login, String password, String instanceId, Integer cacheTime, boolean remoteMode) {
		if (instanceId == null) {
			return null;
		}
		
		if (remoteMode && (login == null || password == null)) {
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
		
		if (remoteMode && !groupService.isUserLoggedOn(iwc, login, password)) {
			return null;
		}
		
		List<String> groupsUniqueIds = null;
		try {
			groupsUniqueIds = groupService.getUniqueIds(calendarCacheName).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		List<String> ledgersIds = null;
		try {
			ledgersIds = groupService.getUniqueIds(ledgersCacheName).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		List<String> eventsIds = null;
		try {
			eventsIds = groupService.getUniqueIds(eventsCacheName).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (groupsUniqueIds == null) {
			return null;
		}
		
		boolean useCache = cacheTime == null ? false : true;
		if (useCache) {
			List<CalScheduleEntry> entriesFromCache = getCalendarEntriesFromCache(iwc, instanceId);
			if (entriesFromCache != null) {
				return entriesFromCache;
			}
		}
		
		CalBusiness calBusiness = getCalBusiness(iwc);
		if (calBusiness == null) {
			return null;
		}
		GroupBusiness groupBusiness = getGroupBusiness(iwc);
		if (groupBusiness == null) {
			return null;
		}
		
		//	Getting ids for groups from unique ids
		List<String> groupsIds = new ArrayList<String>();
		Group group = null;
		for (int i = 0; i < groupsUniqueIds.size(); i++) {
			group = null;
			
			try {
				group = groupBusiness.getGroupByUniqueId(groupsUniqueIds.get(i));
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			
			if (group != null) {
				groupsIds.add(group.getId());
			}
		}
		
		//	Events by type(s)
		List<CalendarEntry> entriesByEvents = new ArrayList<CalendarEntry>();
		if (eventsIds != null && eventsIds.size() > 0) {
			Collection entries = null;
			try {
				entries = calBusiness.getEntriesByEventsIdsAndGroupsIds(eventsIds, groupsIds);
			} catch(Exception e) {
				e.printStackTrace();
			}
			if (entries != null) {
				entriesByEvents.addAll(entries);
			}
		}
		
		//	Events by ledger(s)
		List<CalendarEntry> entriesByLedgers = new ArrayList<CalendarEntry>();
		if (ledgersIds != null && ledgersIds.size() > 0) {
			Collection entries = null;
			try {
				entries = calBusiness.getEntriesByLedgersIdsAndGroupsIds(ledgersIds, groupsIds);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if (entries != null) {
				entriesByLedgers.addAll(entries);
			}
		}
		
		List<CalendarEntry> allEntries = new ArrayList<CalendarEntry>();
		allEntries.addAll(entriesByEvents);
		
		allEntries = getFilteredEntries(entriesByLedgers, allEntries);
		
		List<CalScheduleEntry> entries =getConvertedEntries(allEntries, iwc.getCurrentLocale());
		if (useCache) {
			addCalendarEntriesToCache(iwc, instanceId, entries);
		}
		
		return entries;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<CalScheduleEntry>> getCalendarCache(IWContext iwc) {
		if (iwc == null) {
			return null;
		}
		
		IWCacheManager2 cacheManager = IWCacheManager2.getInstance(iwc.getIWMainApplication());
		if (cacheManager == null) {
			return null;
		}
		
		Object o = null;
		
		try {
			cacheManager.getCache("cacheForCalendarViewerCalScheduleEntries");
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (o instanceof Map) {
			return (Map) o;
		}
		
		return null;
	}
	
	private boolean addCalendarEntriesToCache(IWContext iwc, String instanceId, List<CalScheduleEntry> entries) {
		if (instanceId == null || entries == null) {
			return false;
		}
		
		try {
			getCalendarCache(iwc).put(instanceId, entries);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private List<CalScheduleEntry> getCalendarEntriesFromCache(IWContext iwc, String instanceId) {
		if (instanceId == null) {
			return null;
		}
		
		try {
			return getCalendarCache(iwc).get(instanceId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean removeCelandarEntriesFromCache(String instanceId) {
		if (instanceId == null) {
			return false;
		}
		
		try {
			Map<String, List<CalScheduleEntry>> cache = getCalendarCache(CoreUtil.getIWContext());
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
	
	private List<CalendarEntry> getFilteredEntries(List<CalendarEntry> source, List<CalendarEntry> destination) {
		if (source != null) {
			for (int i = 0; i < source.size(); i++) {
				if (!(destination.contains(source.get(i)))) {
					destination.add(source.get(i));
				}
			}
		}
		
		return destination;
	}
	
	private List<CalScheduleEntry> getConvertedEntries(List<CalendarEntry> entries, Locale locale) {
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
		
		List<CalScheduleEntry> convertedEntries = new ArrayList<CalScheduleEntry>();
		CalendarEntry entry = null;
		for (int i = 0; i < entries.size(); i++) {
			entry = entries.get(i);
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
