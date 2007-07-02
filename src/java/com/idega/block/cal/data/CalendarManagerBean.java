package com.idega.block.cal.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.user.bean.GroupsAndCalendarPropertiesBean;

public class CalendarManagerBean implements Serializable {

	private Map<String, GroupsAndCalendarPropertiesBean> calendarProperties = null;
	
	public CalendarManagerBean() {
		calendarProperties = new HashMap<String, GroupsAndCalendarPropertiesBean>();
	}
	
	public boolean addCalendarProperties(String instanceId, GroupsAndCalendarPropertiesBean propertiesBean) {
		if (instanceId == null || propertiesBean == null) {
			return false;
		}
		calendarProperties.put(instanceId, propertiesBean);
		return true;
	}
	
	public GroupsAndCalendarPropertiesBean getCalendarProperties(String instanceId) {
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

