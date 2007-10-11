package com.idega.block.cal.presentation;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;

public class CalendarListViewer extends Block {
	
	private String calendarListContainerId = "local_calendar_list_container_id";
	
	public void main(IWContext iwc) {
		Layer main = new Layer();
		
		Layer listContainer = new Layer();
		listContainer.setId(calendarListContainerId);
		main.add(listContainer);
		
		addJavaScript(iwc);
		
		add(main);
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
		
		resourceAdder.addStyleSheet(iwc, AddResource.HEADER_BEGIN, iwb.getVirtualPathWithFileNameString("style/cal.css"));

		
//		//	Actions to be performed on page loaded event
		StringBuffer action = new StringBuffer("registerEvent(window, 'load', function() {loadLocalCalendarList('");
		action.append(calendarListContainerId).append("')});");
		
		StringBuffer scriptString = new StringBuffer();
		scriptString.append("<script type=\"text/javascript\" > \n")
		.append("\t").append(action).append(" \n")
		.append("</script> \n");
		 
		add(scriptString.toString());
	}
	public String getCalendarListContainerId() {
		return calendarListContainerId;
	}
	public void setCalendarListContainerId(String calendarListContainerId) {
		this.calendarListContainerId = calendarListContainerId;
	}
	public String getBundleIdentifier()	{
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
}
