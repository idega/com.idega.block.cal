package com.idega.block.cal.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.bean.GroupsAndCalendarPropertiesBean;

public class CalendarManagerBean implements Serializable {

//	private Map<String, PropertiesBean> abstractProperties = null;
//	private Map<String, GroupPropertiesBean> groupProperties = null;
//	private Map<String, UserPropertiesBean> userProperties = null;
	
	private Map<String, GroupsAndCalendarPropertiesBean> calendarProperties = null;
	
	public CalendarManagerBean() {
//		abstractProperties = new HashMap<String, PropertiesBean>();
//		groupProperties = new HashMap<String, GroupPropertiesBean>();
//		userProperties = new HashMap<String, UserPropertiesBean>();
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
//	
//	public boolean addAbstractProperties(String instanceId, PropertiesBean propertiesBean) {
//		if (instanceId == null || propertiesBean == null) {
//			return false;
//		}
//		abstractProperties.put(instanceId, propertiesBean);
//		return true;
//	}
	
//	private PropertiesBean getAbstractProperties(String instanceId) {
//		if (instanceId == null) {
//			return null;
//		}
//		return abstractProperties.get(instanceId);
//	}
	
//	public boolean addGroupProperties(String instanceId, GroupPropertiesBean propertiesBean) {
//		if (instanceId == null || propertiesBean == null) {
//			return false;
//		}
//		groupProperties.put(instanceId, propertiesBean);
//		return true;
//	}
//	
//	public boolean addUserProperties(String instanceId, UserPropertiesBean propertiesBean) {
//		if (instanceId == null || propertiesBean == null) {
//			return false;
//		}
//		userProperties.put(instanceId, propertiesBean);
//		return true;
//	}
//	
//	public GroupPropertiesBean getGroupProperties(String instanceId) {
//		if (instanceId == null) {
//			return null;
//		}
//		PropertiesBean bean = getAbstractProperties(instanceId);
//		if (bean == null) {
//			return groupProperties.get(instanceId);
//		}
//		else {
//			abstractProperties.remove(instanceId);
//			return new GroupPropertiesBean(bean);
//		}
//		
//	}
	
//	public UserPropertiesBean getUserProperties(String instanceId) {
//		if (instanceId == null) {
//			return null;
//		}
//		PropertiesBean bean = getAbstractProperties(instanceId);
//		if (bean == null) {
//			return userProperties.get(instanceId);
//		}
//		else {
//			abstractProperties.remove(instanceId);
//			return new UserPropertiesBean(bean);
//		}
//	}
//	
//	public boolean removeGroupProperties(String instanceId) {
//		return removeProperties(instanceId, groupProperties);
//	}
//	
//	public boolean removeUserProperties(String instanceId) {
//		return removeProperties(instanceId, userProperties);
//	}
//	
//	private boolean removeProperties(String key, Map properties) {
//		if (key == null) {
//			return false;
//		}
//		
//		properties.remove(key);
//		
//		return true;
//	}

}

