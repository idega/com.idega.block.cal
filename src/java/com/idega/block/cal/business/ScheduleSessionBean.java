package com.idega.block.cal.business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.myfaces.custom.schedule.model.DefaultScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleDay;
import org.apache.myfaces.custom.schedule.model.ScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;
import org.apache.myfaces.custom.schedule.model.SimpleScheduleModel;
import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.util.CoreUtil;
import com.idega.util.IWTimestamp;

public class ScheduleSessionBean implements ScheduleSession {

	private static final long serialVersionUID = -8327086094911532115L;
	
	private SimpleDateFormat simpleDate = new SimpleDateFormat(CalendarConstants.DATE_PATTERN);	
	
	private static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final long WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;
	private static final long MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 31;

	private static final int DEFAULT_MODE = ScheduleModel.MONTH;
	
	private CalScheduleEntry entry = null;
	
	private Map<String, List<CalScheduleEntry>> scheduleEntries = null;
	private Map<String, HtmlSchedule> schedules = null;
	
	public ScheduleSessionBean() {
		getHtmlScheduleCache();
		getScheduleEntriesCache();
	}
	
	private void changeScheduleMode(String id, int mode) {
		HtmlSchedule schedule = getHtmlSchedule(id);
		schedule.getModel().setMode(mode);
		schedule.getModel().refresh();
		setHtmlSchedule(id, schedule);
	}

	public Document changeModeToDayAndGetScheduleDOM(String id) {
		changeScheduleMode(id, ScheduleModel.DAY);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToDayAndGetListOfEntries(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		changeScheduleMode(id, ScheduleModel.DAY);
		Calendar calendar = Calendar.getInstance();
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(schedule.getModel().getSelectedDate());
		
		List<CalScheduleEntry> entriesOfSelectedDay = new ArrayList<CalScheduleEntry>();
		
		List<CalScheduleEntry> entries = getScheduleEntries(id);
		if (entries == null) {
			return null;
		}
		CalScheduleEntry entry = null;
		for (int i = 0; i < entries.size(); i++) {
			entry = entries.get(i);
			
			String entryDateString = entry.getEntryDate();
			Date entryDate = null;                
			try {
				entryDate = simpleDate.parse(entryDateString);
				calendar.setTime(entryDate);
				if ((calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) && (calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) && 
					(calendar.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH))) {
						entriesOfSelectedDay.add(entry);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return entriesOfSelectedDay;
	}
	
	public Document changeModeToMonthAndGetScheduleDOM(String id) {
		changeScheduleMode(id, ScheduleModel.MONTH);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToMonthAndGetListOfEntries(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		changeScheduleMode(id, ScheduleModel.MONTH);
		Calendar calendar = Calendar.getInstance();
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(schedule.getModel().getSelectedDate());
		
		List<CalScheduleEntry> entriesOfSelectedMonth = new ArrayList<CalScheduleEntry>();
		
		List<CalScheduleEntry> entries = getScheduleEntries(id);
		if (entries == null) {
			return null;
		}
		CalScheduleEntry entry = null;
		for (int i = 0; i < entries.size(); i++) {
			entry = entries.get(i);
			String entryDateString = entry.getEntryDate();
			Date entryDate = null;
			try {
				entryDate = simpleDate.parse(entryDateString);
				calendar.setTime(entryDate);
				if ((calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) && (calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH))) {
					entriesOfSelectedMonth.add(entry);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return entriesOfSelectedMonth;
	}	

	public Document changeModeToWorkweekAndGetScheduleDOM(String id){
		changeScheduleMode(id, ScheduleModel.WORKWEEK);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToWorkweekAndGetListOfEntries(String id){
		return getEntriesForCurrentWeek(id, true);
	}		

	public Document changeModeToWeekAndGetScheduleDOM(String id){
		changeScheduleMode(id, ScheduleModel.WEEK);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToWeekAndGetListOfEntries(String id){
		return getEntriesForCurrentWeek(id, false);
	}	
	
	private List<CalScheduleEntry> getEntriesForCurrentWeek(String id, boolean workdaysOnly) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		
		Calendar beginingOfTheWeek = Calendar.getInstance();
		beginingOfTheWeek.setTime(schedule.getModel().getSelectedDate());
		beginingOfTheWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		beginingOfTheWeek.set(Calendar.HOUR_OF_DAY, 0);
		beginingOfTheWeek.set(Calendar.MINUTE, 0);
		beginingOfTheWeek.set(Calendar.SECOND, 0);
		beginingOfTheWeek.set(Calendar.MILLISECOND, 0);
		
		Calendar endOfTheWeek = Calendar.getInstance();
		endOfTheWeek.setTime(schedule.getModel().getSelectedDate());
		if (workdaysOnly) {
			changeScheduleMode(id, ScheduleModel.WORKWEEK);
			endOfTheWeek.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		}
		else {
			changeScheduleMode(id, ScheduleModel.WEEK);
			endOfTheWeek.setTimeInMillis(endOfTheWeek.getTimeInMillis()+WEEK_IN_MILLISECONDS);
			endOfTheWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		}
		endOfTheWeek.set(Calendar.HOUR_OF_DAY, 0);
		endOfTheWeek.set(Calendar.MINUTE, 0);
		endOfTheWeek.set(Calendar.SECOND, 0);
		endOfTheWeek.set(Calendar.MILLISECOND, 0);			
		
		List<CalScheduleEntry> entriesOfSelectedWorkweek = new ArrayList<CalScheduleEntry>();
		
		List<CalScheduleEntry> entries = getScheduleEntries(id);
		if (entries == null) {
			return null;
		}
		CalScheduleEntry entry = null;
		for (int i = 0; i < entries.size(); i++) {
			entry = entries.get(i);
			String entryDateString = entry.getEntryDate();
			Date entryDate = null;
			try {
				entryDate = simpleDate.parse(entryDateString);
				calendar.setTime(entryDate);
				if (beginingOfTheWeek.before(calendar) && calendar.before(endOfTheWeek)) {
					entriesOfSelectedWorkweek.add(entry);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entriesOfSelectedWorkweek;		
	}
	
	public Document switchToNextAndGetScheduleDOM(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		ScheduleModel model = schedule.getModel();
		switch (model.getMode()) {
			case ScheduleModel.DAY:
				setSelectedDateToNextDay(id);
				break;
			case ScheduleModel.WORKWEEK:
				setSelectedDateToNextWeek(id);
				break;
			case ScheduleModel.WEEK:
				setSelectedDateToNextWeek(id);
				break;
			case ScheduleModel.MONTH:
				setSelectedDateToNextMonth(id);
				break;
		}
		
		return getScheduleDOM(id);
	}	

	public List<CalScheduleEntry> switchToNextAndGetListOfEntries(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		ScheduleModel model = schedule.getModel();
		switch (model.getMode()) {
			case ScheduleModel.DAY: 
				setSelectedDateToNextDay(id);
				return changeModeToDayAndGetListOfEntries(id);
			case ScheduleModel.WORKWEEK: 
				setSelectedDateToNextWeek(id);
				return changeModeToWorkweekAndGetListOfEntries(id);
			case ScheduleModel.WEEK: 
				setSelectedDateToNextWeek(id);
				return changeModeToWeekAndGetListOfEntries(id);
			case ScheduleModel.MONTH: 
				setSelectedDateToNextMonth(id); 
				return changeModeToMonthAndGetListOfEntries(id);
		}
		
		return null;
	}	
	
	public Document switchToPreviousAndGetScheduleDOM(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		ScheduleModel model = schedule.getModel();
		switch (model.getMode()) {
			case ScheduleModel.DAY:
				setSelectedDateToPreviousDay(id);
				break;
			case ScheduleModel.WORKWEEK:
				setSelectedDateToPreviousWeek(id);
				break;
			case ScheduleModel.WEEK:
				setSelectedDateToPreviousWeek(id);
				break;
			case ScheduleModel.MONTH:
				setSelectedDateToPreviousMonth(id);
				break;
		}
		return getScheduleDOM(id);
	}

	public List<CalScheduleEntry> switchToPreviousAndGetListOfEntries(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return null;
		}
		
		ScheduleModel model = schedule.getModel();
		switch (model.getMode()) {
			case ScheduleModel.DAY: 
				setSelectedDateToPreviousDay(id);
				return changeModeToDayAndGetListOfEntries(id);
			case ScheduleModel.WORKWEEK: 
				setSelectedDateToPreviousWeek(id);
				return changeModeToWorkweekAndGetListOfEntries(id);
			case ScheduleModel.WEEK: 
				setSelectedDateToPreviousWeek(id);
				return changeModeToWeekAndGetListOfEntries(id);
			case ScheduleModel.MONTH:
				setSelectedDateToPreviousMonth(id); 
				return changeModeToMonthAndGetListOfEntries(id);
		}
		return null;
	}		
	
	private void setSelectedDateToNextDay(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return;
		}
		
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		
		schedule.setModel(model);
		setHtmlSchedule(id, schedule);
	}
	
	private void setSelectedDateToNextWeek(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return;
		}
		
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		
		schedule.setModel(model);
		setHtmlSchedule(id, schedule);
	}
	
	private void setSelectedDateToNextMonth(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return;
		}
		
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		
		schedule.setModel(model);
		setHtmlSchedule(id, schedule);
	}

	private void setSelectedDateToPreviousDay(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return;
		}
		
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		
		schedule.setModel(model);
		setHtmlSchedule(id, schedule);
	}
	
	private void setSelectedDateToPreviousWeek(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return;
		}
		
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		
		schedule.setModel(model);
		setHtmlSchedule(id, schedule);
	}
	
	private void setSelectedDateToPreviousMonth(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			return;
		}
		
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		
		schedule.setModel(model);
		setHtmlSchedule(id, schedule);
	}
	
	public Document getScheduleDOM(String id) {
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc == null) {
			return null;
		}

		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		ScheduleModel scheduleModel = null;
		if (schedule == null) {
			schedule = getHtmlSchedule(id);
		}
		scheduleModel = schedule.getModel();


		if (scheduleModel == null) {
			scheduleModel = new SimpleScheduleModel();
			scheduleModel.setMode(DEFAULT_MODE);
			
			List<CalScheduleEntry> entries = getScheduleEntries(id);
			if (entries == null) {
				return null;
			}
			
			CalScheduleEntry entry = null;
			for (int i = 0; i < entries.size(); i++) {
				entry = entries.get(i);
				DefaultScheduleEntry defaultScheduleEntry = new DefaultScheduleEntry();
				if (entry.getId() != null) {
					defaultScheduleEntry.setId(entry.getId());
				}				
				if (entry.getEntryDate() != null) {
					try {
						defaultScheduleEntry.setStartTime(simpleDate.parse(entry.getEntryDate()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (entry.getEntryEndDate() != null) {
					try {
						defaultScheduleEntry.setEndTime(simpleDate.parse(entry.getEntryEndDate()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (entry.getEntryName() != null) {
					defaultScheduleEntry.setTitle(entry.getEntryName());
				}
				if (entry.getEntryDescription() != null) {
					defaultScheduleEntry.setDescription(entry.getEntryDescription());
				}
				try {
					scheduleModel.addEntry(defaultScheduleEntry);
				} catch (RuntimeException e) {
					e.printStackTrace();
					return null;
				}
			}
			
			scheduleModel.refresh();
			schedule.setModel(scheduleModel);
			setHtmlSchedule(id, schedule);
		}
		
		schedule.setReadonly(false);
		Layer scheduleLayer = new Layer();
		scheduleLayer.add(schedule);
		return BuilderLogic.getInstance().getRenderedComponent(iwc, scheduleLayer, false);
	}

	public boolean addEntries(List<CalScheduleEntry> entries, String id, boolean clearPreviousEntries) {
		if (entries == null || id == null) {
			return false;
		}
		
		HtmlSchedule schedule = getHtmlSchedule(id);
		ScheduleModel scheduleModel = schedule.getModel();
		
		if (schedule == null || scheduleModel == null) {
			return false;
		}
		
		if (clearPreviousEntries) {
			removeEntriesFromSchedule(id, scheduleModel);
		}
		
		List<CalScheduleEntry> parsedEntries = new ArrayList<CalScheduleEntry>();
		
		Locale locale = null;
		IWContext iwc = CoreUtil.getIWContext();
		if (iwc != null) {
			locale = iwc.getCurrentLocale();
		}
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		
		String startDate = null;
		String endDate = null;
		for (int i = 0; i < entries.size(); i++) {
			CalScheduleEntry entry = entries.get(i);
			
			if (entry != null) {
				startDate = entry.getEntryDate();
				endDate = entry.getEntryEndDate();
				
				setLocalizedDate(entry, startDate, locale, true);
				setLocalizedDate(entry, endDate, locale, false);
				
				DefaultScheduleEntry defaultScheduleEntry = new DefaultScheduleEntry();
				
				//	Id
				if (entry.getId() != null){
					defaultScheduleEntry.setId(entry.getId());
				}
				
				//	Start date
				if (startDate != null) {
					try {
						defaultScheduleEntry.setStartTime(simpleDate.parse(startDate));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//	End date
				if (endDate != null) {
					try {
						defaultScheduleEntry.setEndTime(simpleDate.parse(endDate));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//	Name
				if (entry.getEntryName() != null) {
					defaultScheduleEntry.setTitle(entry.getEntryName());
				}
				
				//	Description
				if (entry.getEntryDescription() != null) {
					defaultScheduleEntry.setDescription(entry.getEntryDescription());
				}		
				
				scheduleModel.addEntry(defaultScheduleEntry);
				parsedEntries.add(entry);
			}
		}
	
		//	Storing schedule entries
		setEntriesForSchedule(id, parsedEntries);
		
		//	Refreshing schedule
		scheduleModel.refresh();
		schedule.setModel(scheduleModel);
		setHtmlSchedule(id, schedule);
		
		return true;
	}
	
	public List<CalScheduleEntry> getListOfEntries(String id){
		return changeModeToMonthAndGetListOfEntries(id);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<CalScheduleEntry>> getScheduleEntriesCache() {		
		if (scheduleEntries == null) {
			scheduleEntries = new HashMap<String, List<CalScheduleEntry>>();
		}
		
		return scheduleEntries;
	}
	
	private boolean setEntriesForSchedule(String id, List<CalScheduleEntry> entries) {
		return setEntriesForSchedule(id, entries, true);
	}
	
	private boolean setEntriesForSchedule(String id, List<CalScheduleEntry> entries, boolean append) {
		Map<String, List<CalScheduleEntry>> cache = getScheduleEntriesCache();
		if (cache == null) {
			return false;
		}
		
		if (append && entries != null) {
			try {
				List<CalScheduleEntry> currentEntries = cache.get(id);
				if (currentEntries == null) {
					currentEntries = new ArrayList<CalScheduleEntry>(entries);
				}
				else {
					currentEntries.addAll(entries);
				}
				return setEntriesForSchedule(id, currentEntries, false);
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		try {
			cache.put(id, entries);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	
		return true;
	}
	
	private List<CalScheduleEntry> getScheduleEntries(String id) {
		if (id == null) {
			return null;
		}
		
		Map<String, List<CalScheduleEntry>> cache = getScheduleEntriesCache();
		if (cache == null) {
			return null;
		}
		
		try {
			return cache.get(id);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean removeEntriesFromSchedule(String id, ScheduleModel scheduleModel) {
		if (scheduleModel != null) {
			ScheduleDay day = null;
			Object o = null;
			List<ScheduleEntry> oldEntries = new ArrayList<ScheduleEntry>();
			for (int i = 0; i < scheduleModel.size(); i++) {
				o = scheduleModel.get(i);
				if (o instanceof ScheduleDay) {
					day = (ScheduleDay) o;
					Iterator dayEntries = day.iterator();
					if (dayEntries != null) {
						Object oo = null;
						for (Iterator it = dayEntries; it.hasNext();) {
							oo = it.next();
							if (oo instanceof ScheduleEntry) {
								oldEntries.add((ScheduleEntry) oo);
							}
						}
					}
				}
			}
			for (int i = 0; i < oldEntries.size(); i++) {
				scheduleModel.removeEntry(oldEntries.get(i));
			}
			
			scheduleModel.setMode(DEFAULT_MODE);
			setHtmlSchedule(id, getHtmlSchedule(id));
		}
		
		return setEntriesForSchedule(id, new ArrayList<CalScheduleEntry>(), false);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, HtmlSchedule> getHtmlScheduleCache() {
		if (schedules == null) {
			schedules = new HashMap<String, HtmlSchedule>();
		}
		
		return schedules;
	}
	
	private HtmlSchedule getHtmlScheduleFromCache(String id) {
		if (id == null) {
			return null;
		}
		
		Map<String, HtmlSchedule> cache = getHtmlScheduleCache();
		if (cache == null) {
			return null;
		}
		
		try {
			return cache.get(id);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean setHtmlSchedule(String id, HtmlSchedule schedule) {
		if (id == null) {
			return false;
		}
		
		Map<String, HtmlSchedule> cache = getHtmlScheduleCache();
		if (cache == null) {
			return false;
		}
		
		try {
			cache.put(id, schedule);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private HtmlSchedule getHtmlSchedule(String id) {
		HtmlSchedule schedule = getHtmlScheduleFromCache(id);
		if (schedule == null) {
			schedule = new HtmlSchedule();
			
			ScheduleModel model = new SimpleScheduleModel();
			model.setSelectedDate(new Date(System.currentTimeMillis()));
			model.setMode(DEFAULT_MODE);
			model.refresh();
			schedule.setModel(model);
			
			setHtmlSchedule(id, schedule);
		}
		return schedule;
	}
	
	public boolean addCalendarEntryForInfoWindow(CalScheduleEntry entry) {
		if (entry == null) {
			return false;
		}
		
		this.entry = entry;
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public CalScheduleEntry getCalendarEntry(String entryId, String scheduleId, Locale locale) {
		if (entryId == null || scheduleId == null) {
			return null;
		}
		
		HtmlSchedule schedule = getHtmlSchedule(scheduleId);
		if (schedule == null) {
			return null;
		}
		ScheduleModel model = schedule.getModel();
		if (model == null) {
			return null;
		}
		
		Iterator days = model.iterator();
		if (days == null) {
			return null;
		}
		
		Object o = null;
		Object oo = null;
		DefaultScheduleEntry entry = null;
		ScheduleDay day = null;
		for (Iterator it = days; (it.hasNext() && entry == null);) {
			o = it.next();
			if (o instanceof ScheduleDay) {
				day = (ScheduleDay) o;
				
				Iterator entries = day.iterator();
				if (entries != null) {
					for (Iterator itt = entries; (itt.hasNext() && entry == null);) {
						oo = itt.next();
						if (oo instanceof DefaultScheduleEntry) {
							entry = (DefaultScheduleEntry) oo;
							if (!(entryId.equals(entry.getId()))) {
								entry = null;
							}
						}
					}
				}
			}
		}
		
		if (entry == null) {
			return null;
		}
		
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		CalScheduleEntry info = new CalScheduleEntry();
		IWTimestamp date = new IWTimestamp(entry.getStartTime());
		IWTimestamp endDate = new IWTimestamp(entry.getEndTime());
		
		info.setId(String.valueOf(entry.getId()));
		info.setEntryName(entry.getTitle());
		
		info.setEntryDate(date.getDateString(CalendarConstants.DATE_PATTERN));
		info.setEntryEndDate(endDate.getDateString(CalendarConstants.DATE_PATTERN));
		
		info.setEntryTime(date.getLocaleTime(locale));
		info.setEntryEndTime(endDate.getLocaleTime(locale));
		
		info.setEntryDescription(entry.getDescription());
		
		return info;
	}
	
	public CalScheduleEntry getCalendarEntryForInfoWindow(String id) {
		if (id == null || entry == null) {
			return null;
		}
		
		if (id.equals(entry.getId())) {
			return entry;
		}
		
		return null;
	}
	
	public String[] getLocalizedDateAndTime(String date, Locale locale) {
		if (date == null || locale == null) {
			return null;
		}
		
		IWTimestamp timestamp = new IWTimestamp(date);
		
		String[] localizations = new String[2];
		localizations[0] = timestamp.getLocaleDate(locale);
		localizations[1] = timestamp.getLocaleTime(locale, DateFormat.SHORT);
	
		return localizations;
	}
	
	private void setLocalizedDate(CalScheduleEntry entry, String date, Locale locale, boolean startTime) {
		String[] localizedTime = getLocalizedDateAndTime(date, locale);
		if (localizedTime == null) {
			return;
		}
		
		if (startTime) {
			entry.setLocalizedDate(localizedTime[0]);
			entry.setLocalizedTime(localizedTime[1]);
		}
		else {
			entry.setLocalizedEndDate(localizedTime[0]);
			entry.setLocalizedEndTime(localizedTime[1]);
		}
	}
	
	public boolean removeCalendar(String instanceId) {
		if (instanceId == null) {
			return false;
		}
		
		Map<String, HtmlSchedule> cache = getHtmlScheduleCache();
		if (cache == null) {
			return false;
		}
		
		try {
			cache.remove(instanceId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}