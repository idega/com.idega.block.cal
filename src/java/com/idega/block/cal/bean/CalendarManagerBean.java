package com.idega.block.cal.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.cal.bean.CalendarPropertiesBean;

public class CalendarManagerBean implements Serializable {

	private static final long serialVersionUID = 2479903618028715269L;
	
	private Map<String, CalendarPropertiesBean> calendarProperties = null;
	
	public CalendarManagerBean() {
		calendarProperties = new HashMap<String, CalendarPropertiesBean>();
	}
	
	public boolean addCalendarProperties(String instanceId, CalendarPropertiesBean propertiesBean) {
		if (instanceId == null || propertiesBean == null) {
			return false;
		}
		calendarProperties.put(instanceId, propertiesBean);
		return true;
	}
	
	public CalendarPropertiesBean getCalendarProperties(String instanceId) {
		if (instanceId == null) {
			return null;
		}
		return calendarProperties.get(instanceId);
	}	
	
	public boolean removeProperties(String key, Map calendarProperties) {
		if (key == null) {
			return false;
		}
		
		calendarProperties.remove(key);
		
		return true;
	}
}

