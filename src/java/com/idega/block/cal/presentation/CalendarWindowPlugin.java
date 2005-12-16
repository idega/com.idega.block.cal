package com.idega.block.cal.presentation;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import sun.security.action.GetLongAction;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.business.ICApplicationBindingBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.user.app.ToolbarElement;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Aug 26, 2004
 */
public class CalendarWindowPlugin implements ToolbarElement {
	
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.cal";

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getButtonImage(com.idega.presentation.IWContext)
	 */
	public Image getButtonImage(IWContext iwc) {
		IWBundle bundle = iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		return bundle.getImage("cal_icon.gif");
	}

	public String getName(IWContext iwc) {
		IWBundle bundle = iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle resourceBundle = bundle.getResourceBundle(iwc);
		return resourceBundle.getLocalizedString("button.calendar", "Calendar");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPresentationObjectClass(com.idega.presentation.IWContext)
	 */
	public Class getPresentationObjectClass(IWContext iwc) {
		return CalendarWindow.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getParameterMap(com.idega.presentation.IWContext)
	 */
	public Map getParameterMap(IWContext iwc) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isValid(com.idega.presentation.IWContext)
	 */
	public boolean isValid(IWContext iwc) {
        try {
        	ICApplicationBindingBusiness applicationBindingBusiness = (ICApplicationBindingBusiness) IBOLookup.getServiceInstance(iwc, ICApplicationBindingBusiness.class);
        	String showStuff =applicationBindingBusiness.get("temp_show_is_related_stuff");
        	String showCalender = applicationBindingBusiness.get("temp_show_calendar");
        	// original condition, everything is true if not null
        	return (showStuff != null) && (showCalender != null);
        }
        catch (IBOLookupException ex) {
        	throw new IBORuntimeException(ex);
        }
        catch (IOException ex) {
        	Logger.getLogger(CalendarWindowPlugin.class.getName()).warning("[CalendarWindowPlugin] Could not look up parameter temp_show_is_related_stuff and/or temp_show_calendar");
        	return false;
        }
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPriority(com.idega.presentation.IWContext)
	 */
	public int getPriority(IWContext iwc) {
		return 11;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isButton(com.idega.presentation.IWContext)
	 */
	public boolean isButton(IWContext iwc) {
		return true;
	}

}
