package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.myfaces.custom.schedule.model.DefaultScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;
import org.apache.myfaces.custom.schedule.model.SimpleScheduleModel;
import org.jdom.Document;

import com.idega.business.IBOSessionBean;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.util.CoreUtil;

public class ScheduleSessionBean extends IBOSessionBean implements ScheduleSession{
	private Map<String, HtmlSchedule> htmlSchedules = new HashMap<String, HtmlSchedule>();
	HtmlSchedule htmlSchedule = null;
	private SimpleDateFormat simpleDate = null;	
	private List<CalScheduleEntry> entriesInSchedule = null;
	private SimpleDateFormat localizedDate = null;
	private static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final long WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;
	private static final long MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 31;	

	private static final int DEFAULT_MODE = ScheduleModel.MONTH;
	
	private void changeMode(String id, int mode){
		htmlSchedules.get(id).getModel().setMode(mode);
		htmlSchedules.get(id).getModel().refresh();		
//		htmlSchedule.getModel().setMode(mode);
//		htmlSchedule.getModel().refresh();		

	}

	public Document changeModeToDayAndGetScheduleDOM(String id){
		changeMode(id, ScheduleModel.DAY);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToDayAndGetListOfEntries(String id){
		changeMode(id, ScheduleModel.DAY);
		Calendar calendar = Calendar.getInstance();
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(htmlSchedules.get(id).getModel().getSelectedDate());
//		currentDate.setTime(htmlSchedule.getModel().getSelectedDate());
		
		List<CalScheduleEntry> entriesOfSelectedDay = new ArrayList<CalScheduleEntry>();
		
		if (simpleDate == null){
			simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 
		}
		for (int i = 0; i < entriesInSchedule.size(); i++) {
			String entryDateString = entriesInSchedule.get(i).getEntryDate();
			Date entryDate = null;                
			try {
				entryDate = simpleDate.parse(entryDateString);
				calendar.setTime(entryDate);
				if((calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) && 
					(calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) && 
					(calendar.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH))){
						entriesOfSelectedDay.add(entriesInSchedule.get(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entriesOfSelectedDay;
	}
	
	public Document changeModeToMonthAndGetScheduleDOM(String id){
		changeMode(id, ScheduleModel.MONTH);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToMonthAndGetListOfEntries(String id){
		changeMode(id, ScheduleModel.MONTH);
		Calendar calendar = Calendar.getInstance();
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(htmlSchedules.get(id).getModel().getSelectedDate());
//		currentDate.setTime(htmlSchedule.getModel().getSelectedDate());
		
		List<CalScheduleEntry> entriesOfSelectedMonth = new ArrayList<CalScheduleEntry>();
		
		if (simpleDate == null){
			simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 
		}
		for (int i = 0; i < entriesInSchedule.size(); i++) {
			String entryDateString = entriesInSchedule.get(i).getEntryDate();
			Date entryDate = null;
			try {
				entryDate = simpleDate.parse(entryDateString);
				calendar.setTime(entryDate);
				if((calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) && 
					(calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH))){
					entriesOfSelectedMonth.add(entriesInSchedule.get(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entriesOfSelectedMonth;
	}	

	public Document changeModeToWorkweekAndGetScheduleDOM(String id){
		changeMode(id, ScheduleModel.WORKWEEK);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToWorkweekAndGetListOfEntries(String id){
		return getEntriesForCurrentWeek(id, true);
	}		

	public Document changeModeToWeekAndGetScheduleDOM(String id){
		changeMode(id, ScheduleModel.WEEK);
		return getScheduleDOM(id);
	}
	
	public List<CalScheduleEntry> changeModeToWeekAndGetListOfEntries(String id){
		return getEntriesForCurrentWeek(id, false);
	}	
	
	private List<CalScheduleEntry> getEntriesForCurrentWeek(String id, boolean workdaysOnly){
		Calendar calendar = Calendar.getInstance();
		
		Calendar beginingOfTheWeek = Calendar.getInstance();
		beginingOfTheWeek.setTime(htmlSchedules.get(id).getModel().getSelectedDate());
//		beginingOfTheWeek.setTime(htmlSchedule.getModel().getSelectedDate());
		beginingOfTheWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		beginingOfTheWeek.set(Calendar.HOUR_OF_DAY, 0);
		beginingOfTheWeek.set(Calendar.MINUTE, 0);
		beginingOfTheWeek.set(Calendar.SECOND, 0);
		beginingOfTheWeek.set(Calendar.MILLISECOND, 0);
		
		Calendar endOfTheWeek = Calendar.getInstance();
		endOfTheWeek.setTime(htmlSchedules.get(id).getModel().getSelectedDate());
//		endOfTheWeek.setTime(htmlSchedule.getModel().getSelectedDate());
		if (workdaysOnly){
			htmlSchedules.get(id).getModel().setMode(ScheduleModel.WORKWEEK);
			htmlSchedules.get(id).getModel().refresh();
//			htmlSchedule.getModel().setMode(ScheduleModel.WORKWEEK);
//			htmlSchedule.getModel().refresh();
			endOfTheWeek.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		}
		else{
			htmlSchedules.get(id).getModel().setMode(ScheduleModel.WEEK);
			htmlSchedules.get(id).getModel().refresh();
//			htmlSchedule.getModel().setMode(ScheduleModel.WEEK);
//			htmlSchedule.getModel().refresh();
			endOfTheWeek.setTimeInMillis(endOfTheWeek.getTimeInMillis()+WEEK_IN_MILLISECONDS);
			endOfTheWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		}
		endOfTheWeek.set(Calendar.HOUR_OF_DAY, 0);
		endOfTheWeek.set(Calendar.MINUTE, 0);
		endOfTheWeek.set(Calendar.SECOND, 0);
		endOfTheWeek.set(Calendar.MILLISECOND, 0);			
		
		List<CalScheduleEntry> entriesOfSelectedWorkweek = new ArrayList<CalScheduleEntry>();
				
		if (simpleDate == null){
			simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"); 
		}
		if(entriesInSchedule == null){
			entriesInSchedule = new ArrayList<CalScheduleEntry>();
		} 
		for (int i = 0; i < entriesInSchedule.size(); i++) {
			String entryDateString = entriesInSchedule.get(i).getEntryDate();
			Date entryDate = null;
			try {
				entryDate = simpleDate.parse(entryDateString);
				calendar.setTime(entryDate);
				if(beginingOfTheWeek.before(calendar) && calendar.before(endOfTheWeek)){
					entriesOfSelectedWorkweek.add(entriesInSchedule.get(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entriesOfSelectedWorkweek;		
	}
	
	public Document switchToNextAndGetScheduleDOM(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();		
//		ScheduleModel model = htmlSchedule.getModel();		
		switch (model.getMode()){
			case ScheduleModel.DAY: setSelectedDateToNextDay(id); break;
			case ScheduleModel.WORKWEEK: setSelectedDateToNextWeek(id); break;
			case ScheduleModel.WEEK: setSelectedDateToNextWeek(id); break;
			case ScheduleModel.MONTH: setSelectedDateToNextMonth(id); break;
		}
		return getScheduleDOM(id);
	}	

	public List<CalScheduleEntry> switchToNextAndGetListOfEntries(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		switch (model.getMode()){
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
	
	public Document switchToPreviousAndGetScheduleDOM(String id){		
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		switch (model.getMode()){
			case ScheduleModel.DAY: setSelectedDateToPreviousDay(id); break;
			case ScheduleModel.WORKWEEK: setSelectedDateToPreviousWeek(id); break;
			case ScheduleModel.WEEK: setSelectedDateToPreviousWeek(id); break;
			case ScheduleModel.MONTH: setSelectedDateToPreviousMonth(id); break;
		}
		return getScheduleDOM(id);
	}

	public List<CalScheduleEntry> switchToPreviousAndGetListOfEntries(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		switch (model.getMode()){
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
	
	private void setSelectedDateToNextDay(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
//		htmlSchedule.setModel(model);
	}
	
	private void setSelectedDateToNextWeek(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
//		htmlSchedule.setModel(model);
	}
	
	private void setSelectedDateToNextMonth(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
//		htmlSchedule.setModel(model);
	}

	private void setSelectedDateToPreviousDay(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();		
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
//		htmlSchedule.setModel(model);
	}
	
	private void setSelectedDateToPreviousWeek(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
//		htmlSchedule.setModel(model);
	}
	
	private void setSelectedDateToPreviousMonth(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
//		ScheduleModel model = htmlSchedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);	
//		htmlSchedule.setModel(model);	
	}
	public Document getScheduleDOM(String id){
		
		
		IWContext iwc = CoreUtil.getIWContext();
		BuilderService service = null;
		try { 
			service = BuilderServiceFactory.getBuilderService(iwc);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		ScheduleModel scheduleModel = null;

		if (htmlSchedules.get(id) == null){
			htmlSchedules.put(id, new HtmlSchedule());
		}
		else{
			scheduleModel = htmlSchedules.get(id).getModel();
		}

//		if(htmlSchedule == null){
//			htmlSchedule = new HtmlSchedule();
//		}
//		else{
//			scheduleModel = htmlSchedule.getModel();
//		}
		
		
//		if(entriesInSchedule == null){
//			scheduleModel = htmlSchedules.get(id).getModel();
//		}
//		else{ 
		if(scheduleModel == null){
			scheduleModel = new SimpleScheduleModel();
			scheduleModel.setMode(DEFAULT_MODE);
			if(entriesInSchedule != null){
				for (int i = 0; i < entriesInSchedule.size(); i++) {
					CalScheduleEntry entry = entriesInSchedule.get(i);
					DefaultScheduleEntry defaultScheduleEntry = new DefaultScheduleEntry();
					if (simpleDate == null){
						simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
					}
					if (entry.getId() != null){
						defaultScheduleEntry.setId(entry.getId());
					}				
					if (entry.getEntryDate() != null){
						defaultScheduleEntry.setStartTime(simpleDate.parse(entry.getEntryDate(), new ParsePosition(0)));
					}
					if (entry.getEntryEndDate() != null){
						defaultScheduleEntry.setEndTime(simpleDate.parse(entry.getEntryEndDate(), new ParsePosition(0)));
					}
					if (entry.getEntryName() != null){
						defaultScheduleEntry.setTitle(entry.getEntryName());
					}
					if (entry.getEntryDescription() != null){
						defaultScheduleEntry.setDescription(entry.getEntryDescription());
					}
					try {
						scheduleModel.addEntry(defaultScheduleEntry);
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
	//					e.printStackTrace();
					}
				}			
			}
			scheduleModel.refresh();
			htmlSchedules.get(id).setModel(scheduleModel);			
//			htmlSchedule.setModel(scheduleModel);
		}
//		htmlSchedules.get(id).setReadonly(true);
		htmlSchedules.get(id).setReadonly(false);
//		htmlSchedule.setReadonly(false);
		Layer scheduleLayer = new Layer();
		scheduleLayer.add(htmlSchedules.get(id));
//		scheduleLayer.add(htmlSchedule);
		return service.getRenderedComponent(iwc, scheduleLayer, false);
	}

/*	
	private List setTestEntries(){
		List<CalScheduleEntry> entries = new ArrayList<CalScheduleEntry>();
		for(int i = 1; i <= 3; i++){
			CalScheduleEntry entry = new CalScheduleEntry();
			
			entry.setEntryDate("2007-09-08 "+(i*2+12)+":00:00.0");
			entry.setEntryEndDate("2007-09-08 "+(i*2+13)+":00:00.0");
			entry.setEntryName(i+"st entry name");
			entry.setEntryDescription(i+"st entry description");
			entry.setId(i+"st_entry_id");
			entries.add(entry);
		}
		return entries;
	}
*/	
	public int addEntries(List<CalScheduleEntry> entries, String id, boolean clearPreviousEntries){
/*		
		if(entries == null || entries.isEmpty()){
			entries = setTestEntries();
		}
		
		
		
*/		
		ScheduleModel scheduleModel = null;
		
		if (htmlSchedules.get(id) == null){
//		if (htmlSchedule == null){			
			htmlSchedules.put(id, new HtmlSchedule());
//			htmlSchedule = new HtmlSchedule();
		}
		
		scheduleModel = htmlSchedules.get(id).getModel();
//		scheduleModel = htmlSchedule.getModel();		
		
		if (entriesInSchedule == null){
			entriesInSchedule = new ArrayList<CalScheduleEntry>();
		}
		
		if (clearPreviousEntries) {
			entriesInSchedule.clear();
			scheduleModel.setMode(DEFAULT_MODE);
		}
		
		entriesInSchedule.addAll(entries);
		
		for (int i = 0; i < entries.size(); i++) {
			CalScheduleEntry entry = entries.get(i);
			entry = setLocalizedDate(entry);
			DefaultScheduleEntry defaultScheduleEntry = new DefaultScheduleEntry();
			if (simpleDate == null){
				simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			}
			if (entry.getId() != null){
				defaultScheduleEntry.setId(entry.getId());
			}
			if (entry.getEntryDate() != null){
				defaultScheduleEntry.setStartTime(simpleDate.parse(entry.getEntryDate(), new ParsePosition(0)));
			}
			if (entry.getEntryEndDate() != null){
				defaultScheduleEntry.setEndTime(simpleDate.parse(entry.getEntryEndDate(), new ParsePosition(0)));
			}
			if (entry.getEntryName() != null){
				defaultScheduleEntry.setTitle(entry.getEntryName());
			}
			if (entry.getEntryDescription() != null){
				defaultScheduleEntry.setDescription(entry.getEntryDescription());
			}			
			scheduleModel.addEntry(defaultScheduleEntry);
		}			
	
		scheduleModel.refresh();
		if (htmlSchedules.get(id) == null){
//		if (htmlSchedule == null){		
			htmlSchedules.put(id, new HtmlSchedule());			
//			htmlSchedule = new HtmlSchedule();
		}
		htmlSchedules.get(id).setModel(scheduleModel);		
//		htmlSchedule.setModel(scheduleModel);		
		return 0;
	}	
	
	private CalScheduleEntry setLocalizedDate(CalScheduleEntry entry){
		IWContext iwc = IWContext.getInstance();
		if (localizedDate == null)
			localizedDate = (SimpleDateFormat)DateFormat.getDateInstance(DateFormat.DEFAULT, iwc.getLocale());
		if (simpleDate == null){
			simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		}
		
		Date date = simpleDate.parse(entry.getEntryDate(), new ParsePosition(0));
		entry.setLocalizedEntryDate(localizedDate.format(date));
		date = simpleDate.parse(entry.getEntryEndDate(), new ParsePosition(0));
		entry.setLocalizedEntryEndDate(localizedDate.format(date));
		
		return entry;
	}
	
	public List<CalScheduleEntry> getListOfEntries(String id){
		return changeModeToMonthAndGetListOfEntries(id);
	}
	
}
