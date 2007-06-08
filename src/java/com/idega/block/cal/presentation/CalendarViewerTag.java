package com.idega.block.cal.presentation;

import javax.faces.component.UIComponent; 
import javax.faces.webapp.UIComponentTag; 
 
 
public class CalendarViewerTag extends UIComponentTag{ 
 
	 public void release() { 
	  // the super class method should be called  
		 super.release();
	 } 
  
 protected void setProperties(UIComponent component) { 
  // the super class method should be called
	 super.setProperties(component);
 } 
 
 public String getComponentType() { 
  return "calendarViewerTag"; 
 } 
 
 public String getRendererType() { 	
  // null means the component renders itself 
  return null; 
 } 
 
} 
