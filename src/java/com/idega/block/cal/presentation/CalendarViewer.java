package com.idega.block.cal.presentation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.model.DefaultScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;
import org.apache.myfaces.custom.schedule.model.SimpleScheduleModel;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.ui.GenericButton;

public class CalendarViewer extends IWBaseComponent{

	HtmlSchedule schedule = null;
	ScheduleModel scheduleModel = null;
	protected void initializeComponent(FacesContext context) {
		schedule = new HtmlSchedule();
//		SimpleScheduleModel scheduleModel = new SimpleScheduleModel();
		scheduleModel = new SimpleScheduleModel(); 
		scheduleModel.setMode(ScheduleModel.MONTH);
		
		DefaultScheduleEntry entry = new DefaultScheduleEntry();
		Calendar calendar = GregorianCalendar.getInstance();
		entry.setId("firstEntryId");
		entry.setTitle("test entry");
		entry.setStartTime(calendar.getTime());
		calendar.add(Calendar.MINUTE, 45);
		entry.setEndTime(calendar.getTime());
		scheduleModel.addEntry(entry);
		scheduleModel.refresh();
//		schedule.setValue(scheduleModel);
		schedule.setModel(scheduleModel);
		
		getChildren().add(schedule);
		GenericButton button = new GenericButton();
		button.setValue("refresh");
//		button.setOnClick(action)
	}

}
