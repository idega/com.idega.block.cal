package com.idega.block.cal.presentation;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.AbstractChooser;
/**
 * 
 * @author <a href="justinas@idega.com">Justinas Rakita</a>
 *
 */
public class GroupAndCalendarChooser extends AbstractChooser {

	private static final String GROUPS_AND_CALENDAR_STYLE_CLASS = "groupsAndCalendarsLayerStyleClass";
	private static final String CALENDAR_STYLE_CLASS = "calendarsLayerStyleClass";
	private static final String NODE_ON_CLICK_ACTION = "function(){ScheduleSession.getCalendarParameters(this.id, displayCalendarAttributes);saveProperties();}";
//	private UserBusiness userBiz = null;
	private static IWBundle bundle = null;	
	private static final String chooserHelper = "groups_and_calendar_chooser_helper";
	private List values = null;
	
	public GroupAndCalendarChooser(String instanceId, String method) {
		super(false);
		addForm(false);
		setInstanceId(instanceId);
		setMethod(method);
	}
	
//	public void main(IWContext iwc) {
//		Layer groupsAndCalendarsLayer = new Layer();
////		Page parentPage = getParentPage();
//		
//		GroupsChooserBlock groupsChooser = new GroupsChooserBlock();
//		groupsChooser.setNodeOnClickAction(NODE_ON_CLICK_ACTION);
//		groupsChooser.setStyleClass("groupsLayerStyleClass");
//		
////		groupsAndCalendarsLayer.add(groupsChooser);
//		
//		CoreUtil.addJavaSciptForChooser(iwc);
//		
////		groupsAndCalendarsLayer.add(new CalendarChooserBlock());	
//		CalendarChooserBlock calendarChooserBlock = new CalendarChooserBlock();
//		calendarChooserBlock.setStyleClass(CALENDAR_STYLE_CLASS);
//		groupsAndCalendarsLayer.add(calendarChooserBlock);		
//		groupsAndCalendarsLayer.setStyleClass(GROUPS_AND_CALENDAR_STYLE_CLASS);
//		
//		add(groupsAndCalendarsLayer);
//		
//	}
		
	public void encodeBegin(FacesContext fc)throws IOException{
		super.encodeBegin(fc);
		Layer panels = (Layer)this.getFacet("PANELS");
		this.renderChild(fc,panels);
		
	}
	
//	public static IWBundle getBundle() {
//		if (bundle == null) {
//			setupBundle();
//		}
//		return bundle;
//	}

	private static void setupBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		IWContext iwContext = IWContext.getIWContext(context);
		bundle = iwContext.getIWMainApplication().getBundle(CalendarConstants.BUNDLE_IDENTIFIER);
	}
//	@Override
//	public boolean getChooserAttributes() {
//		// TODO Auto-generated method stub
//		return false;
//	}
	@Override
	protected String getChooserHelperVarName() {
		// TODO Auto-generated method stub
		return chooserHelper;
	}
	@Override
	public Class getChooserWindowClass() {
		// TODO Auto-generated method stub
		return GroupAndCalendarChooserBlock.class;
	}	
	public PresentationObject getChooser(IWContext iwc, IWBundle bundle) {
//		GroupChooserBlock chooserBlock = new GroupChooserBlock();
		GroupAndCalendarChooserBlock chooserBlock = new GroupAndCalendarChooserBlock();
//		chooserBlock.setGroupsChooserBlock
//		chooserBlock.setCalChooserBlock
		
		return chooserBlock;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
}