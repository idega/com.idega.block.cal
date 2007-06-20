package com.idega.block.cal.handler;

import java.util.List;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.cal.presentation.CalendarViewer;
import com.idega.block.cal.presentation.GroupAndCalendarChooser;
import com.idega.builder.business.BuilderLogic;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserConstants;

public class CalendarHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc, boolean oldGenerationHandler, String instanceId,
			String method) {
		String action = null;
//		IWResourceBundle iwrb = null;
		
		BuilderLogic builder = BuilderLogic.getInstance();
		String className = builder.getModuleClassName(builder.getCurrentIBPage(iwc), instanceId);
		if (className != null) {
			String message = "Loading...";
			try{
				IWResourceBundle iwrb =  iwc.getIWMainApplication().getBundle(CalendarConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
				message = iwrb.getLocalizedString("loading", message);
			} catch (Exception e) {}
			
			StringBuffer temp = new StringBuffer();
			if (className.equals(CalendarViewer.class.getName())) {
				temp.append("reloadGroupProperties('");
			}
			else if (className.equals(CalendarViewer.class.getName())) {
				temp.append("reloadGroupMemberProperties('");
			}
			temp.append(instanceId).append("', '").append(instanceId).append(UserConstants.GROUP_VIEWER_CONTAINER_ID_ENDING).append("', '");
			temp.append(message).append("');");
			action = temp.toString();
		}

//		GroupsChooser chooser = new GroupsChooser(instanceId, method, action);
//		chooser.set
//		chooser.get
//		GroupsChooserHelper helper = new GroupsChooserHelper();
//		chooser.setBean(helper.getExtractedPropertiesFromString(stringValue));
//		chooser.setNodeOnClickAction("function(){CalService.getCalendarParameters(this.id, displayCalendarAttributes);}");
//		chooserPresentationObject.setAddExtraJavaScript(true);
//		chooserPresentationObject.setNodeOnClickAction("function(){CalService.getCalendarParameters(this.id, displayCalendarAttributes);}");
		
		GroupAndCalendarChooser groupAndCalendarChooser = new GroupAndCalendarChooser(instanceId, method);
		groupAndCalendarChooser.setStyleClass("groupsAndCalendarsLayerStyleClass");
//		CalendarChooserBlock calendarChooser = new CalendarChooserBlock();
//		chooser.getChooser(iwc, bundle)
//		groupAndCalendarChooser.add(chooser);
//		groupAndCalendarChooser.add(calendarChooser);
		//return chooser;
		return groupAndCalendarChooser;
	}

	public void onUpdate(String[] values, IWContext iwc) {
	}

}
