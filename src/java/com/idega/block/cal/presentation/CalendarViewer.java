package com.idega.block.cal.presentation;

import java.util.List;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.idegaweb.IWBundle;
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
	
	private String server = null;
	private String user = null;
	private String password = null;	
	
	private List<String> uniqueIds = null;
	
	private List entries = null;
	
	private boolean remoteMode = false;	
	
	
	public void main(IWContext iwc) {
		Layer main = new Layer();
		main.setId(CALENDAR_VIEWER_SCHEDULE_ID);
		Layer schedule = new Layer();
		schedule.setId(this.getId());
//		schedule.setId(CALENDAR_VIEWER_SCHEDULE_ID);
//		Layer calendarViewerButtons = new Layer();
//		
//		HtmlCommandButton buttonNext = new HtmlCommandButton();
//		buttonNext.setValue(BUTTON_NEXT_VALUE);
//		
//		HtmlCommandButton buttonPrevious = new HtmlCommandButton();
//		buttonNext.setValue(BUTTON_PREVIOUS_VALUE);
//		
//		HtmlCommandButton buttonDay = new HtmlCommandButton();
//		buttonNext.setValue(BUTTON_DAY_VALUE);
//		
//		HtmlCommandButton buttonWeek = new HtmlCommandButton();
//		buttonNext.setValue(BUTTON_WEEK_VALUE);
//		
//		HtmlCommandButton buttonWorkweek = new HtmlCommandButton();
//		buttonNext.setValue(BUTTON_WORKWEEK_VALUE);
//		
//		HtmlCommandButton buttonMonth = new HtmlCommandButton();
//		buttonNext.setValue(BUTTON_MONTH_VALUE);
//		
//		calendarViewerButtons.add(buttonNext);
//		calendarViewerButtons.add(buttonPrevious);
//		calendarViewerButtons.add(buttonDay);
//		calendarViewerButtons.add(buttonWorkweek);
//		calendarViewerButtons.add(buttonWeek);
//		calendarViewerButtons.add(buttonMonth);
		
		main.add(schedule);
//		main.add(calendarViewerButtons);
		
		add(main);
		setID();
		addJavaScript(iwc);
//		main.setId(CALENDAR_VIEWER_CONTAINER_ID);
		
	}
	
	public void setEntries(){
		
	}
	
	private void addJavaScript(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		
		AddResource resourceAdder = AddResourceFactory.getInstance(iwc);
		
//		//	"Helpers"
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/CalendarList.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/cal.js"));
		
		//	DWR
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, CalendarConstants.CALENDAR_SERVICE_DWR_INTERFACE_SCRIPT);
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, "/dwr/engine.js");
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, "/dwr/interface/ScheduleSession.js");
		resourceAdder.addStyleSheet(iwc, AddResource.HEADER_BEGIN, iwb.getVirtualPathWithFileNameString("style/cal.css"));

		
//		//	Actions to be performed on page loaded event
		
		StringBuffer action = new StringBuffer("registerEvent(window, 'load', function() {" +
				"var array = new Array();");
				if (entries != null){
					for (int i = 0; i < entries.size(); i++) {
						action.append("array.push('"+entries.get(i)+"');");
					}
				}
			action.append("var scheduleLayer = document.getElementById('"+CALENDAR_VIEWER_SCHEDULE_ID+"');");
			action.append("var schedule = scheduleLayer.getElementsByTagName('div')[0];");
			action.append("var scheduleId = schedule.id;");
			action.append("getSchedule(scheduleId, array);});");
//			action.append("getSchedule(array);});");
//				"getSchedule(array);});");
		
//		System.out.println(action.toString());	
			
		StringBuffer scriptString = new StringBuffer();
		scriptString.append("<script type=\"text/javascript\" > \n")
		.append("\t").append(action).append(" \n")
		.append("</script> \n");
		 
		add(scriptString.toString());
	}	
	public String getBundleIdentifier()	{
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
	
//	PropertiesBean bean
	public void setGroups(List entries) {
		this.entries = entries;
		server = null;
		user = null;
		password = null;
		uniqueIds = null;
		remoteMode = false;
//		return;
	}
//	public void setGroups(PropertiesBean bean) {
//		if (bean == null) {
//			server = null;
//			user = null;
//			password = null;
//			uniqueIds = null;
//			remoteMode = false;
//			return;
//		}
//		server = bean.getServer();
//		user = bean.getLogin();
//		password = bean.getPassword();
//		uniqueIds = bean.getUniqueIds();
//		remoteMode = bean.isRemoteMode();
//		
//		if (server != null) {
//			if (!server.startsWith("http://") && !server.startsWith("https://")) {
//				server = new StringBuffer("http://").append(server).toString();
//			}
//			if (server.endsWith("/")) {
//				server = server.substring(0, server.lastIndexOf("/"));
//			}
//		}
//	}	
	
//	public boolean isRemoteMode() {
//		return remoteMode;
//	}
//	
//	public void setRemoteMode(boolean remoteMode) {
//		this.remoteMode = remoteMode;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getServer() {
//		return server;
//	}
//
//	public void setServer(String server) {
//		this.server = server;
//	}
//
//	public List<String> getUniqueIds() {
//		return uniqueIds;
//	}
//
//	public void setUniqueIds(List<String> uniqueIds) {
//		this.uniqueIds = uniqueIds;
//	}
//
//	public String getUser() {
//		return user;
//	}
//
//	public void setUser(String user) {
//		this.user = user;
//	}	
	
	
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
