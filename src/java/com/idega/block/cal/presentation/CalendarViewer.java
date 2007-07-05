package com.idega.block.cal.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.web2.business.Web2Business;
import com.idega.business.SpringBeanLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.user.bean.GroupsAndCalendarPropertiesBean;
import com.idega.webface.WFUtil;

public class CalendarViewer extends Block{

	private static final String CALENDAR_VIEWER_SCHEDULE_ID = "calendarViewerScheduleId";
	
	private String server = null;
	private String user = null;
	private String password = null;	
	
	private boolean showEntriesAsList = false;
	private boolean hideMenu = false;
	private boolean hidePreviousAndNext = false;
	
	private List<String> uniqueIds = null;
	
	private boolean isRemoteMode = false;	
	private List<String> calendarAttributes = null;
	
	public void main(IWContext iwc) {
		Layer main = new Layer();
		main.setId(CALENDAR_VIEWER_SCHEDULE_ID);
		Layer schedule = new Layer();
		schedule.setId(this.getId());
		
		main.add(schedule);
		
		add(main);
		setID();
		addJavaScript(iwc);
		setCalendarPropertiesBean(this.getId());
	}
	
	public void setEntries(){
		
	}
	private void setCalendarPropertiesBean(String instanceId){
		GroupsAndCalendarPropertiesBean groupsAndCalendarPropertiesBean = new GroupsAndCalendarPropertiesBean();
		
		groupsAndCalendarPropertiesBean.setCalendarAttributes(calendarAttributes);
		groupsAndCalendarPropertiesBean.setLogin(user);
		groupsAndCalendarPropertiesBean.setPassword(password);
		groupsAndCalendarPropertiesBean.setRemoteMode(isRemoteMode);
		groupsAndCalendarPropertiesBean.setServer(server);
		
		Object[] parameters = new Object[2];
		parameters[0] = instanceId;
		parameters[1] = groupsAndCalendarPropertiesBean;
		
		Class[] classes = new Class[2];
		classes[0] = String.class;
		classes[1] = GroupsAndCalendarPropertiesBean.class;
//		
//		//	Setting parameters to bean, these parameters will be taken by DWR and sent to selected server to get required info
		WFUtil.invoke(CalendarConstants.CALENDAR_MANAGER_BEAN_ID, "addCalendarProperties", parameters, classes);
		
	}
	protected Web2Business getWeb2Service(IWApplicationContext iwc) {
		return (Web2Business) SpringBeanLookup.getInstance().getSpringBean(iwc, Web2Business.class);
	}
	
	private void addJavaScript(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		Web2Business web2_business = getWeb2Service(iwc.getApplicationContext());
//		BuilderService bService = null;
//		try {
//			bService = BuilderServiceFactory.getBuilderService(iwc.getApplicationContext());
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		AddResource resourceAdder = AddResourceFactory.getInstance(iwc);
		
//		//	"Helpers"

		if(web2_business != null){
			try {
//				resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,web2_business.getBundleURIToBehaviourLib());
				resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,web2_business.getBundleURIToMootoolsLib());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/CalendarList.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/cal.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/alphaAPI.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/domLib.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/domTT_drag.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/domTT.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/fadomatic.js"));
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN,iwb.getVirtualPathWithFileNameString("javascript/schedule.js"));
		
		//	DWR
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, CalendarConstants.CALENDAR_SERVICE_DWR_INTERFACE_SCRIPT);
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, "/dwr/engine.js");
		resourceAdder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, "/dwr/interface/ScheduleSession.js");
		resourceAdder.addStyleSheet(iwc, AddResource.HEADER_BEGIN, iwb.getVirtualPathWithFileNameString("style/cal.css"));
		resourceAdder.addStyleSheet(iwc, AddResource.HEADER_BEGIN, iwb.getVirtualPathWithFileNameString("style/schedule.css"));

		
//		//	Actions to be performed on page loaded event
		
		StringBuffer action = new StringBuffer("registerEvent(window, 'load', function() {"+
				"");
			action.append("scheduleId = '"+this.getId()+"';");
			action.append("showEntriesAsList = "+showEntriesAsList+";");
			action.append("hideMenu = "+hideMenu+";");
			action.append("hidePreviousAndNext = "+hidePreviousAndNext+";");
			action.append("getCalendarProperties();");
			action.append("});");
			
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
	
	public void setShowEntriesAsList(boolean showEntriesAsList){
		this.showEntriesAsList = showEntriesAsList;
	}

	public void setHideMenu(boolean hideMenu){
		this.hideMenu = hideMenu;
	}
	
	public void setHidePreviousAndNext(boolean hidePreviousAndNext){
		this.hidePreviousAndNext = hidePreviousAndNext;
	}
	
	
	public void setCalendarEntries(List list) {

		if (list == null) {
			server = null;
			user = null;
			password = null;
			isRemoteMode = false;
			calendarAttributes = null;

			return;
		}
		if (((String)list.get(3)).equals("true")){
			isRemoteMode = true;
			server = (String)list.get(0);
			user = (String)list.get(1);
			password = (String)list.get(2);
		}
		else{
			isRemoteMode = false;
			server = null;
			user = null;
			password = null;			
		}
		calendarAttributes = new ArrayList<String>();
		for (int i = 4; i < list.size(); i++) {
			calendarAttributes.add((String)list.get(i));
			
		}
	}	
	
	public boolean isRemoteMode() {
		return isRemoteMode;
	}
	
	public void setRemoteMode(boolean remoteMode) {
		this.isRemoteMode = remoteMode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public List<String> getUniqueIds() {
		return uniqueIds;
	}

	public void setUniqueIds(List<String> uniqueIds) {
		this.uniqueIds = uniqueIds;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}	
	}
