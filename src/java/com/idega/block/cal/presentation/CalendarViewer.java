package com.idega.block.cal.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.web2.business.Web2Business;
import com.idega.builder.business.BuilderLogic;
import com.idega.cal.bean.CalendarPropertiesBean;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.user.presentation.group.GroupViewer;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.expression.ELUtil;
import com.idega.webface.WFUtil;

public class CalendarViewer extends GroupViewer {
	
	private boolean showEntriesAsList = false;
	private boolean hideMenu = false;
	private boolean hidePreviousAndNext = false;
	private boolean showTime = false;
	
	private List<String> events = null;
	private List<String> ledgers = null;
	
	@Override
	public void main(IWContext iwc) {
		super.main(iwc);
		
		String instanceId = BuilderLogic.getInstance().getInstanceId(this);
		
		Layer container = new Layer();
		if (instanceId != null) {
			String containerId = new StringBuffer(instanceId).append(CalendarConstants.CALENDAR_VIEWER_MAIN_CONTAINER_ID_ENDING).toString();
			container.setId(containerId);
		}
		
		addJavaScript(iwc, container.getId(), instanceId);
		addCssFiles(iwc);
		
		setCalendarPropertiesBean(instanceId);
		
		add(container);
	}
	
	private void setCalendarPropertiesBean(String instanceId){
		CalendarPropertiesBean properties = new CalendarPropertiesBean();
		setBasicProperties(properties, instanceId);
		
		properties.setShowEntriesAsList(isShowEntriesAsList());
		properties.setHideMenu(isHideMenu());
		properties.setHidePreviousAndNext(isHidePreviousAndNext());
		properties.setShowTime(isShowTime());
		properties.setEvents(getEvents());
		properties.setLedgers(ledgers);
		
		Object[] parameters = new Object[2];
		parameters[0] = instanceId;
		parameters[1] = properties;
		
		Class<?>[] classes = new Class[2];
		classes[0] = String.class;
		classes[1] = CalendarPropertiesBean.class;

		//	Setting parameters to bean, these parameters will be taken by DWR and sent to selected server to get required info
		WFUtil.invoke(CalendarConstants.CALENDAR_MANAGER_BEAN_ID, "addCalendarProperties", parameters, classes);
	}
	
	@SuppressWarnings("cast")
	private void addCssFiles(IWContext iwc) {
		IWBundle iwb = getBundle(iwc);
		
		StringBuffer cssFiles = new StringBuffer("<link rel=\"stylesheet\" href=\"");
		cssFiles.append(iwb.getVirtualPathWithFileNameString("style/cal.css")).append("\" type=\"text/css\" />\n");
		
		cssFiles.append("<link rel=\"stylesheet\" href=\"");
		cssFiles.append(iwb.getVirtualPathWithFileNameString("style/schedule.css")).append("\" type=\"text/css\" />\n");
		
		if (!CoreUtil.isSingleComponentRenderingProcess(iwc)) {
			try {
				Web2Business web2 = ELUtil.getInstance().getBean(Web2Business.class);
				cssFiles.append("<link rel=\"stylesheet\" href=\"");
				cssFiles.append(web2.getMoodalboxStyleFilePath()).append("\" type=\"text/css\" />\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		add(cssFiles.toString());
	}
	
	@SuppressWarnings("cast")
	private void addJavaScript(IWContext iwc, String id, String instanceId) {
		IWBundle iwb = getBundle(iwc);
		List<String> files = new ArrayList<String>();
		
		boolean singleProcess = CoreUtil.isSingleComponentRenderingProcess(iwc);
		if (!singleProcess) {
			//	Web 2.0 stuff
			Web2Business web2 = ELUtil.getInstance().getBean(Web2Business.class);
			try {
				files.add(web2.getBundleURIToMootoolsLib());
				files.add(web2.getMoodalboxScriptFilePath(false));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		//	Calendar stuff
		files.add(iwb.getVirtualPathWithFileNameString("javascript/CalendarViewerHelper.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/CalendarList.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/alphaAPI.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/domLib.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/domTT_drag.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/domTT.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/fadomatic.js"));
		files.add(iwb.getVirtualPathWithFileNameString("javascript/schedule.js"));
		
		//	DWR
		files.add(CalendarConstants.CALENDAR_SERVICE_DWR_INTERFACE_SCRIPT);
		files.add(CoreConstants.DWR_ENGINE_SCRIPT);
		files.add("/dwr/interface/ScheduleSession.js");

		addScriptFiles(iwc, files, singleProcess);
		
		StringBuffer singleAction = new StringBuffer("loadCalendarViewer('").append(id).append("', '").append(instanceId).append("', '");
		singleAction.append(iwb.getResourceBundle(iwc).getLocalizedString("loading", "Loading...")).append("');");
		StringBuffer action = null;
		if (singleProcess) {
			action = singleAction;
		}
		else {
			action = new StringBuffer("window.addEvent('load', function() {").append(singleAction.toString()).append("});");
		}
			
		StringBuffer scriptString = new StringBuffer("<script type=\"text/javascript\" > \n\t").append(action.toString()).append(" \n</script> \n");
		add(scriptString.toString());
	}
	
	public void setCalendarProperties(CalendarPropertiesBean properties) {
		super.setGroups(properties);
		
		if (properties == null) {
			events = null;
			ledgers = null;
		}
		
		setEvents(properties.getEvents());
		setLedgers(properties.getLedgers());
	}
	
	@Override
	public String getBundleIdentifier()	{
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}

	public void setShowEntriesAsList(boolean showEntriesAsList) {
		this.showEntriesAsList = showEntriesAsList;
	}

	public void setHideMenu(boolean hideMenu) {
		this.hideMenu = hideMenu;
	}

	public void setHidePreviousAndNext(boolean hidePreviousAndNext) {
		this.hidePreviousAndNext = hidePreviousAndNext;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public boolean isShowEntriesAsList() {
		return showEntriesAsList;
	}

	public boolean isHideMenu() {
		return hideMenu;
	}

	public boolean isHidePreviousAndNext() {
		return hidePreviousAndNext;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public List<String> getEvents() {
		return events;
	}

	public List<String> getLedgers() {
		return ledgers;
	}

	public void setLedgers(List<String> ledgers) {
		this.ledgers = ledgers;
	}
	
}
