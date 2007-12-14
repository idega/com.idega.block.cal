var CALENDAR_VIEWER_EXTENDED_PROPERTIES = new Array();

var CALENDAR_MONTH_MODE = 'MONTH';
var CALENDAR_DAY_MODE = 'DAY';
var CALENDAR_WEEK_MODE = 'WEEK';
var CALENDAR_WORK_WEEK_MODE = 'WORKWEEK';

var DEFAULT_CALENDAR_MODE = CALENDAR_MONTH_MODE;
var CURRENT_CALENDAR_MODE = DEFAULT_CALENDAR_MODE;

var ENTRIES_PACKAGE_SIZE = 2;

var CALENDAR_SCHEDULE_ENTRY_TABLE_ID = 'scheduleEntryTableId';
var CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID = 'calendarpropertiesbeanid';

//	Labels
var CALENDAR_ENTRY_NAME = 'Name';
var CALENDAR_ENTRY_END_DATE = 'End date';
var CALENDAR_ENTRY_TYPE = 'Type';
var CALENDAR_ENTRY_TIME = 'Time';
var CALENDAR_ENTRY_DATE = 'Date';
var CALENDAR_NO_ENTRIES = 'There are no entries to display';
var CALENDAR_SERVER_ERROR_MESSAGE = 'can not connect to:';
var CALENDAR_LOADING_MESSAGE = 'Loading...';
var CALENDAR_PREVIOUS_LABEL = 'Previous';
var CALENDAR_NEXT_LABEL = 'Next';
var CALENDAR_DAY_LABEL = 'Day';
var CALENDAR_WEEK_LABEL = 'Week';
var CALENDAR_WORK_WEEK_LABEL = 'Work week';
var CALENDAR_MONTH_LABEL = 'Month';

//	CSS style classes
var entryListTableStyleClass = 'entryList';
var entryListCaptionStyleClass = 'entryListCaption';
var entryListEvenRowStyleClass = 'entryListEvenRow';
var entryListOddRowStyleClass = 'entryListOddRow';
var entryListElementStyleClass = 'entryListElement';
var entryInfoStyleClass = 'entryInfo';
var entryListElementTimeStyleClass = 'entryListElementTime';
var entryInfoNameStyleClass = 'entryInfoName';
var entryInfoDateStyleClass = 'entryInfoDate';
var entryInfoTimeStyleClass = 'entryInfoTime';
var entryInfoTypeStyleClass = 'entryInfoType';
var entryInfoDescriptionStyleClass = 'entryInfoDescription';
var ENTRY_IN_SCHEDULE_STYLE_CLASS = 'scheduleEntry';

	//	Returns layer with schedule buttons (previous, next, day...). Those buttons swithches to next or previous day/workweek/week/month, or changes view mode.
	function getScheduleButtons(id) {
		var extendedProperties = getExtendedCalendarViewerPropertiesBean(id);
		
		//	Next
		var scheduleNextButton = new Element('input');
		scheduleNextButton.setProperty('type', 'button');
		scheduleNextButton.setProperty('id', id + '_nextButton');
		scheduleNextButton.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, id);
		scheduleNextButton.addClass('scheduleNextButtonStyleClass');	
		scheduleNextButton.setProperty('value', CALENDAR_NEXT_LABEL);
		scheduleNextButton.removeEvents('click');
		scheduleNextButton.addEvent('click', function() {
			getNext(scheduleNextButton.getProperty('id'));
		});
		
		//	Previous
		var schedulePreviousButton = new Element('input');
		schedulePreviousButton.setProperty('type', 'button');
		schedulePreviousButton.setProperty('id', id + '_previousButton');
		schedulePreviousButton.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, id);
		schedulePreviousButton.addClass('schedulePreviousButtonStyleClass');	
		schedulePreviousButton.setProperty('value', CALENDAR_PREVIOUS_LABEL);	
		schedulePreviousButton.removeEvents('click');
		schedulePreviousButton.addEvent('click', function() {
			getPrevious(schedulePreviousButton.getProperty('id'));
		});
		
		//	Day
		var scheduleDayButton = new Element('input');
		scheduleDayButton.setProperty('type', 'button');
		scheduleDayButton.setProperty('id', id + '_dayButton');
		scheduleDayButton.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, id);
		scheduleDayButton.addClass('scheduleDayButtonStyleClass');	
		scheduleDayButton.setProperty('value', CALENDAR_DAY_LABEL);	
		scheduleDayButton.removeEvents('click');
		scheduleDayButton.addEvent('click', function() {
			changeModeToDay(scheduleDayButton.getProperty('id'));
		});
		
		//	Week
		var scheduleWeekButton = new Element('input');
		scheduleWeekButton.setProperty('type', 'button');
		scheduleWeekButton.setProperty('id', id + '_weekButton');
		scheduleWeekButton.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, id);
		scheduleWeekButton.addClass('scheduleWeekButtonStyleClass');	
		scheduleWeekButton.setProperty('value', CALENDAR_WEEK_LABEL);	
		scheduleWeekButton.removeEvents('click');
		scheduleWeekButton.addEvent('click', function() {
			changeModeToWeek(scheduleWeekButton.getProperty('id'));
		});
		
		//	Work week
		var scheduleWorkweekButton = new Element('input');
		scheduleWorkweekButton.setProperty('type', 'button');
		scheduleWorkweekButton.setProperty('id', id + '_workWeekButton');
		scheduleWorkweekButton.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, id);
		scheduleWorkweekButton.addClass('scheduleWorkweekButtonStyleClass');	
		scheduleWorkweekButton.setProperty('value', CALENDAR_WORK_WEEK_LABEL);
		scheduleWorkweekButton.removeEvents('click');
		scheduleWorkweekButton.addEvent('click', function() {
			changeModeToWorkweek(scheduleWorkweekButton.getProperty('id'));
		});
		
		//	Month
		var scheduleMonthButton = new Element('input');
		scheduleMonthButton.setProperty('type', 'button');
		scheduleMonthButton.setProperty('id', id + '_monthButton');
		scheduleMonthButton.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, id);
		scheduleMonthButton.addClass('scheduleMonthButtonStyleClass');	
		scheduleMonthButton.setProperty('value', CALENDAR_MONTH_LABEL);
		scheduleMonthButton.removeEvents('click');
		scheduleMonthButton.addEvent('click', function() {
			changeModeToMonth(scheduleMonthButton.getProperty('id'));
		});
		
		var scheduleButtonsLayer = new Element('div');
		if (!extendedProperties.properties.hideMenu) {
			if (!extendedProperties.properties.hidePreviousAndNext){
				scheduleButtonsLayer.appendChild(schedulePreviousButton);
				scheduleButtonsLayer.appendChild(scheduleNextButton);				
			}
			scheduleButtonsLayer.appendChild(scheduleDayButton);		
			scheduleButtonsLayer.appendChild(scheduleWeekButton);
			scheduleButtonsLayer.appendChild(scheduleWorkweekButton);		
			scheduleButtonsLayer.appendChild(scheduleMonthButton);
		}
		
		return  scheduleButtonsLayer;	
	}
	
	function calendarChangeModeCallback(entries, extendedProperties) {
		var newProperties = new CopiedExtendedCalendarViewerPropertiesBean(entries, extendedProperties);
		addExtendedCalendarViewerPropertiesBean(newProperties);
				
		if (newProperties.properties.showEntriesAsList) {
			displayEntriesAsList(newProperties.id);
		}
		else {
			ScheduleSession.getScheduleDOM(newProperties.id, {
				callback: function(component) {
					displayCalendarEntries(component, newProperties.id);
				}
			});
		}
	}
	
	function getExtendedPropertiesByElementIdAndAttributeValue(elementId) {
		if (elementId == null) {
			return false;
		}
		
		var element = $(elementId);
		if (element == null) {
			return false;
		}
		
		return getExtendedCalendarViewerPropertiesBean(element.getProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID));
	}
	
	//	Switches schedule to next	
	function getNext(elementId) {
		var extendedProperties = getExtendedPropertiesByElementIdAndAttributeValue(elementId);
		if (extendedProperties == null) {
			return false;
		}
		
		ScheduleSession.switchToNextAndGetListOfEntries(extendedProperties.properties.instanceId, {
			callback: function(entries) {
				calendarChangeModeCallback(entries, extendedProperties);
			}
		});

	}
	
	//	Switches schedule to previous	
	function getPrevious(elementId) {
		var extendedProperties = getExtendedPropertiesByElementIdAndAttributeValue(elementId);
		if (extendedProperties == null) {
			return false;
		}
		
		ScheduleSession.switchToPreviousAndGetListOfEntries(extendedProperties.properties.instanceId, {
			callback: function(entries) {
				calendarChangeModeCallback(entries, extendedProperties);
			}
		});
	}
	
	//	Changes mode to day	
	function changeModeToDay(elementId) {
		var extendedProperties = getExtendedPropertiesByElementIdAndAttributeValue(elementId);
		if (extendedProperties == null) {
			return false;
		}
		
		extendedProperties.currentMode = CALENDAR_DAY_MODE;
		addExtendedCalendarViewerPropertiesBean(extendedProperties);
		
		ScheduleSession.changeModeToDayAndGetListOfEntries(extendedProperties.properties.instanceId, {
			callback: function(entries) {
				calendarChangeModeCallback(entries, extendedProperties);
			}
		});
	}

	//	Changes mode to workweek		
	function changeModeToWorkweek(elementId) {
		var extendedProperties = getExtendedPropertiesByElementIdAndAttributeValue(elementId);
		if (extendedProperties == null) {
			return false;
		}
		
		extendedProperties.currentMode = CALENDAR_WORK_WEEK_MODE;
		addExtendedCalendarViewerPropertiesBean(extendedProperties);
		
		ScheduleSession.changeModeToWorkweekAndGetListOfEntries(extendedProperties.properties.instanceId, {
			callback: function(entries) {
				calendarChangeModeCallback(entries, extendedProperties);
			}
		});
	}	
	
	// Changes mode to week
	function changeModeToWeek(elementId) {
		var extendedProperties = getExtendedPropertiesByElementIdAndAttributeValue(elementId);
		if (extendedProperties == null) {
			return false;
		}
		
		extendedProperties.currentMode = CALENDAR_WEEK_MODE;
		addExtendedCalendarViewerPropertiesBean(extendedProperties);
		
		ScheduleSession.changeModeToWeekAndGetListOfEntries(extendedProperties.properties.instanceId, {
			callback: function(entries) {
				calendarChangeModeCallback(entries, extendedProperties);
			}
		});
	}		

	//	Changes mode to month
	function changeModeToMonth(elementId) {
		var extendedProperties = getExtendedPropertiesByElementIdAndAttributeValue(elementId);
		if (extendedProperties == null) {
			return false;
		}
		
		extendedProperties.currentMode = CALENDAR_MONTH_MODE;
		addExtendedCalendarViewerPropertiesBean(extendedProperties);
		
		ScheduleSession.changeModeToMonthAndGetListOfEntries(extendedProperties.properties.instanceId, {
			callback: function(entries) {
				calendarChangeModeCallback(entries, extendedProperties);
			}
		});
	}

	//	Inserts schedule DOM object
	function displayCalendarEntries(component, id) {
		var extendedProperties = getExtendedCalendarViewerPropertiesBean(id);
		if (extendedProperties == null) {
			return false;
		}
		
		var scheduleLayer = $(extendedProperties.containerId);
		var scheduleEntries = $(CALENDAR_SCHEDULE_ENTRY_TABLE_ID);
		
		if (scheduleEntries == null) {
			scheduleEntries = new Element('div');
			scheduleEntries.setProperty('id', CALENDAR_SCHEDULE_ENTRY_TABLE_ID);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons(id));
		}
		else {
			scheduleEntries.empty();
		}
		
		insertNodesToContainer(component, scheduleEntries);

		setBehaviourOnScheduleEntries(id);
		closeAllLoadingMessages();	
	}
	
	//	Inserts entry list into DOM
	function displayEntriesAsList(id){	
		var extendedProperties = getExtendedCalendarViewerPropertiesBean(id);
		if (extendedProperties == null) {
			return false;
		}
		
		var scheduleLayer = $(extendedProperties.containerId); 	
		var scheduleEntries = $(CALENDAR_SCHEDULE_ENTRY_TABLE_ID);
		if (scheduleEntries == null) {
			scheduleEntries = new Element('div');
			scheduleEntries.setProperty('id', CALENDAR_SCHEDULE_ENTRY_TABLE_ID);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons(id));
		}
		else {
			scheduleEntries.empty();
		}
		
		var entries = extendedProperties.entries;
		if (entries == null || entries.length == 0) {
			var txtEntries = document.createTextNode(CALENDAR_NO_ENTRIES);
			scheduleEntries.appendChild(txtEntries);
			closeAllLoadingMessages();
			return false;
		}
		
		var entryIdText = 'entryid';
		for (var i = 0; i < entries.length; i++) {
			var entry = entries[i];
			
			var entryContainer = new Element('div');
			
			var entryLink = new Element('a');
			entryLink.setProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID, extendedProperties.properties.instanceId);
			entryLink.setProperty(entryIdText, entry.id);
			entryLink.injectInside(entryContainer);
			var entryText = entry.entryName;
			if (extendedProperties.properties.showTime) {
				var startDate = entry.localizedEntryDate;
				if (startDate == null) {
					startDate = entry.entryDate;
					if (startDate == null) {
						startDate = '';
					}
				}
				var startTime = entry.entryTime;
				if (startTime == null) {
					startTime = '';
				}
				
				var endDate = entry.localizedEntryEndDate;
				if (endDate == null) {
					endDate = entry.entryEndDate;
					if (endDate == null) {
						endDate = '';
					}
				}
				var endTime = entry.entryEndTime;
				if (endTime == null) {
					endTime = '';
				}
				
				entryText += ' ' + startDate + ' ' + startTime + ' - ' + endDate + ' ' + endTime;
			}
			entryLink.setText(entryText);
			entryLink.setProperty('href', 'javascript:void(0)');
			addEventToCalendarEntryToShowInfoWindow(entryLink, null);
			
			entryContainer.injectInside(scheduleEntries);
		}
		
		closeAllLoadingMessages();
	}

	function setEventsForShowHideCalendarEntryContainer(element) {
		element.removeEvents('mouseenter');
		element.removeEvents('mouseleave');
				
		element.addEvent('mouseenter', function(e) {
		});
		
		element.addEvent('mouseleave', function() {
		});
	}
	
	function addEventToCalendarEntryToShowInfoWindow(element, id) {
		if (element == null) {
			return false;
		}
		element.removeEvents('click');
		
		var entryIdText = 'entryid';
		element.addEvent('click', function() {
			var clickedEntryId = -1;
			var clickedEntryObject = null;
		
			var clickedElement = $(this);
			if (clickedElement != null) {
				clickedEntryId = clickedElement.getProperty(entryIdText);
				var scheduleId = id;
				if (scheduleId == null) {
					scheduleId = clickedElement.getProperty(CALENDAR_ELEMENT_PROPERTY_NAME_FOR_BEAN_ID);
				}
				var extendedProperties = getExtendedCalendarViewerPropertiesBean(scheduleId);
				if (extendedProperties != null) {
					if (extendedProperties.entries != null) {
						for (var i = 0; (i < extendedProperties.entries.length && clickedEntryObject == null); i++) {
							if (clickedEntryId == extendedProperties.entries[i].id) {
								clickedEntryObject = extendedProperties.entries[i];
							}
						}
					}
				}
			}
			
			ScheduleSession.addCalendarEntryForInfoWindow(clickedEntryObject, {
				callback: function(result) {
					if (!result) {
						return false;
					}
					
					var link = CALENDAR_ENTRY_INFO_WINDOW_LINK + '&'+entryIdText+'=' + clickedEntryId;
					MOOdalBox.init({resizeDuration: 50, evalScripts: true, animateCaption: false});
					var result = MOOdalBox.open(link, CALENDAR_ENTRY_INFO_WINDOW_TITLE, '600 300');
				}
			});
		});
	}
	
	//	Setting behaviour on entries in schedule
	function setBehaviourOnScheduleEntries(id) {
		$$('a.'+ENTRY_IN_SCHEDULE_STYLE_CLASS).each(
			function(element) {
				setEventsForShowHideCalendarEntryContainer(element);
				
				addEventToCalendarEntryToShowInfoWindow(element, id);
    		}
	    );
	    
		$$('a.entry').each(
			function(element) {
				setEventsForShowHideCalendarEntryContainer(element);
				
				addEventToCalendarEntryToShowInfoWindow(element, id);
	    	}
	    );
	    
	    $$('a.workweekEntry').each(
			function(element) {
				setEventsForShowHideCalendarEntryContainer(element);
				
				addEventToCalendarEntryToShowInfoWindow(element, id);
	    	}
	    );
	    
	    var extendedProperties = getExtendedCalendarViewerPropertiesBean(id);
	    if (extendedProperties == null) {
	    	return false;
	    }
	}
	
	function addExtendedCalendarViewerPropertiesBean(extendedProperties) {
		if (CALENDAR_VIEWER_EXTENDED_PROPERTIES == null) {
			CALENDAR_VIEWER_EXTENDED_PROPERTIES = new Array();
		}
		
		var oldProperties = getExtendedCalendarViewerPropertiesBean(extendedProperties.id);
		if (oldProperties != null) {
			removeElementFromArray(CALENDAR_VIEWER_EXTENDED_PROPERTIES, oldProperties);
		}
		
		CALENDAR_VIEWER_EXTENDED_PROPERTIES.push(extendedProperties);
	}
	
	function getExtendedCalendarViewerPropertiesBean(id) {
		if (id == null || CALENDAR_VIEWER_EXTENDED_PROPERTIES == null) {
			return null;
		}
		
		var propertiesBean = null;
		for (var i = 0; (i < CALENDAR_VIEWER_EXTENDED_PROPERTIES.length && propertiesBean == null); i++) {
			if (CALENDAR_VIEWER_EXTENDED_PROPERTIES[i].id == id) {
				propertiesBean = CALENDAR_VIEWER_EXTENDED_PROPERTIES[i];
			}
		}
		
		return propertiesBean;
	}
	
	function ExtendedCalendarViewerPropertiesBean(entries, properties, containerId) {
		this.entries = entries;
		this.properties = properties;
		this.containerId = containerId;
		this.id = properties.instanceId;
		this.currentMode = CALENDAR_MONTH_MODE;
	}
	
	function CopiedExtendedCalendarViewerPropertiesBean(entries, extendedProperties) {
		this.entries = entries;
		this.properties = extendedProperties.properties;
		this.containerId = extendedProperties.containerId;
		this.id = extendedProperties.properties.instanceId;
		this.currentMode = extendedProperties.currentMode;
	}
	
	//	Sends first package of entries to server side
	function addEntriesToListOrSchedule(entries, extendedProperties, firstTimeCalling) {
		if (entries != null && entries.length > 0) {
			//	Streaming entries
			var pack = new Array();
			if (entries.length > ENTRIES_PACKAGE_SIZE) {
				for (var i = 0; i < ENTRIES_PACKAGE_SIZE; i++) {
					pack.push(entries[i]);
				}
			}
			else {
				for (var i = 0; i < entries.length; i++) {
					pack.push(entries[i]);
				}
			}
			
			ScheduleSession.addEntries(pack, extendedProperties.properties.instanceId, firstTimeCalling, {
				callback: function(result) {
					for (var i = 0; i < pack.length; i++) {
						removeElementFromArray(entries, pack[i]);	//	Removing streamed entries
					}
					
					addEntriesToListOrSchedule(entries, extendedProperties, false);
				}
			});
		}
		else {
			//	Rendering view
			displayAllEntriesInMonthMode(extendedProperties);
		}
	}
	
	//	Entries are displayed as list or component is rendered and inserted in DOM
	function displayAllEntriesInMonthMode(extendedProperties) {
		closeAllLoadingMessages();
		if (extendedProperties.properties.showEntriesAsList) {
			displayEntriesAsList(extendedProperties.properties.instanceId);
		}
		else {
			ScheduleSession.getScheduleDOM(extendedProperties.properties.instanceId, {
				callback: function(component) {
					displayCalendarEntries(component, extendedProperties.properties.instanceId);
				}
			});
		}
	}