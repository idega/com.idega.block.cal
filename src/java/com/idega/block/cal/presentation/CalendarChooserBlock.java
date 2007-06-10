package com.idega.block.cal.presentation;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.util.AbstractChooserBlock;

public class CalendarChooserBlock extends AbstractChooserBlock {
	
	private static final String CALENDAR_LIST_CONTAINER_ID = "calendar_list_container_id";
	private String styleClass = null;
	public void main(IWContext iwc) {
		
		Layer main = new Layer();

		if(styleClass != null){
			main.setStyleClass(styleClass);
			System.out.println("setting style class");
		}
		else{
			System.out.println("STYLE CLASS == NULL");
		}
		
		CalendarListViewer calendarList = new CalendarListViewer();
		calendarList.setCalendarListContainerId(CALENDAR_LIST_CONTAINER_ID);
		main.add(calendarList);
		add(main);
	}
	
	@Override
	public boolean getChooserAttributes() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getBundleIdentifier()	{
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	public void setStyleClass(String styleClass){
		this.styleClass = styleClass;
	}	
}
