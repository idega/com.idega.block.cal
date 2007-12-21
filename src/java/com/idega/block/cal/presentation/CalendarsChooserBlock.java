package com.idega.block.cal.presentation;

import java.util.List;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.cal.bean.CalendarPropertiesBean;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.FieldSet;
import com.idega.presentation.ui.Legend;
import com.idega.user.presentation.group.GroupsChooserBlock;

public class CalendarsChooserBlock extends Block {
	
	private CalendarPropertiesBean properties = null;
	
	private String calendarTypesContainerId = null;
	private String calendarLedgersContainerId = null;
	
	public void main(IWContext iwc) {
		Layer main = new Layer();
		
		GroupsChooserBlock groupsChooser = new GroupsChooserBlock();
		groupsChooser.setExecuteScriptOnLoad(false);
		groupsChooser.setSpecialMarkForRadioButton("calendar");
		groupsChooser.setStyleClass("groupsLayerStyleClass");
		groupsChooser.setPropertiesBean(properties);
		
		Layer middlePartOfChooser = new Layer(); 
		middlePartOfChooser.add(getEventsLayer(iwc));
		middlePartOfChooser.add(getLedgersLayer(iwc));
		
		groupsChooser.setMiddlePartOfChooser(middlePartOfChooser);
		groupsChooser.setCustomTreeFunctionToBeExecutedOnLoad(getEventScriptFunction());
		main.add(groupsChooser);

		add(main);
	}
	
	private String getEventScriptFunction() {
		StringBuffer actions = new StringBuffer(getJavaScriptAction("getSimpleCalendarTypes", calendarTypesContainerId,
				properties == null ? null : properties.getEvents()));
		actions.append(" ");
		actions.append(getJavaScriptAction("getSimpleCalendarLedgers", calendarLedgersContainerId,  properties == null ? null : properties.getLedgers()));
		
		return actions.toString();
	}
	
	private String getJavaScriptAction(String functionName, String containerId, List<String> ids) {
		String server = null;
		String login = null;
		String password = null;
	
		boolean remoteMode = properties == null ? false : properties.isRemoteMode();
		if (remoteMode && properties != null) {
			server = properties.getServer();
			login = properties.getLogin();
			password = properties.getPassword();
		}
		
		StringBuffer action = new StringBuffer(functionName).append("(");
		if (server == null) {
			action.append("null");
		}
		else {
			action.append("'").append(server).append("'");
		}
		action.append(", ");
		if (login == null) {
			action.append("null");
		}
		else {
			action.append("'").append(login).append("'");
		}
		action.append(", ");
		if (password == null) {
			action.append("null");
		}
		else {
			action.append("'").append(password).append("'");
		}
		action.append(", ").append(remoteMode).append(", '").append(containerId).append("', ");
		if (ids == null || ids.size() == 0) {
			action.append("null");
		}
		else {
			action.append("[");
			for (int i = 0; i < ids.size(); i++) {
				action.append("'").append(ids.get(i)).append("'");
				if ((i + 1 < ids.size())) {
					action.append(", ");
				}
			}
			action.append("]");
		}
		action.append(");");
		
		return action.toString();
	}
	
	private Layer getEventsLayer(IWContext iwc) {
		Layer container = new Layer();
		container.setStyleClass("calendarEventsContainerStyleClass");
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		FieldSet events = new FieldSet(new Legend(iwrb.getLocalizedString("events", "Events")));
		events.add(iwrb.getLocalizedString("select_events", "Select (deselect) events:"));
		Layer eventsContainer = new Layer();
		calendarTypesContainerId = eventsContainer.getId();
		events.add(eventsContainer);
		container.add(events);
		
		return container;
	}
	
	private Layer getLedgersLayer(IWContext iwc) {
		Layer container = new Layer();
		container.setStyleClass("calendarLedgersContainerStyleClass");
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		FieldSet ledgers = new FieldSet(new Legend(iwrb.getLocalizedString("ledgers", "Ledgers")));
		ledgers.add(iwrb.getLocalizedString("select_ledgers", "Select (deselect) ledgers:"));
		Layer ledgersContainer = new Layer();
		calendarLedgersContainerId = ledgersContainer.getId();
		ledgers.add(ledgersContainer);
		container.add(ledgers);
		
		return container;
	}

	public CalendarPropertiesBean getProperties() {
		return properties;
	}

	public void setProperties(CalendarPropertiesBean properties) {
		this.properties = properties;
	}
	
	public String getBundleIdentifier() {
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
}
