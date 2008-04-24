package com.idega.block.cal.presentation;

import com.idega.cal.bean.CalendarPropertiesBean;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.presentation.group.GroupsChooser;

/**
 * 
 * @author <a href="justinas@idega.com">Justinas Rakita</a>
 *
 */
public class CalendarsChooser extends GroupsChooser {
	
	private CalendarPropertiesBean properties = null;
	
	public CalendarsChooser(String instanceId, String method, String actionAfterPropertySaved) {
		super(instanceId, method, actionAfterPropertySaved);
	}
	
	@Override
	public Class getChooserWindowClass() {
		return CalendarsChooserBlock.class;
	}
	
	@Override
	public PresentationObject getChooser(IWContext iwc, IWBundle bundle) {
		CalendarsChooserBlock chooserBlock = new CalendarsChooserBlock();
		chooserBlock.setProperties(properties);
		return chooserBlock;
	}

	public CalendarPropertiesBean getProperties() {
		return properties;
	}

	public void setProperties(CalendarPropertiesBean properties) {
		this.properties = properties;
	}

}