package com.idega.block.cal.handler;

import java.util.List;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.cal.presentation.CalendarsChooser;
import com.idega.builder.business.BuilderLogic;
import com.idega.business.chooser.helper.CalendarsChooserHelper;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.util.CoreConstants;

public class CalendarHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc, boolean oldGenerationHandler, String instanceId, String method) {
		String action = null;
		
		BuilderLogic builder = BuilderLogic.getInstance();
		String className = builder.getModuleClassName(builder.getCurrentIBPage(iwc), instanceId);
		if (className != null) {
			String message = "Loading...";
			try{
				IWResourceBundle iwrb =  iwc.getIWMainApplication().getBundle(CoreConstants.IW_USER_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
				message = iwrb.getLocalizedString("loading", message);
			} catch (Exception e) {}
			
			StringBuffer temp = new StringBuffer("reloadPropertiesForCalendarViewer('");
			temp.append(instanceId).append("', '").append(instanceId).append(CalendarConstants.CALENDAR_VIEWER_MAIN_CONTAINER_ID_ENDING).append("', '");
			temp.append(message).append("');");
			action = temp.toString();
		}
		
		CalendarsChooser chooser = new CalendarsChooser(instanceId, method, action);
		chooser.setStyleClass("groupsAndCalendarsLayerStyleClass");
		CalendarsChooserHelper helper = new CalendarsChooserHelper();
		chooser.setProperties(helper.getExtractedPropertiesFromString(stringValue));

		return chooser;
	}

	public void onUpdate(String[] values, IWContext iwc) {
	}

}
