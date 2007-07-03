var arrayOfParameters = null;
var arrayOfCheckedParameters = null;
var arrayOfEntryIds = null;
var scheduleEntryTableId = 'scheduleEntryTableId';
var scheduleButtonsId = 'scheduleButtonsId';
var groups_and_calendar_chooser_helper = null;
var serverErrorMessage = 'can\'t connect to:';
var scheduleId = null;
var arrayOfCheckedCalendarParameters = new Array();
var groups_and_calendar_chooser_helper = null;

var loadingMsg = 'Loading...';

var showEntriesAsList = false;
var showMenu = false;
var showPreviousAndNext = false;
var entryNumber = 'Number';
var entryName = 'Name';
var entryDate = 'Begin date';
var entryEndDate = 'End date';
var entryType = 'Type';
var entryListTableStyleClass = 'entryListTableStyleClass';
var entryListTableCaptionStyleClass = 'entryListTableCaptionStyleClass';
var calendarProperties = null;

var entriesToCalendar = null;
var entriesPackageSize = 10;
var packageIndex = 0;
var packageEndIndex = entriesPackageSize;
var noEntries = 'There are no entries to display';
/*
function setCalendars(calendars, id){
	var parentOfList = document.getElementById(id);
	var ledgersList = document.createElement('div');
	var entryTypesList = document.createElement('div');
	
	var ledgersListCaption = document.createTextNode('Ledgers: '); 					
	ledgersList.appendChild(ledgersListCaption);
	var typesListCaption = document.createTextNode('Types: '); 					
	entryTypesList.appendChild(typesListCaption);
		
	var element = null;
	for (var i = 0; i < calendars.length; i++){
		element = calendars[i];
		if(element.type == 'L'){
			var ledgerElement = document.createElement('input');
			ledgerElement.setAttribute('id', element.type + element.elementId);						
			ledgerElement.setAttribute('elementId',element.elementId);			
			ledgerElement.setAttribute('name',element.name);
			ledgerElement.setAttribute('type','checkbox');		
			var ledgerCaption = document.createTextNode(element.name); 
			ledgersList.appendChild(ledgerElement);
			ledgersList.appendChild(ledgerCaption);
			
			arrayOfParameters.push(element.elementId + element.type);
		}	
		if(element.type == 'T'){
			ledgerElement.setAttribute('id',element.elementId + element.type);									
			var typeElement = document.createElement('input');
			typeElement.setAttribute('elementId',element[0]);			
			typeElement.setAttribute('name',element[1]);
			typeElement.setAttribute('type','checkbox');				
			var typeCaption = document.createTextNode(element.name); 					
			entryTypesList.appendChild(typeElement);
			entryTypesList.appendChild(typeCaption);
			
			arrayOfParameters.push(element.elementId + element.type);			
		}	
	}

	parentOfList.appendChild(ledgersList);
//	parentOfList.appendChild(typesListCaption);	
	parentOfList.appendChild(entryTypesList);	
}
*/
function displayCalendarAttributes(calendars){
	closeLoadingMessage();
	arrayOfParameters = new Array();	
	var parentOfList = document.getElementById('calendar_list_container_id');
//	parentOfList.setAttribute('class','calendarChooserStyleClass');
	removeChildren(parentOfList);
	var ledgersList = document.createElement('div');
	var entryTypesList = document.createElement('div');
/*	
	var ledgersListCaption = document.createTextNode('Ledgers: '); 					
	ledgersList.appendChild(ledgersListCaption);
	var typesListCaption = document.createTextNode('Types: '); 					
	entryTypesList.appendChild(typesListCaption);
*/		
	var tableOfParameters = document.createElement('table');
	var trLedgers = document.createElement('tr');
	var trTypes = document.createElement('tr');
	
	var tdLedgersCaption = document.createElement('td');
	var tdLedgersList = document.createElement('td');
	var tdTypesCaption = document.createElement('td');
	var tdTypesList = document.createElement('td');
		
	tdLedgersCaption.appendChild(document.createTextNode('Ledgers:'));
	tdTypesCaption.appendChild(document.createTextNode('Types:'));
		
	var element = null;
	var getEntriesButton = null;
	var addLedgerList = false;
	var addEntryTypesList = false;
	for (var i = 0; i < calendars.length; i++){
		element = calendars[i];
		if(element.type == 'L'){
			var ledgerElement = document.createElement('input');
			ledgerElement.setAttribute('id', element.type + element.id);									
			ledgerElement.setAttribute('elementId',element.id);			
			ledgerElement.setAttribute('name',element.name);
			ledgerElement.setAttribute('elementType',element.type);			
			ledgerElement.setAttribute('type','checkbox');		
			ledgerElement.setAttribute('class','calendarCheckbox');
			var ledgerCaption = document.createTextNode(element.name); 
			ledgersList.appendChild(ledgerElement);
			ledgersList.appendChild(ledgerCaption);
			
			arrayOfParameters.push(element.type + element.id);
			addLedgerList = true;
		}	
		if(element.type == 'T'){
			var typeElement = document.createElement('input');
			typeElement.setAttribute('id', element.type + element.id);												
			typeElement.setAttribute('elementId',element.id);			
			typeElement.setAttribute('name',element.name);
			typeElement.setAttribute('elementType',element.type);			
			typeElement.setAttribute('type','checkbox');				
			typeElement.setAttribute('class','calendarCheckbox');			
			var typeCaption = document.createTextNode(element.name); 					
			entryTypesList.appendChild(typeElement);
			entryTypesList.appendChild(typeCaption);
			
			arrayOfParameters.push(element.type + element.id);
			addEntryTypesList = true;
		}	
	if (addLedgerList == true){
		tdLedgersList.appendChild(ledgersList);
		trLedgers.appendChild(tdLedgersCaption);
		trLedgers.appendChild(tdLedgersList);
	}	
	
	if (addEntryTypesList == true){
		tdTypesList.appendChild(entryTypesList);
		trTypes.appendChild(tdTypesCaption);
		trTypes.appendChild(tdTypesList);
	}
	tableOfParameters.appendChild(trLedgers);
	tableOfParameters.appendChild(trTypes);
				
	}
		getEntriesButton = document.createElement('input');
		getEntriesButton.setAttribute('type', 'button');
		getEntriesButton.setAttribute('class', 'calendarButtonStyleClass');	
		getEntriesButton.setAttribute('value', 'Get entries');		
/*
	parentOfList.appendChild(ledgersList);
	parentOfList.appendChild(entryTypesList);
*/
	parentOfList.appendChild(tableOfParameters);
//	if(getEntriesButton)
//		parentOfList.appendChild(getEntriesButton);	
	addButtonBehaviour();
	
}

	function addButtonBehaviour(){
	    $$('input.calendarButtonStyleClass').each(
    	function(element) {
			element.onclick = function() {
				arrayOfCheckedParameters = new Array();
				for(var i = 0; i < arrayOfParameters.length; i++){					
					var element = document.getElementById(arrayOfParameters[i]);
					if (element.checked)
						arrayOfCheckedParameters.push(arrayOfParameters[i]);
				}

				groups_and_calendar_chooser_helper = new ChooserHelper();
				groups_and_calendar_chooser_helper.removeAllAdvancedProperties();
				for(var index=0; index<arrayOfCheckedParameters.length; index++) {
					groups_and_calendar_chooser_helper.addAdvancedProperty(arrayOfCheckedParameters[index].id, arrayOfCheckedParameters[index]);
				}
				CalService.setCheckedParameters(arrayOfCheckedParameters, displayEntries);	
			}
    	}
    	);
    	$$('input.calendarCheckbox').each(
	    	function(element) {
	    		element.onclick = function(){
	    			if(element.checked == false){
						arrayOfCheckedCalendarParameters.remove(element.id);
	    				groups_and_calendar_chooser_helper.removeAdvancedProperty(element.id);
//	    				groups_and_calendar_chooser_helper.removeAdvancedProperty('calendarAttributes');
//	    				groups_and_calendar_chooser_helper.addAdvancedProperty('calendarAttributes', arrayOfCheckedCalendarParameters);	    				
	    			}
	    			else{
						arrayOfCheckedCalendarParameters.push(element.id);
	    				groups_and_calendar_chooser_helper.addAdvancedProperty(element.id, element.id);
//	    				groups_and_calendar_chooser_helper.removeAdvancedProperty('calendarAttributes');
//	    				groups_and_calendar_chooser_helper.addAdvancedProperty('calendarAttributes', arrayOfCheckedCalendarParameters);	    				
	    			}
	    		}
	    	}
	    );
/*    	
    	var groupsAndCalendarsLayer = document.getElementById('groupsAndCalendarsLayerId').parentNode;
    	var buttonId = null;
    	for(var index=0; index<groupsAndCalendarsLayer.getElementsByTagName('input').length; index++) {
    		var element = groupsAndCalendarsLayer.getElementsByTagName('input')[index];
    		if (element.name.toString() == 'save'){
	    		buttonId = element.id;
    		}
    	}
    	
    	$(buttonId).addEvent('click', function(e) {
			arrayOfCheckedParameters = new Array();
			for(var i = 0; i < arrayOfParameters.length; i++){					
				var element = document.getElementById(arrayOfParameters[i]);
				if (element.checked)
					arrayOfCheckedParameters.push(arrayOfParameters[i]);
			}

			groups_and_calendar_chooser_helper = new ChooserHelper();
			groups_and_calendar_chooser_helper.removeAllAdvancedProperties();
			for(var index=0; index<arrayOfCheckedParameters.length; index++) {
				groups_and_calendar_chooser_helper.addAdvancedProperty(arrayOfCheckedParameters[index].id, arrayOfCheckedParameters[index]);
			}
			ScheduleSession.setCheckedParameters(scheduleId, arrayOfCheckedParameters, displayEntries);
    	});
  */  	
	};
	
	function addBehaviour(){
	    $$('li.groupNode').each(
    	function(element) {
			element.onclick = function() {
				CalService.getCalendarParameters(element.id, displayCalendarAttributes);
			}
    	}
    	);
	}	
/*	
			this.entryName = entryName;
		this.entryDate = entryDate;
		this.entryEndDate = entryEndDate;
		this.repeat = repeat;
		this.entryTypeName = entryTypeName;
*/ 
	
	function getScheduleButtons(){
		var scheduleNextButton = document.createElement('input');
		scheduleNextButton.setAttribute('type', 'button');
		scheduleNextButton.setAttribute('class', 'scheduleNextButtonStyleClass');	
		scheduleNextButton.setAttribute('value', 'Next');
		
//		scheduleNextButton.setAttribute('onclick', 'ScheduleSession.getNext(scheduleId, displayEntries)');	
		
		var schedulePreviousButton = document.createElement('input');
		schedulePreviousButton.setAttribute('type', 'button');
		schedulePreviousButton.setAttribute('class', 'schedulePreviousButtonStyleClass');	
		schedulePreviousButton.setAttribute('value', 'Previous');	
		
//		schedulePreviousButton.setAttribute('onclick', 'ScheduleSession.getPrevious(scheduleId, displayEntries)');	
		
		var scheduleDayButton = document.createElement('input');
		scheduleDayButton.setAttribute('type', 'button');
		scheduleDayButton.setAttribute('class', 'scheduleDayButtonStyleClass');	
		scheduleDayButton.setAttribute('value', 'Day');	
		
//		scheduleDayButton.setAttribute('onclick', 'ScheduleSession.changeModeToDay(scheduleId, displayEntries)');	
		
		var scheduleWeekButton = document.createElement('input');
		scheduleWeekButton.setAttribute('type', 'button');
		scheduleWeekButton.setAttribute('class', 'scheduleWeekButtonStyleClass');	
		scheduleWeekButton.setAttribute('value', 'Week');	
		
//		scheduleWeekButton.setAttribute('onclick', 'ScheduleSession.changeModeToWeek(scheduleId, displayEntries)');	
		
		var scheduleWorkweekButton = document.createElement('input');
		scheduleWorkweekButton.setAttribute('type', 'button');
		scheduleWorkweekButton.setAttribute('class', 'scheduleWorkweekButtonStyleClass');	
		scheduleWorkweekButton.setAttribute('value', 'Workweek');	
		
//		scheduleWorkweekButton.setAttribute('onclick', 'ScheduleSession.changeModeToWorkweek(scheduleId, displayEntries)');	
		
		var scheduleMonthButton = document.createElement('input');
		scheduleMonthButton.setAttribute('type', 'button');
		scheduleMonthButton.setAttribute('class', 'scheduleMonthButtonStyleClass');	
		scheduleMonthButton.setAttribute('value', 'Month');	

//		scheduleMonthButton.setAttribute('onclick', 'ScheduleSession.changeModeToMonth(scheduleId, displayEntries)');	

		var scheduleButtonsLayer = document.createElement('div');
		if(showMenu == true){
			if (showPreviousAndNext == true){
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

	function getNext(){
		if(showEntriesAsList){
//			ScheduleSession.getNextAsList(scheduleId, displayEntriesAsList);
			ScheduleSession.switchToNextAndGetListOfEntries(scheduleId, displayEntriesAsList);
		}	
		else{
			ScheduleSession.switchToNextAndGetScheduleDOM(scheduleId, displayEntries);
		}
	}
	function getPrevious(){
		if(showEntriesAsList){
			ScheduleSession.switchToPreviousAndGetListOfEntries(scheduleId, displayEntriesAsList);
		}	
		else{
			ScheduleSession.switchToPreviousAndGetScheduleDOM(scheduleId, displayEntries);
		}		
	}
	function changeModeToDay(){
		if(showEntriesAsList){
			ScheduleSession.changeModeToDayAndGetListOfEntries(scheduleId, displayEntriesAsList);
		}	
		else{
			ScheduleSession.changeModeToDayAndGetScheduleDOM(scheduleId, displayEntries);
		}		
	}
	function changeModeToWorkweek(){
		if(showEntriesAsList){
			ScheduleSession.changeModeToWorkweekAndGetListOfEntries(scheduleId, displayEntriesAsList);
		}	
		else{
			ScheduleSession.changeModeToWorkweekAndGetScheduleDOM(scheduleId, displayEntries);
		}		
		
	}
	function changeModeToWeek(){
		if(showEntriesAsList){
			ScheduleSession.changeModeToWeekAndGetListOfEntries(scheduleId, displayEntriesAsList);
		}	
		else{
			ScheduleSession.changeModeToWeekAndGetScheduleDOM(scheduleId, displayEntries);
		}		
		
	}
	function changeModeToMonth(){
		if(showEntriesAsList){
			ScheduleSession.changeModeToMonthAndGetListOfEntries(scheduleId, displayEntriesAsList);
		}	
		else{
			ScheduleSession.changeModeToMonthAndGetScheduleDOM(scheduleId, displayEntries);
		}		
		
	}
	
	function displayEntries(result){			//inserting schedule DOM object

		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		if(scheduleEntries){
			removeChildren(scheduleEntries);  //remove previous schedule DOM object
		}
		else{
			scheduleEntries = document.createElement('div');
			scheduleEntries.setAttribute('id',scheduleEntryTableId);
//			scheduleEntries.appendChild(result);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons());		
			setBehaviourToScheduleButtons();
		}
		insertNodesToContainer(result, scheduleEntries);
		closeLoadingMessage();		
	}
	
	function displayEntriesAsList(entries){	
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		if(scheduleEntries){
			removeChildren(scheduleEntries);  //remove previous schedule DOM object
		}
		else{
			scheduleEntries = document.createElement('div');
			scheduleEntries.setAttribute('id',scheduleEntryTableId);
//			scheduleEntries.appendChild(result);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons());
			setBehaviourToScheduleButtons();
		}
		if (entries.length == 0){
			var txtEntries=document.createTextNode(noEntries);
			scheduleEntries.appendChild(txtEntries);
			return;
		}
//		scheduleEntries = document.createElement('div');
//		scheduleEntries.setAttribute('id',scheduleEntryTableId);
		
		var scheduleTable = document.createElement('table');
		scheduleTable.setAttribute('class', entryListTableStyleClass);
		
		
		var tr=document.createElement('tr');
//		tr.setAttribute('class',entryListTableCaptionStyleClass)
		var tdNumber=document.createElement('td');
		tdNumber.setAttribute('class',entryListTableCaptionStyleClass);
		var tdName=document.createElement('td');
		tdName.setAttribute('class',entryListTableCaptionStyleClass);		
		var tdDate=document.createElement('td');		
		tdDate.setAttribute('class',entryListTableCaptionStyleClass);		
		var tdEndDate=document.createElement('td');		
		tdEndDate.setAttribute('class',entryListTableCaptionStyleClass);		
		var tdType=document.createElement('td');	
		tdType.setAttribute('class',entryListTableCaptionStyleClass);
		
		var txtNumber=document.createTextNode(entryNumber);
		var txtName=document.createTextNode(entryName);
		var txtDate=document.createTextNode(entryDate);		
		var txtEndDate=document.createTextNode(entryEndDate);		
		var txtType=document.createTextNode(entryType);	
							
		tdNumber.appendChild(txtNumber);
		tdName.appendChild(txtName);
		tdDate.appendChild(txtDate);
		tdEndDate.appendChild(txtEndDate);
		tdType.appendChild(txtType);		
				  	
		tr.appendChild(tdNumber);
		tr.appendChild(tdName);
		tr.appendChild(tdDate);
		tr.appendChild(tdEndDate);
		tr.appendChild(tdType);		
		scheduleTable.appendChild(tr);
		
		for(var index=0; index<entries.length; index++) {
			
			var tr=document.createElement('tr');
			var tdNumber=document.createElement('td');
			tdNumber.setAttribute('class',entryListTableStyleClass);
			var tdName=document.createElement('td');
			tdName.setAttribute('class',entryListTableStyleClass);
			var tdDate=document.createElement('td');		
			tdDate.setAttribute('class',entryListTableStyleClass);
			var tdEndDate=document.createElement('td');		
			tdEndDate.setAttribute('class',entryListTableStyleClass);
			var tdType=document.createElement('td');	
			tdType.setAttribute('class',entryListTableStyleClass);
			
			var txtNumber=document.createTextNode(index+1);
			var txtName=document.createTextNode(entries[index].entryName);
			var txtDate=document.createTextNode(entries[index].entryDate.substring(0,16));		
			var txtEndDate=document.createTextNode(entries[index].entryEndDate.substring(0,16));		
			var txtType=document.createTextNode(entries[index].entryTypeName);	
			
			tdNumber.appendChild(txtNumber);
			tdName.appendChild(txtName);
			tdDate.appendChild(txtDate);
			tdEndDate.appendChild(txtEndDate);
			tdType.appendChild(txtType);		
					  	
			tr.appendChild(tdNumber);
			tr.appendChild(tdName);
			tr.appendChild(tdDate);
			tr.appendChild(tdEndDate);
			tr.appendChild(tdType);		
			scheduleTable.appendChild(tr);
		}

		scheduleEntries.appendChild(scheduleTable);
		closeLoadingMessage();
//		scheduleEntries.appendChild(scheduleTable);
		
		
/*		
		  	td.appendChild(tdText);  					// - put the text node in the table cell
		  	tr.appendChild(td); 						// - put the cell into the row
		  	tempTable.appendChild(tr); 	
		  	rootUl.appendChild(tempTable);		
*/		
	}
/*	
	function getEmptySchedule(){
		ScheduleSession.setCheckedParameters(new Array(), displayEntries);
	}
*/
/*
	function getSchedule(id, array){
		scheduleId = id;
		ScheduleSession.setCheckedParameters(id, array, displayEntries);
	}
*/	
	function createEmptySchedule(result){
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		scheduleEntries = document.createElement('div');
		scheduleEntries.setAttribute('id',scheduleEntryTableId);
		scheduleLayer.appendChild(scheduleEntries);
		scheduleLayer.appendChild(getScheduleButtons());
		insertNodesToContainer(result, scheduleEntries);

	}
	

	function setBehaviourToScheduleButtons(){
		$$('input.scheduleNextButtonStyleClass').each(
			function(element) {
				element.onclick = function() {
					getNext();					
				}
//				ScheduleSession.getNext(displayEntries);
	    	}
	    );		
		$$('input.schedulePreviousButtonStyleClass').each(
			function(element) {
				element.onclick = function() {				
					getPrevious();
				}
//				ScheduleSession.getPrevious(displayEntries);
	    	}
	    );		
	    $$('input.scheduleDayButtonStyleClass').each(
			function(element) {
				element.onclick = function() {
//				ScheduleSession.changeModeToDay(displayEntries);
					changeModeToDay();
				}
	    	}
	    );		
	    $$('input.scheduleWorkweekButtonStyleClass').each(
			function(element) {
//				ScheduleSession.changeModeToWorkweek(displayEntries);
				element.onclick = function() {
					changeModeToWorkweek();	
				}
	    	}
	    );		
	    $$('input.scheduleWeekButtonStyleClass').each(
			function(element) {
//				ScheduleSession.changeModeToWeek(displayEntries);
				element.onclick = function() {
					changeModeToWeek();
				}
	    	}
	    );		
	    $$('input.scheduleMonthButtonStyleClass').each(
			function(element) {
				element.onclick = function() {				
//				ScheduleSession.changeModeToMonth(displayEntries);
					changeModeToMonth();	
				}
	    	}
	    );		
	}

	function getCalendarProperties(){
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getCalendarProperties(scheduleId, {
			callback: function(result) {
				calendarProperties = result;
				getEntries(result.remoteMode, result.server, result.login, result.password, result.calendarAttributes);
			}
		});
//		getEntries(calendarProperties.remoteMode, calendarProperties.server, calendarProperties.login, result.password, result.calendarAttributes);
	}
/*	
	function getGroupsWithValues(loadingMsg, server, login, password, id, canNotConnectMsg, failedLoginMsg, noGroupsMsg, needsDecode, selectedGroups, styleClass) {
	showLoadingMessage(loadingMsg);
	if (needsDecode) {
		password = decode64(password);
	}
	GroupService.canUseRemoteServer(server, {
		callback: function(result) {
			canUseRemoteCallback(result, server, login, password, id, canNotConnectMsg, failedLoginMsg, noGroupsMsg, selectedGroups, styleClass);
		}
	});
}
*/	
	
//	function getCalendarParameters(groupId){
	function prepareDwrForGettingCalendarParameters(groupId){
		
		groups_and_calendar_chooser_helper = new ChooserHelper();
		groups_and_calendar_chooser_helper.removeAllAdvancedProperties();
		groups_and_calendar_chooser_helper.addAdvancedProperty('server', SERVER);
		groups_and_calendar_chooser_helper.addAdvancedProperty('login', LOGIN);
		groups_and_calendar_chooser_helper.addAdvancedProperty('password', PASSWORD);		
		
		var connection = $('connectionData');
		if (connection == null) {
			return;
		}
		if (connection.style.display == 'none'){
			groups_and_calendar_chooser_helper.addAdvancedProperty('isRemoteMode', 'false');
			getLocalCalendarParameters(groupId);
		}
		else{
			groups_and_calendar_chooser_helper.addAdvancedProperty('isRemoteMode', 'true');			
			canUseRemoteCalendar(groupId);
		}

		
	}
	
	function getLocalCalendarParameters(groupId){
		showLoadingMessage(loadingMsg);
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getCalendarParameters(groupId, displayCalendarAttributes);
	}
		
	function canUseRemoteCalendar(groupId){
		showLoadingMessage(loadingMsg);
		CalService.canUseRemoteServer(SERVER, {
			callback: function(result) {
				canUseRemoteCalendarCallback(result, groupId);
			}
		});
	}
	function canUseRemoteCalendarCallback(canUse, groupUniqueId){
		if(canUse){
			prepareDwr(CalService, SERVER + DEFAULT_DWR_PATH);
		
			//	Getting info from remote server
			CalService.getRemoteCalendarParameters(groupUniqueId, LOGIN, PASSWORD, displayCalendarAttributes);		
		}
		else{
			//	Cannot use remote server
			closeLoadingMessage();
			alert(serverErrorMessage + ' ' + server);
			return false;			
		}
	}
	
	function getEntries(isRemoteMode, server, login, password, calendarAttributes){
//	function getEntries(){
/*
		var isRemoteMode = calendarProperties.remoteMode;
		var server = calendarProperties.server;		
		var login = calendarProperties.login;
		var password = calendarProperties.password;
		var calendarAttributes = calendarProperties.calendarAttributes;
*/		
		if(isRemoteMode == true){
			showLoadingMessage(loadingMsg);
			CalService.canUseRemoteServer(server, {
				callback: function(result) {
					getEntriesCallback(result, server, login, password, calendarAttributes);
				}
			});
		}
		else{
			CalService.getEntries(calendarAttributes, addEntriesToListOrSchedule);		
		}
	}
	
/*
	function getEntries(isRemoteMode, calendarAttributes){
		if(isRemoteMode == 'true'){
			showLoadingMessage(loadingMsg);
			CalService.getCalendarConnectionProperties(server, {
				callback: function(result) {
					getEntriesCallback(result, server, login, password, calendarAttributes);
				}
			});
		}
		else{
			CalService.getEntries(calendarAttributes, getScheduleWithEntries);		
		}
	}
*/
	function getCalendarConnectionPropertiesCallback(){
		CalService.canUseRemoteServer(server, {
			callback: function(result) {
				getEntriesCallback(result, server, login, password, calendarAttributes);
			}
		});		
	}
	
	function getEntriesCallback(canUse, server, login, password, calendarAttributes){
		if(canUse){
			prepareDwr(CalService, server + getDefaultDwrPath());
			CalService.getRemoteEntries(calendarAttributes, login, password, addEntriesToListOrSchedule);
		}
		else{
			closeLoadingMessage();
			alert(serverErrorMessage + ' ' + server);
			return false;
		}
	}
/*	
	function emtpyCallback(result){
		
	}
*/	
	function addEntriesCallback(result){
//		entriesPackageSize = 3;
		packageIndex += entriesPackageSize;
		packageEndIndex += entriesPackageSize;
		packageOfEntries = new Array();
		var reachedEnd = false;
		for(var index=packageIndex; index<packageEndIndex; index++) {
			if ((entriesToCalendar[index] == null) || (entriesToCalendar[index] == undefined)){
				reachedEnd = true;
				break;
			}
			packageOfEntries.push(entriesToCalendar[index]);
		}
//		if (packageOfEntries.length != 0){
			ScheduleSession.addEntries(packageOfEntries, scheduleId, false, function(result){
				if(reachedEnd == false){
					addEntriesCallback(result);
				}
				else{
					if(showEntriesAsList){
//						displayEntriesAsList(entriesToCalendar);
						ScheduleSession.getListOfEntries(scheduleId, displayEntriesAsList);
					}
					else{
//						ScheduleSession.getScheduleDOM(entries, scheduleId, displayEntries);
						ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
					}
				}
			});
//		}	
	}
	
	function addEntriesToListOrSchedule(entries){
		entriesToCalendar = entries;
		
		var packageOfEntries = new Array();
		var limitOfPackage = null;    
		if(entries.length > entriesPackageSize){
			limitOfPackage = entriesPackageSize;
		}
		else{
			limitOfPackage = entries.length;
		}
		for(var index=0; index<limitOfPackage; index++) {
			packageOfEntries.push(entries[index]);			
		}
		ScheduleSession.addEntries(packageOfEntries, scheduleId, true, addEntriesCallback);		
	}
/*		
		if(showEntriesAsList){

		}
		else{
			prepareDwr(ScheduleSession, getDefaultDwrPath());
			var packageOfEntries = new Array();
			var maxPackageSize = 3;
			for(var index=0; index<entries.length; index++) {
				if(index % maxPackageSize == 0){
					ScheduleSession.addEntries(packageOfEntries, scheduleId, emptyCallback);
					packageOfEntries = new Array();
				}
			}			
			ScheduleSession.addEntries(packageOfEntries, scheduleId, emptyCallback);
			DWREngine.setOrdered(false);
			ScheduleSession.getScheduleDOM(entries, scheduleId, displayEntries);
		}
*/ 
//	}

//	function testDisplayEntriesAsList(result){
//	}
		
//		prepareDwr(ScheduleSession, server + DEFAULT_DWR_PATH);
		
//		showLoadingMessage(loadingMsg);
/*		
		if(SERVER != null){
			prepareDwr(ScheduleSession, SERVER + getDefaultDwrPath());
			getRemoteCalendarParameters(groupId, login, password);
		}
		else{
			prepareDwr(ScheduleSession, getDefaultDwrPath());
			getLocalCalendarParameters(groupId);			
		}
		*/ 
/*	
	function saveProperties(){
		groups_and_calendar_chooser_helper = new ChooserHelper();
		groups_and_calendar_chooser_helper.removeAllAdvancedProperties();
		for(var index=0; index<arrayOfParameters.length; index++) {
			groups_and_calendar_chooser_helper.addAdvancedProperty(arrayOfParameters[index].id, arrayOfParameters[index].id);
		}
	}
*/
/*
		if (arrayOfEntryIds)
			clearPreviousEntries();
		arrayOfEntryIds = new Array();
		for(var i = 0; i < result.length; i++){
			var entry = result[i];
			addEntryToCalendar(entry.entryName, entry.entryDate, entry.entryEndDate, entry.entryTime, entry.entryEndTime);
		}
*/

/*	
	function addEntryToCalendar(entryName, entryDate, entryEndDate, entryTime, entryEndTime){
		var entryCell = document.getElementById('_id0_body_'+entryDate);	
		if (entryCell){
			arrayOfEntryIds.push('_id0_body_'+entryDate);
			var entryTable = entryCell.getElementsByTagName('table')[0];
			var trElement =  document.createElement('tr');
			var tdElement = document.createElement('td');
			var entryName = document.createTextNode(entryName+' '+entryTime+'-'+entryEndTime);
			tdElement.appendChild(entryName);
			trElement.appendChild(tdElement);
			entryTable.appendChild(trElement);
		}
	}
	
	function clearPreviousEntries(){
		for(var i = 0; i < arrayOfEntryIds.length; i++){
			var entryDiv = document.getElementById(arrayOfEntryIds[i])
			var entryTable = entryDiv.childNodes[0];

			for (var j = 0; j < entryTable.childNodes.length; j++){
				entryTable.removeChild(entryTable.childNodes[j]);
			}

//			var entryParent = entry.parentNode;
		}
	}
*/