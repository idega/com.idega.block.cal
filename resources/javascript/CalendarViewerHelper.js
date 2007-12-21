var CALENDAR_EVENT_ADVANCED_PROPERTY_NAME = 'calendareventtype';
var CALENDAR_LEDGER_ADVANCED_PROPERTY_NAME = 'calendarledgertype';

var CALENDAR_VIEWER_UNIQUE_IDS_CACHE_NAME = 'calendarViewersUniqueIdsCache';
var EVENTS_FOR_CALENDAR_VIEWER_IDS_CACHE_NAME = 'eventsForCalendarViewersUniqueIdsCache';
var LEDGERS_FOR_CALENDAR_VIEWER_IDS_CACHE_NAME = 'ledgersForCalendarViewersUniqueIdsCache';

var CALENDAR_EVENTS_CONTAINER_ID = null;
var CALENDAR_LEDGERS_CONTAINER_ID = null;

var CALENDAR_NO_EVENTS_TEXT = 'Sorry, there are no events created.';
var CALENDAR_NO_LEDGERS_TEXT = 'Sorry, there are no ledgers created.';
var CALENDAR_ENTRY_INFO_WINDOW_TITLE = 'Calendar entry information';

var CALENDAR_ENTRY_INFO_WINDOW_LINK = '/servlet/ObjectInstanciator?idegaweb_instance_class=com.idega.block.cal.presentation.CalendarEntryInfoBlock';

function getSimpleCalendarTypes(server, login, password, remoteMode, containerId, selectedEventTypes) {
	if (containerId == null) {
		return false;
	}
	
	checkOtherProperties(null);
	
	CALENDAR_EVENTS_CONTAINER_ID = containerId;
	
	if (remoteMode) {
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.canUseRemoteServer(server, {
			callback: function(result) {
				if (!result) {
					return false;
				}
				
				prepareDwr(CalService, server + getDefaultDwrPath());
				CalService.getAvailableCalendarEventTypesWithLogin(login, decode64(password), {
					callback: function(events) {
						addAdvancedPropertiesForCalendar(events, containerId, selectedEventTypes, true);
					},
					rpcType: dwr.engine.ScriptTag
				});
			},
			rpcType: dwr.engine.XMLHttpRequest
		});
	}
	else {
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getAvailableCalendarEventTypes({
			callback: function(events) {
				addAdvancedPropertiesForCalendar(events, containerId, selectedEventTypes, true);
			},
			rpcType: dwr.engine.XMLHttpRequest
		});
	}
}

function getSimpleCalendarLedgers(server, login, password, remoteMode, containerId, selectedLedgers) {
	if (containerId == null) {
		return false;
	}
	
	checkOtherProperties(null);
	
	CALENDAR_LEDGERS_CONTAINER_ID = containerId;
	
	if (remoteMode) {
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.canUseRemoteServer(server, {
			callback: function(result) {
				if (result) {
					prepareDwr(CalService, server + getDefaultDwrPath());
					CalService.getAvailableLedgersWithLogin(login, decode64(password), {
						callback: function(ledgers) {
							addAdvancedPropertiesForCalendar(ledgers, containerId, selectedLedgers, false);
						},
						rpcType: dwr.engine.ScriptTag
					});
				}
				else {
					closeAllLoadingMessages();
					return false;
				}
			},
			rpcType: dwr.engine.XMLHttpRequest
		});
	}
	else {
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getAvailableLedgers({
			callback: function(ledgers) {
				addAdvancedPropertiesForCalendar(ledgers, containerId, selectedLedgers, false);
			},
			rpcType: dwr.engine.XMLHttpRequest
		});
	}
}

function addAdvancedPropertiesForCalendar(properties, containerId, selectedProperties, events) {
	var container = $(containerId);
	if (container == null) {
		return false;
	}
	
	container.empty();
	
	if (properties == null || properties.length == 0) {
		if (events) {
			container.setText(CALENDAR_NO_EVENTS_TEXT);
		}
		else {
			container.setText(CALENDAR_NO_LEDGERS_TEXT);
		}
		return false;
	}
	
	for (var i = 0; i < properties.length; i++) {
		var propertyContainer = new Element('div');
		propertyContainer.injectInside(container);
		
		var checkBox = new Element('input');
		checkBox.setProperty('type', 'checkbox');
		checkBox.setProperty('value', properties[i].id);
		checkBox.addEvent('click', function() {
			var value = this.value;
			if (value == null) {
				return false;
			}
			
			var key = CALENDAR_LEDGER_ADVANCED_PROPERTY_NAME;
			if (events) {
				key = CALENDAR_EVENT_ADVANCED_PROPERTY_NAME;
			}
			
			var fontWeightValue = 'normal';
			var ids = groups_chooser_helper.getAdvancedProperty(key);
			if (this.checked) {
				// Add property
				addElementValueForAdvancedProperty(ids, key, value);
				
				fontWeightValue = 'bold';
			}
			else {
				//	Remove property
				removeElementValueForAdvancedProperty(ids, key, value);
			}
			
			var spanElement = $(this).getNext();
			spanElement.setStyle('font-weight', fontWeightValue);
			
			checkOtherProperties(null);
		});
		
		var isSelected = existsElementInArray(selectedProperties, properties[i].id);
		if (events && (selectedProperties == null || selectedProperties.length == 0)) {
			isSelected = true;
		}
		
		if (isSelected) {
			var key = CALENDAR_LEDGER_ADVANCED_PROPERTY_NAME;
			if (events) {
				key = CALENDAR_EVENT_ADVANCED_PROPERTY_NAME;
			}
			var ids = groups_chooser_helper.getAdvancedProperty(key);
			addElementValueForAdvancedProperty(ids, key, properties[i].id);
			
			checkBox.checked = true;
		}
		checkBox.injectInside(propertyContainer);
		
		var propertyNameSpan = new Element('span');
		propertyNameSpan.addClass('calendarViewerEventOrLedgerPropertyNameStyleClass');
		propertyNameSpan.appendText(properties[i].value);
		var fontWeightValue = 'normal';
		if (isSelected) {
			fontWeightValue = 'bold';
		}
		propertyNameSpan.setStyle('font-weight', fontWeightValue);
		propertyNameSpan.injectInside(propertyContainer);
	}
}

function reloadPropertiesForCalendarViewer(instanceId, containerId, message) {
	if (instanceId == null || containerId == null) {
		reloadPage();
		return false;
	}
	
	closeAllLoadingMessages();
	showLoadingMessage(message);
	
	prepareDwr(ScheduleSession, getDefaultDwrPath());
	ScheduleSession.removeCalendar(instanceId, {
		callback: function(result) {
			prepareDwr(CalService, getDefaultDwrPath());
			CalService.reloadProperties(instanceId, {
				callback: function(properties) {
					if (properties == null) {
						closeAllLoadingMessages();
						var reloadPageCustomEvent = function() {
							reloadPage();
						}
						MOOdalBox.addEventToCloseAction(reloadPageCustomEvent);
						return false;
					}
					
					var dwrCallPath = getDefaultDwrPath();
					var dwrCallType = dwr.engine.XMLHttpRequest;
					if (properties.remoteMode) {
						dwrCallPath = properties.server + dwrCallPath;
						dwrCallType = dwr.engine.ScriptTag;
					}
					prepareDwr(CalService, dwrCallPath);
					CalService.removeCelandarEntriesFromCache(instanceId, {
						callback: function(result) {
							closeAllLoadingMessages();
							
							loadCalendarViewer(containerId, instanceId, message);
						},
						rpcType: dwrCallType
					});
				},
				rpcType: dwr.engine.XMLHttpRequest
			});
		},
		rpcType: dwr.engine.XMLHttpRequest
	});
}

//	Initial step
function loadCalendarViewer(id, instanceId, message) {
	showLoadingMessage(message);
	
	prepareDwr(CalService, getDefaultDwrPath());
	CalService.getCalendarInformation({
		callback: function(info) {
			if (info != null && info.length != 28) {
				CALENDAR_EVENT_ADVANCED_PROPERTY_NAME = info[0];		//	0
				CALENDAR_LEDGER_ADVANCED_PROPERTY_NAME = info[1];		//	1
				
				CALENDAR_NO_EVENTS_TEXT = info[2];						//	2
				CALENDAR_NO_LEDGERS_TEXT = info[3];						//	3
				
				CALENDAR_VIEWER_UNIQUE_IDS_CACHE_NAME = info[4];		//	4
				EVENTS_FOR_CALENDAR_VIEWER_IDS_CACHE_NAME = info[5];	//	5
				LEDGERS_FOR_CALENDAR_VIEWER_IDS_CACHE_NAME = info[6];	//	6
				
				CALENDAR_MONTH_MODE = info[7];							//	7
				CALENDAR_DAY_MODE = info[8];							//	8
				CALENDAR_WEEK_MODE = info[9];							//	9
				CALENDAR_WORK_WEEK_MODE = info[10];						//	10
				
				CALENDAR_ENTRY_NAME = info[11];							//	11
				CALENDAR_ENTRY_END_DATE = info[12];						//	12
				CALENDAR_ENTRY_TYPE = info[13];							//	13
				CALENDAR_ENTRY_TIME = info[14];							//	14
				CALENDAR_ENTRY_DATE = info[15];							//	15
				CALENDAR_NO_ENTRIES = info[16];							//	16
				CALENDAR_SERVER_ERROR_MESSAGE = info[17];				//	17
				CALENDAR_LOADING_MESSAGE = info[18];					//	18
				
				CALENDAR_PREVIOUS_LABEL = info[19];						//	19
				CALENDAR_NEXT_LABEL = info[20];							//	20
				CALENDAR_DAY_LABEL = info[21];							//	21
				CALENDAR_WEEK_LABEL = info[22];							//	22
				CALENDAR_WORK_WEEK_LABEL = info[23];					//	23
				CALENDAR_MONTH_LABEL = info[24];						//	24
				
				ENTRY_IN_SCHEDULE_STYLE_CLASS = info[25];				//	25
				
				CALENDAR_ENTRY_INFO_WINDOW_LINK = info[26];				//	26
				CALENDAR_ENTRY_INFO_WINDOW_TITLE = info[27];			//	27
			}
			
			getCalendarViewerProperties(instanceId, id, message);
		},
		rpcType: dwr.engine.XMLHttpRequest
	});
}

//	Getting properties for viewer
function getCalendarViewerProperties(instanceId, containerId, message) {
	prepareDwr(CalService, getDefaultDwrPath());
	CalService.getCalendarProperties(instanceId, {
		callback: function(properties) {
			if (properties == null) {
				closeAllLoadingMessages();
				return false;
			}
			
			getCalendarItemsByViewerProperties(properties, containerId);
		},
		rpcType: dwr.engine.XMLHttpRequest
	});
}

//	Checking if can use remote server
function getCalendarItemsByViewerProperties(properties, containerId) {
	if (properties.remoteMode) {
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.canUseRemoteServer(properties.server, {
			callback: function(result) {
				if (result) {
					addCalendarViewerAfterServerIsChecked(properties, containerId);
				}
				else {
					closeAllLoadingMessages();
					return false;
				}
			},
			rpcType: dwr.engine.XMLHttpRequest
		});
	}
	else {
		addCalendarViewerAfterServerIsChecked(properties, containerId);
	}
}

//	Sending groups ids to server
function addCalendarViewerAfterServerIsChecked(properties, containerId) {
	if (IE && properties.uniqueIds != null) {
		if (properties.uniqueIds.length > 20) {
			streamUniqueIdsToServer(properties.instanceId, properties.uniqueIds, properties.server, properties.remoteMode, CALENDAR_VIEWER_UNIQUE_IDS_CACHE_NAME);
			
			sendIdsOfCalendarLedgers(properties, containerId);
			return false;
		}
	}

	var dwrCallType = dwr.engine.XMLHttpRequest;
	var dwrCallPath = getDefaultDwrPath();
	if (properties.remoteMode) {
		dwrCallPath = properties.server + getDefaultDwrPath();
		dwrCallType = dwr.engine.ScriptTag;
	}
	
	prepareDwr(CalService, dwrCallPath);
	
	CalService.addUniqueIdsForCalendarGroups(properties.instanceId, properties.uniqueIds, {
		callback: function(result) {
			sendIdsOfCalendarLedgers(properties, containerId);
		},
		rpcType: dwrCallType
	});
}

//	Sending ids of ledgers
function sendIdsOfCalendarLedgers(properties, containerId) {
	if (IE && properties.ledgers != null) {
		if (properties.ledgers.length > 20) {
			streamUniqueIdsToServer(properties.instanceId, properties.ledgers, properties.server, properties.remoteMode, LEDGERS_FOR_CALENDAR_VIEWER_IDS_CACHE_NAME);
			
			sendIdsOfCalendarEvents(properties, containerId);
			return false;
		}
	}

	var dwrCallType = dwr.engine.XMLHttpRequest;
	var dwrCallPath = getDefaultDwrPath();
	if (properties.remoteMode) {
		dwrCallPath = properties.server + getDefaultDwrPath();
		dwrCallType = dwr.engine.ScriptTag;
	}
	
	prepareDwr(CalService, dwrCallPath);
	
	CalService.addUniqueIdsForCalendarLedgers(properties.instanceId, properties.ledgers, {
		callback: function(result) {			
			sendIdsOfCalendarEvents(properties, containerId);
		},
		rpcType: dwrCallType
	});
}

//	Sending ids of events
function sendIdsOfCalendarEvents(properties, containerId) {
	if (IE && properties.events != null) {
		if (properties.events.length > 20) {
			streamUniqueIdsToServer(properties.instanceId, properties.events, properties.server, properties.remoteMode, EVENTS_FOR_CALENDAR_VIEWER_IDS_CACHE_NAME);
			
			getCalendarEntries(properties, containerId);
			return false;
		}
	}

	var dwrCallType = dwr.engine.XMLHttpRequest;
	var dwrCallPath = getDefaultDwrPath();
	if (properties.remoteMode) {
		dwrCallPath = properties.server + getDefaultDwrPath();
		dwrCallType = dwr.engine.ScriptTag;
	}
	
	prepareDwr(CalService, dwrCallPath);
	
	CalService.addUniqueIdsForCalendarEvents(properties.instanceId, properties.events, {
		callback: function(result) {			
			getCalendarEntries(properties, containerId);
		},
		rpcType: dwrCallType
	});
}

function getCalendarEntries(properties, containerId) {
	var dwrCallType = dwr.engine.XMLHttpRequest;
	var dwrCallPath = getDefaultDwrPath();
	if (properties.remoteMode) {
		dwrCallPath = properties.server + getDefaultDwrPath();
		dwrCallType = dwr.engine.ScriptTag;
	}
	
	prepareDwr(CalService, dwrCallPath);
	
	CalService.getCalendarEntries(properties.login, properties.password, properties.instanceId, properties.cacheTime, properties.remoteMode, {
		callback: function(entries) {
			addCalendarEntriesIntoContainer(entries, containerId, properties);
		},
		rpcType: dwrCallType
	});
}

function addCalendarEntriesIntoContainer(entries, containerId, properties) {
	if (containerId == null) {
		return false;
	}
	
	var container = $(containerId);
	if (container == null) {
		return false;
	}
	
	container.empty();
	
	var extendedProperties = new ExtendedCalendarViewerPropertiesBean(entries, properties, containerId);
	addExtendedCalendarViewerPropertiesBean(extendedProperties);
	
	var cloneOfEntries = new Array();
	if (entries != null) {
		for (var i = 0; i < entries.length; i++) {
			cloneOfEntries.push(entries[i]);
		}
	}
	
	addEntriesToListOrSchedule(cloneOfEntries, extendedProperties, true);
}