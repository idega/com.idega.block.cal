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
//			"ScheduleSession.getCalendarParameters(this.id, displayCalendarAttributes);" +
		"}";
	
	public void main(IWContext iwc) {
		Layer groupsAndCalendarsLayer = new Layer();
//		Page parentPage = getParentPage();
		
		GroupsChooserBlock groupsChooser = new GroupsChooserBlock();
		groupsChooser.setAddExtraJavaScript(true);
		groupsChooser.setNodeOnClickAction(NODE_ON_CLICK_ACTION);
		groupsChooser.setStyleClass("groupsLayerStyleClass");
		groupsChooser.setExecuteScriptOnLoad(false);
		groupsChooser.setPropertiesBean(null);
//		groupsChooser.setNodeOnClickAction(nodeOnClickAction);
		
		groupsAndCalendarsLayer.add(groupsChooser);
		
		CoreUtil.addJavaSciptForChooser(iwc);
		
//		groupsAndCalendarsLayer.add(new CalendarChooserBlock());	
		CalendarChooserBlock calendarChooserBlock = new CalendarChooserBlock();
		calendarChooserBlock.setStyleClass(CALENDAR_STYLE_CLASS);
		groupsAndCalendarsLayer.add(calendarChooserBlock);		
		groupsAndCalendarsLayer.setStyleClass(GROUPS_AND_CALENDAR_STYLE_CLASS);
		
		groupsAndCalendarsLayer.setId(GROUPS_AND_CALENDAR_ID);
		
		add(groupsAndCalendarsLayer);
		addClientResources();
		
		
	}
	
	protected void addClientResources() {
		add(
			new StringBuilder("<script type=\"text/javascript\">")
			.append("var groups_chooser_helper = new ChooserHelper();")
//			.append("console.log(groups_chooser_helper);")
//			.append("var groups_and_calendar_chooser_helper = new ChooserHelper();")
//			.append("saveProperties();")
//			.append("calendar_chooser_helper.removeAllAdvancedProperties();")
//			.append("for(var index=0; index&ltarrayOfParameters.length; index++) {")
//			.append("calendar_chooser_helper.addAdvancedProperty(arrayOfParameters[index].id, arrayOfParameters[index].id);")
//			.append("console.log(arrayOfParameters)")
//			.append("}")
			
			
//			.append("function elightChoosePlugin(checkbox) {")
//			.append("if(!checkbox || checkbox == null)")
//			.append("return;")
//			.append("if(checkbox.checked) {")
//			.append("elight_chooser_helper.addAdvancedProperty(checkbox.id, checkbox.id);")
//			.append("} else {")
//			.append("elight_chooser_helper.removeAdvancedProperty(checkbox.id);")
//			.append("}}")
//			.append("var elight_checkboxes = document.getElementsByName('elightPluginUsedCheckbox');")
//			.append("if(elight_checkboxes &amp;&amp; elight_checkboxes != null)")
//			.append("for(var index=0; index &lt; elight_checkboxes.length; index++)")
//			.append("if(elight_checkboxes[index].checked)")
//			.append("elight_chooser_helper.addAdvancedProperty(elight_checkboxes[index].id, elight_checkboxes[index].id);")
			.append("</script>")
			.toString()
			
		);
	}
	
//	calendar_chooser_helper.removeAllAdvancedProperties();
//	
//	for(var index=0; index<arrayOfParameters.length; index++) {
//		
//		calendar_chooser_helper.addAdvancedProperty(arrayOfParameters[index].id, arrayOfParameters[index].id);					
//	}
	
}
