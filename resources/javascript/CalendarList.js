var arrayOfParameters = null;
var arrayOfCheckedParameters = null;
var arrayOfEntryIds = null;
var scheduleEntryTableId = 'scheduleEntryTableId';
var scheduleButtonsId = 'scheduleButtonsId';
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
	arrayOfParameters = new Array();
	var parentOfList = document.getElementById(calendar_list_container_id);
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
			ledgerElement.setAttribute('class','callendarCheckbox');
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
			typeElement.setAttribute('class','callendarCheckbox');			
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


		var getEntriesButton = document.createElement('input');
		getEntriesButton.setAttribute('type', 'button');
		getEntriesButton.setAttribute('class', 'calendarButtonStyleClass');	
		getEntriesButton.setAttribute('value', 'Get entries');					
	}
/*
	parentOfList.appendChild(ledgersList);
	parentOfList.appendChild(entryTypesList);
*/
	parentOfList.appendChild(tableOfParameters);
	parentOfList.appendChild(getEntriesButton);	
	
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
				CalService.setCheckedParameters(arrayOfCheckedParameters, displayEntries);	
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
		
		scheduleNextButton.setAttribute('onclick', 'CalService.getNext(displayEntries)');	
		
		var schedulePreviousButton = document.createElement('input');
		schedulePreviousButton.setAttribute('type', 'button');
		schedulePreviousButton.setAttribute('class', 'schedulePreviousButtonStyleClass');	
		schedulePreviousButton.setAttribute('value', 'Previous');	
		
		schedulePreviousButton.setAttribute('onclick', 'CalService.getPrevious(displayEntries)');	
		
		var scheduleDayButton = document.createElement('input');
		scheduleDayButton.setAttribute('type', 'button');
		scheduleDayButton.setAttribute('class', 'scheduleDayButtonStyleClass');	
		scheduleDayButton.setAttribute('value', 'Day');	
		
		scheduleDayButton.setAttribute('onclick', 'CalService.changeModeToDay(displayEntries)');	
		
		var scheduleWeekButton = document.createElement('input');
		scheduleWeekButton.setAttribute('type', 'button');
		scheduleWeekButton.setAttribute('class', 'scheduleWeekButtonStyleClass');	
		scheduleWeekButton.setAttribute('value', 'Week');	
		
		scheduleWeekButton.setAttribute('onclick', 'CalService.changeModeToWeek(displayEntries)');	
		
		var scheduleWorkweekButton = document.createElement('input');
		scheduleWorkweekButton.setAttribute('type', 'button');
		scheduleWorkweekButton.setAttribute('class', 'scheduleWorkweekButtonStyleClass');	
		scheduleWorkweekButton.setAttribute('value', 'Workweek');	
		
		scheduleWorkweekButton.setAttribute('onclick', 'CalService.changeModeToWorkweek(displayEntries)');	
		
		var scheduleMonthButton = document.createElement('input');
		scheduleMonthButton.setAttribute('type', 'button');
		scheduleMonthButton.setAttribute('class', 'scheduleMonthButtonStyleClass');	
		scheduleMonthButton.setAttribute('value', 'Month');	

		scheduleMonthButton.setAttribute('onclick', 'CalService.changeModeToMonth(displayEntries)');	
		
		var scheduleButtonsLayer = document.createElement('div');
		scheduleButtonsLayer.appendChild(scheduleNextButton);
		scheduleButtonsLayer.appendChild(schedulePreviousButton);		
		scheduleButtonsLayer.appendChild(scheduleDayButton);		
		scheduleButtonsLayer.appendChild(scheduleWeekButton);
		scheduleButtonsLayer.appendChild(scheduleWorkweekButton);		
		scheduleButtonsLayer.appendChild(scheduleMonthButton);
		return  scheduleButtonsLayer;	
	}

	function displayEntries(result){
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		if(scheduleEntries){
			removeChildren(scheduleEntries);
		}
		else{
			scheduleEntries = document.createElement('div');
			scheduleEntries.setAttribute('id',scheduleEntryTableId);
//			scheduleEntries.appendChild(result);
			scheduleLayer.appendChild(scheduleEntries);
			scheduleLayer.appendChild(getScheduleButtons());
//			setBehaviourToScheduleButtons();
		}
		insertNodesToContainer(result, scheduleEntries);
	}
	
	function setBehaviourToScheduleButtons(){
		$$('input.scheduleNextButtonStyleClass').each(
			function(element) {
				CalService.getNext(displayEntries);
	    	}
	    );		
		$$('input.schedulePreviousButtonStyleClass').each(
			function(element) {
				CalService.getPrevious(displayEntries);
	    	}
	    );		
	    $$('input.scheduleDayButtonStyleClass').each(
			function(element) {
				CalService.changeModeToDay(displayEntries);
	    	}
	    );		
	    $$('input.scheduleWorkweekButtonStyleClass').each(
			function(element) {
				CalService.changeModeToWorkweek(displayEntries);
	    	}
	    );		
	    $$('input.scheduleWeekButtonStyleClass').each(
			function(element) {
				CalService.changeModeToWeek(displayEntries);
	    	}
	    );		
	    $$('input.scheduleMonthButtonStyleClass').each(
			function(element) {
				CalService.changeModeToMonth(displayEntries);
	    	}
	    );		
	    
	    
	}
	

/*
		if (arrayOfEntryIds)
			clearPreviousEntries();
		arrayOfEntryIds = new Array();
		for(var i = 0; i < result.length; i++){
			var entry = result[i];
			addEntryToCalendar(entry.entryName, entry.entryDate, entry.entryEndDate, entry.entryTime, entry.entryEndTime);
		}
*/
//	}	
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