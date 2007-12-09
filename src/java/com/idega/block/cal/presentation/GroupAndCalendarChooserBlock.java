package com.idega.block.cal.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.user.presentation.group.GroupsChooserBlock;
import com.idega.util.CoreUtil;

public class GroupAndCalendarChooserBlock extends Block{
	private static final String GROUPS_AND_CALENDAR_STYLE_CLASS = "groupsAndCalendarsLayerStyleClass";
	private static final String GROUPS_AND_CALENDAR_ID = "groupsAndCalendarsLayerId";
	private static final String CALENDAR_STYLE_CLASS = "calendarsLayerStyleClass";
	private static final String NODE_ON_CLICK_ACTION = "function(){" +
//		"var groups_and_calendar_chooser_helper = new ChooserHelper();" +
//		"groups_and_calendar_chooser_helper.removeAllAdvancedProperties();" +			
		"prepareDwrForGettingCalendarParameters(this.id);" +
//		"saveGroupName(this.value);" +
//			"ScheduleSession.getCalendarParameters(this.id, displayCalendarAttributes);" +
		"}";
	
	public void main(IWContext iwc) {
		Layer groupsAndCalendarsLayer = new Layer();
		
		GroupsChooserBlock groupsChooser = new GroupsChooserBlock();
		groupsChooser.setAddExtraJavaScript(true);
		groupsChooser.setNodeOnClickAction(NODE_ON_CLICK_ACTION);
		groupsChooser.setStyleClass("groupsLayerStyleClass");
		groupsChooser.setExecuteScriptOnLoad(false);
		groupsChooser.setPropertiesBean(null);
		
		groupsAndCalendarsLayer.add(groupsChooser);
		
		CoreUtil.addJavaScriptForChooser(iwc);
		
		CalendarChooserBlock calendarChooserBlock = new CalendarChooserBlock();
		calendarChooserBlock.setStyleClass(CALENDAR_STYLE_CLASS);
		groupsAndCalendarsLayer.add(calendarChooserBlock);		
		groupsAndCalendarsLayer.setStyleClass(GROUPS_AND_CALENDAR_STYLE_CLASS);
		
		groupsAndCalendarsLayer.setId(GROUPS_AND_CALENDAR_ID);
		
		add(groupsAndCalendarsLayer);
	}
	
}
