package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.model.DefaultScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;
import org.apache.myfaces.custom.schedule.model.SimpleScheduleModel;
import org.jdom.Document;

import com.idega.block.cal.data.CalendarEntryBMPBean;
import com.idega.block.cal.data.CalendarEntryTypeBMPBean;
import com.idega.block.cal.data.CalendarLedgerBMPBean;
import com.idega.business.IBOServiceBean;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.util.CoreUtil;

public class ScheduleSessionBean extends IBOServiceBean implements ScheduleSession{

//	private HtmlSchedule schedule = null;
	private Map<String, HtmlSchedule> htmlSchedules = new HashMap<String, HtmlSchedule>();
	private Map<String, Integer> dateMode = new HashMap<String, Integer>();
	private SimpleDateFormat simpleDate = null;
//	private int dateMode = -1;
	private static final int DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final int WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;
	private static final int MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 30;	
	
	public Document changeModeToDay(String id){
		dateMode.put(id, ScheduleModel.DAY);
		return getSchedule(id, null, true);
	}

	public Document changeModeToWorkweek(String id){
		dateMode.put(id, ScheduleModel.WORKWEEK);
		return getSchedule(id, null, true);
	}

	public Document changeModeToWeek(String id){
		dateMode.put(id, ScheduleModel.WEEK);
		return getSchedule(id, null, true);
	}

	public Document changeModeToMonth(String id){
		dateMode.put(id, ScheduleModel.MONTH);
		return getSchedule(id, null, true);
	}

	public Document getNext(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		switch (model.getMode()){
			case ScheduleModel.DAY: setSelectedDateToNextDay(id); break;
			case ScheduleModel.WORKWEEK: setSelectedDateToNextWeek(id); break;
			case ScheduleModel.WEEK: setSelectedDateToNextWeek(id); break;
			case ScheduleModel.MONTH: setSelectedDateToNextMonth(id); break;
		}
		return getSchedule(id, null, true);
	}	
	
	public Document getPrevious(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		switch (model.getMode()){
			case ScheduleModel.DAY: setSelectedDateToPreviousDay(id); break;
			case ScheduleModel.WORKWEEK: setSelectedDateToPreviousWeek(id); break;
			case ScheduleModel.WEEK: setSelectedDateToPreviousWeek(id); break;
			case ScheduleModel.MONTH: setSelectedDateToPreviousMonth(id); break;
		}
		return getSchedule(id, null, true);
	}

	private void setSelectedDateToNextDay(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
	}
	
	private void setSelectedDateToNextWeek(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
	}
	
	private void setSelectedDateToNextMonth(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
	}

	private void setSelectedDateToPreviousDay(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
	}
	
	private void setSelectedDateToPreviousWeek(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
	}
	
	private void setSelectedDateToPreviousMonth(String id){
		ScheduleModel model = htmlSchedules.get(id).getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		htmlSchedules.get(id).setModel(model);
	}
	public Document getSchedule(String id, List<CalEntry> result, boolean usePreviousEntries){
		IWContext iwc = CoreUtil.getIWContext();
		BuilderService service = null;
		try { 
			service = BuilderServiceFactory.getBuilderService(iwc);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if (htmlSchedules.get(id) == null){
			htmlSchedules.put(id, new HtmlSchedule());
		}
		ScheduleModel scheduleModel = null;
		
		if(usePreviousEntries){
			scheduleModel = htmlSchedules.get(id).getModel();
		}
		else{ 
			scheduleModel = new SimpleScheduleModel();
			
			for (int i = 0; i < result.size(); i++) {
				CalEntry calEntry = result.get(i);
				DefaultScheduleEntry scheduleEntry = new DefaultScheduleEntry();
				if (simpleDate == null){
					simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
				}
				scheduleEntry.setStartTime(simpleDate.parse(calEntry.getEntryDate(), new ParsePosition(0)));
				scheduleEntry.setEndTime(simpleDate.parse(calEntry.getEntryEndDate(), new ParsePosition(0)));
				scheduleEntry.setTitle(calEntry.getEntryName());
				scheduleModel.addEntry(scheduleEntry);
			}			
		}
		if(dateMode.get(id) == -1){
			scheduleModel.setMode(ScheduleModel.MONTH);
			dateMode.put(id, ScheduleModel.MONTH);
		}
		else{
			scheduleModel.setMode(dateMode.get(id));
		}
		scheduleModel.refresh();
		htmlSchedules.get(id).setModel(scheduleModel);
		Layer scheduleLayer = new Layer();
		scheduleLayer.add(htmlSchedules.get(id));
		return service.getRenderedComponent(iwc, scheduleLayer, false);
	}
	public List getCalendarParameters(String id){

		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = IWContext.getInstance();
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		
		CalBusiness calBusiness = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc);
		
		List ledgersByGroupId = calBusiness.getLedgersByGroupId(id);
		
		List allEntryTypes = calBusiness.getAllEntryTypes();
		
		for (int i = 0; i < ledgersByGroupId.size(); i++) {
			CalendarLedgerBMPBean ledger = (CalendarLedgerBMPBean)ledgersByGroupId.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = (CalendarEntryTypeBMPBean)allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
		}
				
		return calendarParameters;
	}
	
	public Document setCheckedParameters(String id, List checkedParameters){
		
		IWContext iwc = IWContext.getInstance();
		String parameter = null;
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		
		List<String> listOfLedgerIds = new ArrayList<String>();
		List<String> listOfEntryTypesIds = new ArrayList<String>();
		
		for (int i = 0; i < checkedParameters.size(); i++) {
//			System.out.println(checkedParameters.get(i));
			parameter = (String)(checkedParameters.get(i));
			if(parameter.substring(0, 1).equals("L")){
				listOfLedgerIds.add(parameter.substring(1));
			}
			if(parameter.substring(0, 1).equals("T")){
				listOfEntryTypesIds.add(parameter.substring(1));
			}			
		}
		
		CalBusiness calBusiness = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc);
		
		List entriesToDisplay = calBusiness.getEntriesByLedgersAndEntryTypes(listOfEntryTypesIds, listOfLedgerIds);
		
		List<CalEntry> result = new ArrayList<CalEntry>();

		for (int i = 0; i < entriesToDisplay.size(); i++) {
			CalendarEntryBMPBean entry = (CalendarEntryBMPBean)entriesToDisplay.get(i);
			if(checkIfTypeIsCorrect(entry, listOfEntryTypesIds)){
//				result.add(new CalEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), getDate(entry.getStringColumnValue("CAL_ENTRY_DATE")), getDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE")),getTime(entry.getStringColumnValue("CAL_ENTRY_DATE")), getTime(entry.getStringColumnValue("CAL_ENTRY_END_DATE")), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
				result.add(new CalEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), entry.getStringColumnValue("CAL_ENTRY_DATE"), entry.getStringColumnValue("CAL_ENTRY_END_DATE"), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
			}
		}
		dateMode.put(id, ScheduleModel.MONTH);
		return getSchedule(id, result, false);
	}
	
	private boolean checkIfTypeIsCorrect(CalendarEntryBMPBean entry, List<String> entryTypesIds){
		String typeId = entry.getStringColumnValue("CAL_TYPE_ID");
		for (int i = 0; i < entryTypesIds.size(); i++) {
			if(entryTypesIds.get(i).equals(typeId))
				return true;
		}
		return false;
	}	
}
