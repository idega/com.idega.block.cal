package com.idega.block.cal.presentation;

import com.idega.presentation.ui.AbstractChooser;
import com.idega.user.presentation.group.GroupInfoChooserBlock;

public class CalendarChooser  extends AbstractChooser{
	
	public Class getChooserWindowClass() {
		return CalendarChooserBlock.class;
	}
}
