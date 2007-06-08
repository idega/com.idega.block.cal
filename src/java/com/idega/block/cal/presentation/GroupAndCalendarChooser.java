package com.idega.block.cal.presentation;

import java.io.IOException;

import javax.faces.context.FacesContext;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.user.presentation.group.GroupsChooserBlock;
import com.idega.util.CoreUtil;
/**
 * 
 * @author <a href="justinas@idega.com">Justinas Rakita</a>
 *
 */
public class GroupAndCalendarChooser extends Block {
	
//	private UserBusiness userBiz = null;
	private static IWBundle bundle = null;	
	
	public void main(IWContext iwc) {
		Layer groupsAndCalendarsLayer = new Layer();
//		Page parentPage = getParentPage();
		
		GroupsChooserBlock groupsChooser = new GroupsChooserBlock();
		groupsChooser.setNodeOnClickAction("function(){CalService.getCalendarParameters(this.id, displayCalendarAttributes);}");
		
		groupsAndCalendarsLayer.add(groupsChooser);
		
		CoreUtil.addJavaSciptForChooser(iwc);
		
		groupsAndCalendarsLayer.add(new CalendarChooserBlock());	
		groupsAndCalendarsLayer.setStyleClass("groupsAndCalendarsLayerStyleClass");
		add(groupsAndCalendarsLayer);
	}
		
	public void encodeBegin(FacesContext fc)throws IOException{
		super.encodeBegin(fc);
		
		Layer panels = (Layer)this.getFacet("PANELS");
		this.renderChild(fc,panels);
		
	}
	
	public static IWBundle getBundle() {
		if (bundle == null) {
			setupBundle();
		}
		return bundle;
	}

	private static void setupBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		IWContext iwContext = IWContext.getIWContext(context);
		bundle = iwContext.getIWMainApplication().getBundle(CalendarConstants.BUNDLE_IDENTIFIER);
	}
}