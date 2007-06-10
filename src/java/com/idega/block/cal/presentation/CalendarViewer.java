package com.idega.block.cal.presentation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.html.dom.HTMLLinkElementImpl;
import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.custom.schedule.HtmlSchedule;
import org.apache.myfaces.custom.schedule.model.DefaultScheduleEntry;
import org.apache.myfaces.custom.schedule.model.ScheduleModel;
import org.apache.myfaces.custom.schedule.model.SimpleScheduleModel;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericButton;

public class CalendarViewer extends IWBaseComponent{

	HtmlSchedule schedule = null;
	ScheduleModel scheduleModel = null;
	private String mode = null;
	
	public CalendarViewer() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
//		HtmlForm form = new HtmlForm();
		
		schedule = new HtmlSchedule();
//		UIComponent c = schedule;
//		SimpleScheduleModel scheduleModel = new SimpleScheduleModel();
		scheduleModel = new SimpleScheduleModel(); 
//		scheduleModel.
//		scheduleModel.setMode(ScheduleModel.MONTH);
//System.out.println("initializeComponent "+ mode);		
//		if (mode != null && mode.equals("WEEK"))
//			scheduleModel.setMode(ScheduleModel.WEEK);
//		DefaultScheduleEntry entry = new DefaultScheduleEntry();
//		Calendar calendar = GregorianCalendar.getInstance();
//		entry.setId("firstEntryId");
//		entry.setTitle("test entry");
//		entry.setStartTime(calendar.getTime());
//		calendar.add(Calendar.HOUR, 48);
//		entry.setEndTime(calendar.getTime());
//		scheduleModel.addEntry(entry);
		scheduleModel.refresh();
//		schedule.setValue(scheduleModel);
		schedule.setModel(scheduleModel);
		getChildren().add(schedule);
//		form.add(schedule);
		
		GenericButton button = new GenericButton();
		button.setValue("refresh");
		button.setOnClick("addEntry()");
//		getChildren().add(button);
		
		GenericButton monthMode = new GenericButton();
		monthMode.setValue("Month mode");
		monthMode.setOnClick("addEntry()");
//		getChildren().add(monthMode);

//		HtmlCommandLink weekModeLink = new HtmlCommandLink();
//		link
//		HTMLLinkElementImpl link = new HTMLLinkElementImpl();
//		link.
		Link weekModeLink = new Link("week mode ");
		weekModeLink.setParameter("mode", "WEEK");
		getChildren().add(weekModeLink);

//		Link workweekModeLink = new Link("workweek mode");
//		workweekModeLink.setParameter("mode", "WORKWEEK");
//		getChildren().add(workweekModeLink);
		
		
		Link monthModeLink = new Link("month mode");
		monthModeLink.setParameter("mode", "MONTH");
		getChildren().add(monthModeLink);
		HtmlCommandButton weekModeButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		weekModeButton.setValue("Month");

			
//		getChildren().add(weekModeButton);
//		getChildren().add(weekModeButton);
		
//		getChildren().add(form);2
		
		GenericButton weekMode = new GenericButton();
		weekMode.setValue("refresh");
		weekMode.setOnClick("addEntry()");
//		getChildren().add(weekMode);

		GenericButton dayMode  = new GenericButton();
		button.setValue("refresh");
		button.setOnClick("addEntry()");
//		getChildren().add(button);
		getFacets().put("schedule", schedule);
		
//		Date selectedDate
//		
//		scheduleModel.setSelectedDate(scheduleModel.getSelectedDate().getDay()+1));
//		System.out.println("selected date:");
//		System.out.println(scheduleModel.getSelectedDate());
	}
	@Override
	public void decode(FacesContext context) {
		// TODO Auto-generated method stub
		super.decode(context);
		mode = (String)context.getExternalContext().getRequestParameterMap().get("mode");
System.out.println("decode "+mode);
		
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		// TODO Auto-generated method stub
		super.encodeBegin(context);
		schedule = (HtmlSchedule)getFacet("schedule");
		scheduleModel = schedule.getModel();
		mode = (String)context.getExternalContext().getRequestParameterMap().get("mode");
		if(mode != null){
			if(mode.equals("WEEK"))
				scheduleModel.setMode(ScheduleModel.WEEK);
			if(mode.equals("MONTH"))
				scheduleModel.setMode(ScheduleModel.MONTH);
//			if(mode.equals("WORKWEEK"))
//				scheduleModel.setMode(ScheduleModel.WORKWEEK);

		}
		else
			scheduleModel.setMode(ScheduleModel.MONTH);
		scheduleModel.refresh();
System.out.println("encodeBegin "+mode);
	}
	
	@Override
	public Object saveState(FacesContext ctx) {
		Object values[] = new Object[3];
		values[0] = super.saveState(ctx);
		values[1] = mode;
//		values[2] = sessionKey;
		
		return values;
	}
	
	@Override
	public void restoreState(FacesContext ctx, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(ctx, values[0]);
		mode = (String) values[1];
//		sessionKey = (String) values[2];
	}
}
