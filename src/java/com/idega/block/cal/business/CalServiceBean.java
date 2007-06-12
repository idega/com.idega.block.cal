package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class CalServiceBean extends IBOServiceBean implements CalService {

	private HtmlSchedule schedule = null;
	private SimpleDateFormat simpleDate = null;
	private int dateMode = -1;
	private static final int DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
	private static final int WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;
	private static final int MONTH_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 30;
	
//	private ScheduleModel scheduleModel = null;
	
	public void setConnectionData(String serverName, String login,
			String password) {
		// TODO Auto-generated method stub
	}
	
	public List getCalendarParametersList(){
		
		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = IWContext.getInstance();
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		List <CalendarLedgerBMPBean>allLedgers = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllLedgers();
		
		List <CalendarEntryTypeBMPBean>allEntryTypes = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllEntryTypes();
		
		for (int i = 0; i < allLedgers.size(); i++) {
			CalendarLedgerBMPBean ledger = allLedgers.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
		}

		return calendarParameters;
	}
	
	public List getCalendarParameters(String id){

		List <CalendarLedgersAndTypes>calendarParameters = new ArrayList<CalendarLedgersAndTypes>();
		
		IWContext iwc = IWContext.getInstance();
		LedgerVariationsHandler ledgerVariationsHandler = new DefaultLedgerVariationsHandler();
		List <CalendarLedgerBMPBean>ledgersByGroupId = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getLedgersByGroupId(id);
		
		List <CalendarEntryTypeBMPBean>allEntryTypes = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getAllEntryTypes();
		
		for (int i = 0; i < ledgersByGroupId.size(); i++) {
			CalendarLedgerBMPBean ledger = ledgersByGroupId.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+ledger.getID(), ledger.getName(), "L"));
		}
		
		for (int i = 0; i < allEntryTypes.size(); i++) {
			CalendarEntryTypeBMPBean entryType = allEntryTypes.get(i);
			calendarParameters.add(new CalendarLedgersAndTypes(""+entryType.getID(), entryType.getName(), "T"));
		}
				
		return calendarParameters;
	}
	
	public Document setCheckedParameters(List checkedParameters){
		
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
		
		List<CalendarEntryBMPBean> entriesToDisplay = ((DefaultLedgerVariationsHandler)ledgerVariationsHandler).getCalBusiness(iwc).getEntriesByLedgersAndEntryTypes(listOfEntryTypesIds, listOfLedgerIds);
		
		List<CalEntry> result = new ArrayList<CalEntry>();

		for (int i = 0; i < entriesToDisplay.size(); i++) {
			CalendarEntryBMPBean entry = entriesToDisplay.get(i);
			if(checkIfTypeIsCorrect(entry, listOfEntryTypesIds)){
//				result.add(new CalEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), getDate(entry.getStringColumnValue("CAL_ENTRY_DATE")), getDate(entry.getStringColumnValue("CAL_ENTRY_END_DATE")),getTime(entry.getStringColumnValue("CAL_ENTRY_DATE")), getTime(entry.getStringColumnValue("CAL_ENTRY_END_DATE")), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
				result.add(new CalEntry(entry.getStringColumnValue("CAL_ENTRY_NAME"), entry.getStringColumnValue("CAL_ENTRY_DATE"), entry.getStringColumnValue("CAL_ENTRY_END_DATE"), entry.getStringColumnValue("CAL_ENTRY_REPEAT"), entry.getStringColumnValue("CAL_TYPE_NAME")));
			}
		}
		return getSchedule(result, false);
	}
	
	private boolean checkIfTypeIsCorrect(CalendarEntryBMPBean entry, List<String> entryTypesIds){
		String typeId = entry.getStringColumnValue("CAL_TYPE_ID");
		for (int i = 0; i < entryTypesIds.size(); i++) {
			if(entryTypesIds.get(i).equals(typeId))
				return true;
		}
		return false;
	}
	
//	private String getDate(String entryDate){
//		String date = entryDate.substring(0, 10);
//		return date.replaceAll("-", "");
//	}
//
//	private String getTime(String entryDate){
//		return entryDate.substring(11,16);
//	}
	
	public Document getSchedule(List<CalEntry> result, boolean usePreviousEntries){
		IWContext iwc = CoreUtil.getIWContext();
		BuilderService service = null;
		try { 
			service = BuilderServiceFactory.getBuilderService(iwc);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if (schedule == null)
			schedule = new HtmlSchedule();
		ScheduleModel scheduleModel = null;
		
		if(usePreviousEntries){
			scheduleModel = schedule.getModel();
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
		if(dateMode == -1){
			scheduleModel.setMode(ScheduleModel.MONTH);
			dateMode = ScheduleModel.MONTH;
		}
		else{
			scheduleModel.setMode(dateMode);
		}
		scheduleModel.refresh();
		schedule.setModel(scheduleModel);
		Layer scheduleLayer = new Layer();
		scheduleLayer.add(schedule);
		return service.getRenderedComponent(iwc, scheduleLayer, false);
	}

	public Document changeModeToDay(){
		dateMode = ScheduleModel.DAY;
		return getSchedule(null, true);
	}

	public Document changeModeToWorkweek(){
		dateMode = ScheduleModel.WORKWEEK;
		return getSchedule(null, true);
	}

	public Document changeModeToWeek(){
		dateMode = ScheduleModel.WEEK;
		return getSchedule(null, true);
	}

	public Document changeModeToMonth(){
		dateMode = ScheduleModel.MONTH;
		return getSchedule(null, true);
	}

	public Document getNext(){
		ScheduleModel model = schedule.getModel();
		switch (model.getMode()){
			case ScheduleModel.DAY: setSelectedDateToNextDay(); break;
			case ScheduleModel.WORKWEEK: setSelectedDateToNextWeek(); break;
			case ScheduleModel.WEEK: setSelectedDateToNextWeek(); break;
			case ScheduleModel.MONTH: setSelectedDateToNextMonth(); break;
		}
		return getSchedule(null, true);
	}
	
	public Document getPrevious(){
		ScheduleModel model = schedule.getModel();
		switch (model.getMode()){
			case ScheduleModel.DAY: setSelectedDateToPreviousDay(); break;
			case ScheduleModel.WORKWEEK: setSelectedDateToPreviousWeek(); break;
			case ScheduleModel.WEEK: setSelectedDateToPreviousWeek(); break;
			case ScheduleModel.MONTH: setSelectedDateToPreviousMonth(); break;
		}
		return getSchedule(null, true);
	}

	private void setSelectedDateToNextDay(){
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		schedule.setModel(model);
	}
	
	private void setSelectedDateToNextWeek(){
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		schedule.setModel(model);
	}
	
	private void setSelectedDateToNextMonth(){
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()-MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		schedule.setModel(model);
	}

	private void setSelectedDateToPreviousDay(){
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+DAY_IN_MILLISECONDS);
		model.setSelectedDate(date);
		schedule.setModel(model);
	}
	
	private void setSelectedDateToPreviousWeek(){
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+WEEK_IN_MILLISECONDS);
		model.setSelectedDate(date);
		schedule.setModel(model);
	}
	
	private void setSelectedDateToPreviousMonth(){
		ScheduleModel model = schedule.getModel();
		Date date = model.getSelectedDate();
		date.setTime(date.getTime()+MONTH_IN_MILLISECONDS);
		model.setSelectedDate(date);
		schedule.setModel(model);
	}

}
