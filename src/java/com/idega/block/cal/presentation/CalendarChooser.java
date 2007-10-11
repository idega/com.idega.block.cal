package com.idega.block.cal.presentation;

import com.idega.presentation.ui.AbstractChooser;

public class CalendarChooser  extends AbstractChooser{
	
	public Class getChooserWindowClass() {
		return CalendarChooserBlock.class;
	}
	@Override
	public String getChooserHelperVarName() {
		// TODO Auto-generated method stub
		return null;
	}
}
