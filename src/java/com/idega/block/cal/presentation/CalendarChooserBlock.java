package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.util.AbstractChooserBlock;

public class CalendarChooserBlock extends AbstractChooserBlock {
	
	private static final String CALENDAR_LIST_CONTAINER_ID = "calendar_list_container_id";
	
	public void main(IWContext iwc) {
//		Layer main = new Layer();
		
		//	JavaScript
		//addJavaScript(iwc);
		
		//	Groups tree
//		GroupTreeViewer groupsTree = new GroupTreeViewer();
//		groupsTree.setGroupsTreeContainerId(GROUPS_TREE_CONTAINER_ID);
//		add(groupsTree);
		
		// Calendar list
		CalendarListViewer calendarList = new CalendarListViewer();
		calendarList.setCalendarListContainerId(CALENDAR_LIST_CONTAINER_ID);
//		calendarList.setStyleClass("calendarChooserStyleClass");
		add(calendarList);
		//	Connection chooser
//		addConnectionTypeField(iwc, main);
		
//		add(main);
	}
	
	@Override
	public boolean getChooserAttributes() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getBundleIdentifier()	{
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
}
