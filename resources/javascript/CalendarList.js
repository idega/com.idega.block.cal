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

var entryIdPrefix = '';

var loadingMsg = 'Loading...';

var showEntriesAsList = false;
var hideMenu = false;
var hidePreviousAndNext = false;
var showTime = false;

var entryNumber = 'Number';
var entryName = 'Name';
var entryBeginDate = 'Begin date';
var entryEndDate = 'End date';
var entryType = 'Type';
var entryTime = 'Time';
var entryDate = 'Date';
var entryDescription = 'Description';

var entryListTableStyleClass = 'entryList';
var entryListCaptionStyleClass = 'entryListCaption';
//var entryListRowStyleClass = 'entryListRow';
var entryListEvenRowStyleClass = 'entryListEvenRow';
var entryListOddRowStyleClass = 'entryListOddRow';
var entryListElementStyleClass = 'entryListElement';
var entryInfoStyleClass = 'entryInfo';
//var entryInfoRow = 'entryInfoRow';
//var entryInfoCell = 'entryInfoCell';
var entryListElementTimeStyleClass = 'entryListElementTime';
var entryInfoNameStyleClass = 'entryInfoName';
var entryInfoDateStyleClass = 'entryInfoDate';
var entryInfoTimeStyleClass = 'entryInfoTime';
var entryInfoTypeStyleClass = 'entryInfoType';
var entryInfoDescriptionStyleClass = 'entryInfoDescription';
var entryInScheduleStyleClass = null;

var calendarProperties = null;

var entriesToCalendar = null;
var entriesToList = null;
var entriesPackageSize = 5;
var packageIndex = 0;
var packageEndIndex = entriesPackageSize;
var noEntries = 'There are no entries to display';
var entryInfo = document.createElement('div');

function displayCalendarAttributes(calendars){
	closeLoadingMessage();
	arrayOfParameters = new Array();	
	var parentOfList = document.getElementById('calendar_list_container_id');
	removeChildren(parentOfList);
	var ledgersList = document.createElement('div');
	var entryTypesList = document.createElement('div');
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
	parentOfList.appendChild(tableOfParameters);
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
	    			}
	    			else{
						arrayOfCheckedCalendarParameters.push(element.id);
	    				groups_and_calendar_chooser_helper.addAdvancedProperty(element.id, element.id);
	    			}
	    		}
	    	}
	    );
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
	
	function getScheduleButtons(){
		var scheduleNextButton = document.createElement('input');
		scheduleNextButton.setAttribute('type', 'button');
		scheduleNextButton.setAttribute('class', 'scheduleNextButtonStyleClass');	
		scheduleNextButton.setAttribute('value', 'Next');
		
		var schedulePreviousButton = document.createElement('input');
		schedulePreviousButton.setAttribute('type', 'button');
		schedulePreviousButton.setAttribute('class', 'schedulePreviousButtonStyleClass');	
		schedulePreviousButton.setAttribute('value', 'Previous');	
		
		var scheduleDayButton = document.createElement('input');
		scheduleDayButton.setAttribute('type', 'button');
		scheduleDayButton.setAttribute('class', 'scheduleDayButtonStyleClass');	
		scheduleDayButton.setAttribute('value', 'Day');	
		
		var scheduleWeekButton = document.createElement('input');
		scheduleWeekButton.setAttribute('type', 'button');
		scheduleWeekButton.setAttribute('class', 'scheduleWeekButtonStyleClass');	
		scheduleWeekButton.setAttribute('value', 'Week');	
		
		var scheduleWorkweekButton = document.createElement('input');
		scheduleWorkweekButton.setAttribute('type', 'button');
		scheduleWorkweekButton.setAttribute('class', 'scheduleWorkweekButtonStyleClass');	
		scheduleWorkweekButton.setAttribute('value', 'Workweek');	
		
		var scheduleMonthButton = document.createElement('input');
		scheduleMonthButton.setAttribute('type', 'button');
		scheduleMonthButton.setAttribute('class', 'scheduleMonthButtonStyleClass');	
		scheduleMonthButton.setAttribute('value', 'Month');	

		var scheduleButtonsLayer = document.createElement('div');
		if(hideMenu == false){
			if (hidePreviousAndNext == false){
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
		ScheduleSession.switchToNextAndGetListOfEntries(scheduleId, function(result){
			if(showEntriesAsList){			
				displayEntriesAsList(result);
			}
			else{
				entriesToList = result;
				ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
			}
		});

	}
	function getPrevious(){	
		ScheduleSession.switchToPreviousAndGetListOfEntries(scheduleId, function(result){
			if(showEntriesAsList){			
				displayEntriesAsList(result);
			}
			else{
				entriesToList = result;
				ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
			}
		});
	}
	
	function changeModeToDay(){
		ScheduleSession.changeModeToDayAndGetListOfEntries(scheduleId, function(result){
			if(showEntriesAsList){			
				displayEntriesAsList(result);
			}
			else{
				entriesToList = result;
				ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
			}
		});	
	}

	function changeModeToWorkweek(){
		ScheduleSession.changeModeToWorkweekAndGetListOfEntries(scheduleId, function(result){
			if(showEntriesAsList){			
				displayEntriesAsList(result);
			}
			else{
				entriesToList = result;
				ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
			}
		});	
	}	
	
	function changeModeToWeek(){
		ScheduleSession.changeModeToWeekAndGetListOfEntries(scheduleId, function(result){
			if(showEntriesAsList){			
				displayEntriesAsList(result);
			}
			else{
				entriesToList = result;
				ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
			}
		});	
	}		

	function changeModeToMonth(){
		ScheduleSession.changeModeToMonthAndGetListOfEntries(scheduleId, function(result){
			if(showEntriesAsList){			
				displayEntriesAsList(result);
			}
			else{
				entriesToList = result;
				ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
			}
		});	
	}		
/*	
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
*/	
	function displayEntries(result){			//inserting schedule DOM object

//		entriesToList = entries;
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		if(scheduleEntries){
			removeChildren(scheduleEntries);  //remove previous schedule DOM object
		}
		else{
			scheduleEntries = document.createElement('div');
			scheduleEntries.setAttribute('id',scheduleEntryTableId);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons());		
			setBehaviourToScheduleButtons();
		}
		insertNodesToContainer(result, scheduleEntries);
		setBehaviourOnScheduleEntries();
		closeLoadingMessage();		
	}
	
	function displayEntriesAsList(entries){	
		entriesToList = entries;
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		if(scheduleEntries){
			removeChildren(scheduleEntries);  //remove previous schedule DOM object
		}
		else{
			scheduleEntries = document.createElement('div');
			scheduleEntries.setAttribute('id',scheduleEntryTableId);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons());
			setBehaviourToScheduleButtons();
		}
		if (entries.length == 0){
			var txtEntries=document.createTextNode(noEntries);
			scheduleEntries.appendChild(txtEntries);
			return;
		}
		
//		var scheduleTable = document.createElement('table');
		var scheduleList = document.createElement('div');
		scheduleList.setAttribute('class', entryListTableStyleClass);
		
		
//		var tr=document.createElement('tr');
//		tr.setAttribute('class',entryListTableCaptionStyleClass);	

		var listCaptionRow = document.createElement('div');
		listCaptionRow.setAttribute('class', entryListCaptionStyleClass);
		
/*		
		var tdNumber=document.createElement('td');
		tdNumber.setAttribute('class',entryListTableCaptionStyleClass);
*/		
		var nameOfEntry=document.createElement('div');
		nameOfEntry.setAttribute('class',entryListElementStyleClass);		
		var dateOfEntry=document.createElement('div');
		dateOfEntry.setAttribute('class',entryListElementStyleClass);		
//		var endDateOfEntry=document.createElement('div');
//		endDateOfEntry.setAttribute('class',entryListElementStyleClass);		
//		var typeOfEntry=document.createElement('div');	
//		typeOfEntry.setAttribute('class',entryListElementStyleClass);
		var timeOfEntry=document.createElement('div');
		timeOfEntry.setAttribute('class',entryListElementStyleClass);
		
//		var txtNumber=document.createTextNode(entryNumber);
		var txtName=document.createTextNode(entryName);
		var txtDate=document.createTextNode(entryDate);		
//		var txtEndDate=document.createTextNode(entryEndDate);		
//		var txtType=document.createTextNode(entryType);	
		var txtTime=document.createTextNode(entryTime);	
							
//		tdNumber.appendChild(txtNumber);
		nameOfEntry.appendChild(txtName);
		dateOfEntry.appendChild(txtDate);
		timeOfEntry.appendChild(txtTime);		
//		endDateOfEntry.appendChild(txtEndDate);
//		typeOfEntry.appendChild(txtType);		
				  	
//		tr.appendChild(tdNumber);
/*
		listCaptionRow.appendChild(nameOfEntry);
		listCaptionRow.appendChild(dateOfEntry);
		listCaptionRow.appendChild(endDateOfEntry);
		listCaptionRow.appendChild(typeOfEntry);		
*/
		listCaptionRow.appendChild(dateOfEntry);
		listCaptionRow.appendChild(timeOfEntry);
		listCaptionRow.appendChild(nameOfEntry);


		scheduleList.appendChild(listCaptionRow);
		testTable = listCaptionRow;
		for(var index=0; index<entries.length; index++) {
			
			var listRow=document.createElement('div');
			if(index % 2 == 0){
				listRow.setAttribute('class', entryListEvenRowStyleClass);
			}
			else{
				listRow.setAttribute('class', entryListOddRowStyleClass);				
			}
			listRow.setAttribute('id', entryIdPrefix+index);
/*			
			var tdNumber=document.createElement('td');
			tdNumber.setAttribute('class',entryListTableStyleClass);
*/
			var nameOfEntry=document.createElement('div');
			nameOfEntry.setAttribute('class',entryListElementStyleClass);
			var dateOfEntry=document.createElement('div');		
			dateOfEntry.setAttribute('class',entryListElementStyleClass);
			var timeOfEntry=document.createElement('div');	
			timeOfEntry.setAttribute('class',entryListElementTimeStyleClass);
			if(showTime){
				timeOfEntry.style.display = 'inline';
			}
			
//			var endDateOfEntry=document.createElement('div');	
//			endDateOfEntry.setAttribute('class',entryListElementStyleClass);
//			var typeOfEntry=document.createElement('div');	
//			typeOfEntry.setAttribute('class',entryListElementStyleClass);
			
//			var txtNumber=document.createTextNode(index+1);
			var txtName=document.createTextNode(entries[index].entryName);
			var txtDate=document.createTextNode(entries[index].entryDate.substring(0,10));		
			var txtTime=document.createTextNode(entries[index].entryDate.substring(11,16)+'-'+
				entries[index].entryEndDate.substring(11,16));	
//			var txtEndDate=document.createTextNode(entries[index].entryEndDate.substring(0,16));		
//			var txtType=document.createTextNode(entries[index].entryTypeName);	
			
//			tdNumber.appendChild(txtNumber);
			nameOfEntry.appendChild(txtName);
			dateOfEntry.appendChild(txtDate);
			timeOfEntry.appendChild(txtTime);		
//			endDateOfEntry.appendChild(txtEndDate);
//			typeOfEntry.appendChild(txtType);		
					  	
//			tr.appendChild(tdNumber);
			listRow.appendChild(dateOfEntry);
			listRow.appendChild(timeOfEntry);
			listRow.appendChild(nameOfEntry);
//			listRow.appendChild(endDateOfEntry);
//			listRow.appendChild(typeOfEntry);		
			scheduleList.appendChild(listRow);
		}

		scheduleEntries.appendChild(scheduleList);
		setBehaviourOnListRows();
		closeLoadingMessage();
	}
	function createInfoTable(entryNameValue, entryDateValue, entryTimeValue, entryEndTimeValue, entryTypeValue, entryDescriptionValue){
		var txtName=document.createTextNode(entryNameValue);
		var txtDate=document.createTextNode(entryDateValue);		
		var txtTime=document.createTextNode(entryTimeValue+'-'+entryEndTimeValue);	
		var txtType=document.createTextNode(entryTypeValue);		
		var txtDescription=document.createTextNode(entryDescriptionValue);		
/*
		var txtNameCaption=document.createTextNode(entryName+':');
		var txtDateCaption=document.createTextNode(entryDate+':');		
		var txtTimeCaption=document.createTextNode(entryTime+':');	
		var txtTypeCaption=document.createTextNode(entryType+':');	
		var txtDescriptionCaption=document.createTextNode(entryDescription+':');	
*/
		
		var nameOfEntry=document.createElement('div');
		nameOfEntry.appendChild(txtName);
		nameOfEntry.setAttribute('class', entryInfoNameStyleClass);
		var dateOfEntry=document.createElement('div');			
		dateOfEntry.appendChild(txtDate);			
		dateOfEntry.setAttribute('class', entryInfoDateStyleClass);		
		var timeOfEntry=document.createElement('div');			
		timeOfEntry.appendChild(txtTime);			
		timeOfEntry.setAttribute('class', entryInfoTimeStyleClass);
		var typeOfEntry=document.createElement('div');			
		typeOfEntry.appendChild(txtType);			
		typeOfEntry.setAttribute('class', entryInfoTypeStyleClass);
		var descriptionOfEntry=document.createElement('div');			
		descriptionOfEntry.appendChild(txtDescription);			
		descriptionOfEntry.setAttribute('class', entryInfoDescriptionStyleClass);
/*
		var nameCaption=document.createElement('div');
		nameCaption.appendChild(txtNameCaption);
		nameCaption.setAttribute('class', entryInfoCell);
		var dateCaption=document.createElement('div');			
		dateCaption.appendChild(txtDateCaption);			
		dateCaption.setAttribute('class', entryInfoCell);
		var timeCaption=document.createElement('div');			
		timeCaption.appendChild(txtTimeCaption);			
		timeCaption.setAttribute('class', entryInfoCell);
		var typeCaption=document.createElement('div');			
		typeCaption.appendChild(txtTypeCaption);			
		typeCaption.setAttribute('class', entryInfoCell);
		var descriptionCaption=document.createElement('div');			
		descriptionCaption.appendChild(txtDescriptionCaption);			
		descriptionCaption.setAttribute('class', entryInfoCell);
*/		
/*
		var entryInfoNameRow = document.createElement('div');
		entryInfoNameRow.setAttribute('class', entryInfoRow);
		entryInfoNameRow.appendChild(nameCaption);
		entryInfoNameRow.appendChild(nameOfEntry);
		var entryInfoDateRow = document.createElement('div');
		entryInfoDateRow.setAttribute('class', entryInfoRow);
		entryInfoDateRow.appendChild(dateCaption);
		entryInfoDateRow.appendChild(dateOfEntry);
		var entryInfoTimeRow = document.createElement('div');
		entryInfoTimeRow.setAttribute('class', entryInfoRow);
		entryInfoTimeRow.appendChild(timeCaption);
		entryInfoTimeRow.appendChild(timeOfEntry);
		var entryInfoTypeRow = document.createElement('div');
		entryInfoTypeRow.setAttribute('class', entryInfoRow);
		entryInfoTypeRow.appendChild(typeCaption);
		entryInfoTypeRow.appendChild(typeOfEntry);
		
		var entryInfoDescriptionRow = document.createElement('div');
		entryInfoDescriptionRow.setAttribute('class', entryInfoRow);
		entryInfoDescriptionRow.appendChild(descriptionCaption);
		entryInfoDescriptionRow.appendChild(descriptionOfEntry);
*/								

		var entryInfoHeader = document.createElement('div');
		entryInfoHeader.setAttribute('class', 'entryInfoHeader');
		var entryInfoBody = document.createElement('div');
		entryInfoBody.setAttribute('class', 'entryInfoBody');
		
		entryInfoHeader.appendChild(dateOfEntry);
		entryInfoHeader.appendChild(timeOfEntry);
		entryInfoHeader.appendChild(nameOfEntry);
		entryInfoHeader.appendChild(typeOfEntry);
		entryInfoBody.appendChild(descriptionOfEntry);
		
		entryInfo.appendChild(entryInfoHeader);
		entryInfo.appendChild(entryInfoBody);
			
		document.body.appendChild(entryInfo);
		entryInfo.setAttribute('class', entryInfoStyleClass);
	}

	function setBehaviourOnListRows(){
		$$('div.'+entryListEvenRowStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );	
		$$('div.'+entryListOddRowStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );	
	}

	function setBehaviourOnScheduleEntries(){
		$$('a.'+entryInScheduleStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );		
		$$('a.entry').each(
			function(element) {
				element.onmouseover = function(e) {
					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );		

	}

	function displayEntryInfo(element, e){
		var entryElement = null;
		var idOfSelectedEntry = element.id.substring(entryIdPrefix.length);
		if (showEntriesAsList){
			entryElement = entriesToList[idOfSelectedEntry];
		}
		else{	
			for(var index=0; index<entriesToList.length; index++) {
				if(entriesToList[index].id == idOfSelectedEntry.toString()){
			
					entryElement = entriesToList[index];
					break;
				}				
			}
		}
		
		if(entryElement != null){
			createInfoTable(entryElement.entryName, 
				entryElement.entryDate.substring(0,10), 
				entryElement.entryDate.substring(11,16),
				entryElement.entryEndDate.substring(11,16),
				entryElement.entryTypeName,
				entryElement.entryDescription
			);
	
			var currentRow = null;
			var entryId = element.id;
			currentRow = document.getElementById(element.id);
						
			entryInfo.style.display = 'block';
			dragDrop_x = e.clientX/1 + document.body.scrollLeft;
			dragDrop_y = e.clientY/1 + document.documentElement.scrollTop;	
	
			dragDrop_x = 400;
						
			entryInfo.style.left = dragDrop_x + 'px';
			entryInfo.style.top = dragDrop_y + 'px';	
		}	
	}
/*	
	function drawInfoTable(e){
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');

			dragDrop_x = e.clientX/1 + document.body.scrollLeft;
			dragDrop_y = e.clientY/1 + document.documentElement.scrollTop;	

			dragDrop_x = 400;

			entryInfo.style.left = dragDrop_x + 'px';
			entryInfo.style.top = dragDrop_y + 'px';

			entryInfo.appendChild(testTable);
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
	    	}
	    );		
		$$('input.schedulePreviousButtonStyleClass').each(
			function(element) {
				element.onclick = function() {				
					getPrevious();
				}
	    	}
	    );		
	    $$('input.scheduleDayButtonStyleClass').each(
			function(element) {
				element.onclick = function() {
					changeModeToDay();
				}
	    	}
	    );		
	    $$('input.scheduleWorkweekButtonStyleClass').each(
			function(element) {
				element.onclick = function() {
					changeModeToWorkweek();	
				}
	    	}
	    );		
	    $$('input.scheduleWeekButtonStyleClass').each(
			function(element) {
				element.onclick = function() {
					changeModeToWeek();
				}
	    	}
	    );		
	    $$('input.scheduleMonthButtonStyleClass').each(
			function(element) {
				element.onclick = function() {				
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
	}
	
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
	function addEntriesCallback(result){
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
			ScheduleSession.addEntries(packageOfEntries, scheduleId, false, function(result){
				if(reachedEnd == false){
					addEntriesCallback(result);
				}
				else{
//					if(showEntriesAsList){
//						ScheduleSession.getListOfEntries(scheduleId, displayEntriesAsList);
						ScheduleSession.getListOfEntries(scheduleId, setEntries);
/*
					}
					else{
						ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
					}
*/
				}
			});
	}
	
	function setEntries(entries){
		entriesToList = entries;
		if(showEntriesAsList){
			displayEntriesAsList(entries);
		}
		else{
			ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
		}
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