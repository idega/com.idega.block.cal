package com.idega.block.cal.presentation;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;

//public class CalendarViewer extends IWBaseComponent{
public class CalendarViewer extends Block{

	private static final String CALENDAR_VIEWER_SCHEDULE_ID = "calendarViewerScheduleId";
	private static final String BUTTON_NEXT_VALUE = "Next";
	private static final String BUTTON_PREVIOUS_VALUE = "Previous";
	private static final String BUTTON_DAY_VALUE = "Day mode";
	private static final String BUTTON_WORKWEEK_VALUE = "Work week";
	private static final String BUTTON_WEEK_VALUE = "Week";
	private static final String BUTTON_MONTH_VALUE = "Month";	
	
	public void main(IWContext iwc) {
		Layer main = new Layer();
		Layer schedule = new Layer();
		schedule.setId(CALENDAR_VIEWER_SCHEDULE_ID);
		Layer calendarViewerButtons = new Layer();
		
		HtmlCommandButton buttonNext = new HtmlCommandButton();
		buttonNext.setValue(BUTTON_NEXT_VALUE);
		
		HtmlCommandButton buttonPrevious = new HtmlCommandButton();
		buttonNext.setValue(BUTTON_PREVIOUS_VALUE);
		
		HtmlCommandButton buttonDay = new HtmlCommandButton();
		buttonNext.setValue(BUTTON_DAY_VALUE);
		
		HtmlCommandButton buttonWeek = new HtmlCommandButton();
		buttonNext.setValue(BUTTON_WEEK_VALUE);
		
		HtmlCommandButton buttonWorkweek = new HtmlCommandButton();
		buttonNext.setValue(BUTTON_WORKWEEK_VALUE);
		
		HtmlCommandButton buttonMonth = new HtmlCommandButton();
		buttonNext.setValue(BUTTON_MONTH_VALUE);
		
		calendarViewerButtons.add(buttonNext);
		calendarViewerButtons.add(buttonPrevious);
		calendarViewerButtons.add(buttonDay);
		calendarViewerButtons.add(buttonWorkweek);
		calendarViewerButtons.add(buttonWeek);
		calendarViewerButtons.add(buttonMonth);
		
		main.add(schedule);
//		main.add(calendarViewerButtons);
		
		add(main);
//		main.setId(CALENDAR_VIEWER_CONTAINER_ID);
		
	} 
	
	
	
//	HtmlSchedule schedule = null;
//	ScheduleModel scheduleModel = null;
//	private String mode = null;
	
//	public CalendarViewer() {
////		super();
//		// TODO Auto-generated constructor stub
//	}

//	protected void initializeComponent(FacesContext context) {
//		
//		Application application = context.getApplication();
////		HtmlForm form = new HtmlForm();
//		
//		schedule = new HtmlSchedule();
////		UIComponent c = schedule;
////		SimpleScheduleModel scheduleModel = new SimpleScheduleModel();
//		scheduleModel = new SimpleScheduleModel();
////System.out.println(scheduleModel.getSelectedDate().se);
//		
//		Date date = scheduleModel.getSelectedDate();
////		date.setMonth(date.getMonth()+1);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
////		calendar.get(Calendar.MONTH);
//		
//		scheduleModel.setSelectedDate(date);
////		scheduleModel.setSelectedDate(arg0)
////		scheduleModel.
////		scheduleModel.setMode(ScheduleModel.MONTH);
////		if (mode != null && mode.equals("WEEK"))
////			scheduleModel.setMode(ScheduleModel.WEEK);
////		DefaultScheduleEntry entry = new DefaultScheduleEntry();
////		Calendar calendar = GregorianCalendar.getInstance();
////		entry.setId("firstEntryId");
////		entry.setTitle("test entry");
////		entry.setStartTime(calendar.getTime());
////		calendar.add(Calendar.HOUR, 48);
////		entry.setEndTime(calendar.getTime());
////		scheduleModel.addEntry(entry);
//		scheduleModel.refresh();
////		schedule.setValue(scheduleModel);
//		schedule.setModel(scheduleModel);
//		getChildren().add(schedule);
////		form.add(schedule);
//		
//		GenericButton button = new GenericButton();
//		button.setValue("refresh");
//		button.setOnClick("addEntry()");
////		getChildren().add(button);
//		
//		GenericButton monthMode = new GenericButton();
//		monthMode.setValue("Month mode");
//		monthMode.setOnClick("addEntry()");
////		getChildren().add(monthMode);
//
////		HtmlCommandLink weekModeLink = new HtmlCommandLink();
////		link
////		HTMLLinkElementImpl link = new HTMLLinkElementImpl();
////		link.
//		Link weekModeLink = new Link("week mode ");
//		weekModeLink.setParameter("mode", "WEEK");
//		getChildren().add(weekModeLink);
//
////		Link workweekModeLink = new Link("workweek mode");
////		workweekModeLink.setParameter("mode", "WORKWEEK");
////		getChildren().add(workweekModeLink);
//		
//		
//		Link monthModeLink = new Link("month mode");
//		monthModeLink.setParameter("mode", "MONTH");
//		getChildren().add(monthModeLink);
//		HtmlCommandButton weekModeButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
//		weekModeButton.setValue("Month");
//
//			
////		getChildren().add(weekModeButton);
////		getChildren().add(weekModeButton);
//		
////		getChildren().add(form);2
//		
//		GenericButton weekMode = new GenericButton();
//		weekMode.setValue("refresh");
//		weekMode.setOnClick("addEntry()");
////		getChildren().add(weekMode);
//
//		GenericButton dayMode  = new GenericButton();
//		button.setValue("refresh");
//		button.setOnClick("addEntry()");
////		getChildren().add(button);
//		getFacets().put("schedule", schedule);
//		
////		Date selectedDate
////		
////		scheduleModel.setSelectedDate(scheduleModel.getSelectedDate().getDay()+1));
////		System.out.println("selected date:");
////		System.out.println(scheduleModel.getSelectedDate());
////		BuilderService service = BuilderServiceFactory.getBuilderService(CoreUtil.getIWContext());
////		service.getRenderedComponent(component, iwc, false);
//	}
//	@Override
//	public void decode(FacesContext context) {
//		// TODO Auto-generated method stub
//		super.decode(context);
//		mode = (String)context.getExternalContext().getRequestParameterMap().get("mode");
//System.out.println("decode "+mode);
//		
//	}
	
//	@Override
//	public void encodeBegin(FacesContext context) throws IOException {
//		// TODO Auto-generated method stub
//		super.encodeBegin(context);
//		schedule = (HtmlSchedule)getFacet("schedule");
//		scheduleModel = schedule.getModel();
//		mode = (String)context.getExternalContext().getRequestParameterMap().get("mode");
//		if(mode != null){
//			if(mode.equals("WEEK"))
//				scheduleModel.setMode(ScheduleModel.WEEK);
//			if(mode.equals("MONTH"))
//				scheduleModel.setMode(ScheduleModel.MONTH);
////			if(mode.equals("WORKWEEK"))
////				scheduleModel.setMode(ScheduleModel.WORKWEEK);
//
//		}
//		else
//			scheduleModel.setMode(ScheduleModel.MONTH);
//		scheduleModel.refresh();
////System.out.println("encodeBegin "+mode);
//	}
//	
//	
//	
//	@Override
//	public Object saveState(FacesContext ctx) {
//		Object values[] = new Object[3];
//		values[0] = super.saveState(ctx);
//		values[1] = mode;
////		values[2] = sessionKey;
//		
//		return values;
//	}
//	
//	@Override
//	public void restoreState(FacesContext ctx, Object state) {
//		Object values[] = (Object[]) state;
//		super.restoreState(ctx, values[0]);
//		mode = (String) values[1];
////		sessionKey = (String) values[2];
//	}
}
