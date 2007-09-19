var arrayOfParameters = null;
var arrayOfCheckedParameters = null;
var arrayOfEntryIds = null;
var scheduleEntryTableId = 'scheduleEntryTableId';
var scheduleButtonsId = 'scheduleButtonsId';
var groups_and_calendar_chooser_helper = null;
var scheduleId = null;
var arrayOfCheckedCalendarParameters = new Array();
var groups_and_calendar_chooser_helper = null;
var entryIdPrefix = '';
var showEntriesAsList = false;
var hideMenu = false;
var hidePreviousAndNext = false;
var showTime = false;
var calendarProperties = null;

var entriesToCalendar = null;
var entriesToList = null;
var entriesPackageSize = 2;
var packageIndex = 0;
var packageEndIndex = entriesPackageSize;
var entryInfo = document.createElement('div');

var previousHeight = null;
var previousElement = null;

var resizedElements = null;
var classNamesForExpanding = null;

var dayMode = 'DAY';
var weekMode = 'WEEK';
var workweekMode = 'WORKWEEK';
var monthMode = 'MONTH';

var defaultMode = monthMode;

var currentMode = defaultMode;

var ledgersName = '';

var groupName = '';


var elements = [];

//Labels
/*
//var entryNumber = 'Number';
var entryName = 'Name';
//var entryBeginDate = 'Begin date';
var entryEndDate = 'End date';
var entryType = 'Type';
var entryTime = 'Time';
var entryDate = 'Date';
//var entryDescription = 'Description';
var noEntries = 'There are no entries to display';
var serverErrorMessage = 'can\'t connect to:';
var loadingMsg = 'Loading...';
*/

var entryName = null;
var entryEndDate = null;
var entryType = null;
var entryTime = null;
var entryDate = null;
var noEntries = null;
var serverErrorMessage = null;
var loadingMsg = null;

var previousLabel = null;
var nextLabel = null;
var dayLabel = null;
var weekLabel = null;
var workweekLabel = null;
var monthLabel = null;

//CSS style classes

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
var entryInScheduleStyleClass = null;

// displays calendar entry types and ledgers. This is used while selecting group, ledgers and types

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
				ledgersName += ' '+element.name;
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
//				typeElement.setAttribute('class','calendarCheckbox');
				$(typeElement).addClass('calendarCheckbox');							
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
//		getEntriesButton.setAttribute('class', 'calendarButtonStyleClass');	
		$(getEntriesButton).addClass('calendarButtonStyleClass');
		getEntriesButton.setAttribute('value', 'Get entries');		
		parentOfList.appendChild(tableOfParameters);
		addButtonBehaviour();
	}

// adds behaviour on 'Refresh' button and checkboxes in ledgers and types list

	function addButtonBehaviour(){
	    $$('input.calendarButtonStyleClass').each(
		   	function(element) {
				element.onclick = function() {
					arrayOfCheckedParameters = new Array();
					for(var i = 0; i < arrayOfParameters.length; i++){					
						var element = document.getElementById(arrayOfParameters[i]);
						if (element.checked){
							arrayOfCheckedParameters.push(arrayOfParameters[i]);
						}
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
/*	
function addBehaviour(){
    $$('li.groupNode').each(
    	function(element) {
			element.onclick = function() {
				CalService.getCalendarParameters(element.id, displayCalendarAttributes);
			}
    	}
   	);
}	
*/	

	function saveGroupName(name){
		groupName = name;
	}

// returns layer with schedule buttons (previous, next, day...). Those buttons swithches to next or previous day/workweek/week/month, or changes view mode.

	function getScheduleButtons(){
		var scheduleNextButton = document.createElement('input');
		scheduleNextButton.setAttribute('type', 'button');
		scheduleNextButton.setAttribute('class', 'scheduleNextButtonStyleClass');	
		scheduleNextButton.setAttribute('value', nextLabel);

		scheduleNextButton.setAttribute('onclick', getNext);
		
		var schedulePreviousButton = document.createElement('input');
		schedulePreviousButton.setAttribute('type', 'button');
		schedulePreviousButton.setAttribute('class', 'schedulePreviousButtonStyleClass');	
		schedulePreviousButton.setAttribute('value', previousLabel);	

		schedulePreviousButton.setAttribute('onclick', getPrevious);
		
		var scheduleDayButton = document.createElement('input');
		scheduleDayButton.setAttribute('type', 'button');
		scheduleDayButton.setAttribute('class', 'scheduleDayButtonStyleClass');	
		scheduleDayButton.setAttribute('value', dayLabel);	

		scheduleDayButton.setAttribute('onclick', changeModeToDay);
		
		var scheduleWeekButton = document.createElement('input');
		scheduleWeekButton.setAttribute('type', 'button');
		scheduleWeekButton.setAttribute('class', 'scheduleWeekButtonStyleClass');	
		scheduleWeekButton.setAttribute('value', weekLabel);	
		
		scheduleWeekButton.setAttribute('onclick', changeModeToWeek);
		
		var scheduleWorkweekButton = document.createElement('input');
		scheduleWorkweekButton.setAttribute('type', 'button');
		scheduleWorkweekButton.setAttribute('class', 'scheduleWorkweekButtonStyleClass');	
		scheduleWorkweekButton.setAttribute('value', workweekLabel);

		scheduleWorkweekButton.setAttribute('onclick', changeModeToWorkweek);
		
		var scheduleMonthButton = document.createElement('input');
		scheduleMonthButton.setAttribute('type', 'button');
		scheduleMonthButton.setAttribute('class', 'scheduleMonthButtonStyleClass');	
		scheduleMonthButton.setAttribute('value', monthLabel);	
		scheduleMonthButton.setAttribute('onclick', changeModeToMonth);
		
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
	
//	switches schedule to next	
	
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
	
// switches schedule to previous	
	
	function getPrevious(){	
//alert('getPrevious');		
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
	
// changes mode to day	
	
	function changeModeToDay(){
		currentMode = dayMode;
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

// changes mode to workweek		

	function changeModeToWorkweek(){
		currentMode = workweekMode;
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
	
// changes mode to week	
	
	function changeModeToWeek(){
		currentMode = weekMode;
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

// changes mode to month	
	
	function changeModeToMonth(){
		currentMode = monthMode;
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

//	inserts schedule DOM object

	function displayEntries(result){			

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
		
		var tdText=document.createTextNode('Group: '+groupName+' Ledger:'+ledgersName); 
//		scheduleEntries.appendChild(tdText);
		
//		scheduleEntries.ap
		insertNodesToContainer(result, scheduleEntries);
		
		Window.onDomReady(MOOdalBox.init.bind(MOOdalBox));
//		MOOdalBox.init.bind(MOOdalBox);
		
/*
	var firstMonth = scheduleEntries.getElementsByTagName('table')[0];
	
	var daysOfMonth = firstMonth.getElementsByTagName('table');
	
	var firstDay = firstMonth.getElementsByTagName('table')[0];

	var emptyTable = result.getElementsByTagName('table')[1];
 
 	var newTable = new Element('table');
	var newDiv = new Element('div');
	var newTBody = new Element('tbody');	
*/	
	
	
	
/*	
	for(var index=0; index<daysOfMonth.length; index++) {
		var dayOfMonth = firstMonth.getElementsByTagName('table')[0];
//		var parentOfDay = dayOfMonth.parentNode;
//		console.log(daysOfMonth[index]);
//		result.removeChild(daysOfMonth[index]);
		$(dayOfMonth).remove();
//		parentOfDay.
	}
*/
/*
	while(true){
		var dayOfMonth = firstMonth.getElementsByTagName('tr')[1];
		if(dayOfMonth){
			$(dayOfMonth).remove();
		}
		else{
			break;
		}
	}
	*/ 
//	$('scheduleEntryTableId').remove();
//	removeChildren($('scheduleEntryTableId'));
	

//console.log(newTable);	
//	$(newTable).injectInside(firstDay);

//	newTable.appendChild(firstDay);


//	$(newTable).injectInside(firstDay);
//	newDiv.appendChild(newTable);

//	insertNodesToContainer(firstDay, newTable);
//	insertNodesToContainer(newTable, newDiv);
	
//console.log(newTable);	
//	newTable.appendChild(firstDay);
//	var scheduleLayerParent = scheduleLayer.parentNode();
//	$(scheduleLayer).injectInside(newTable);


//	insertNodesToContainer(newDiv, scheduleLayer);
//	firstDay.remble
//firstDay = makeValidCopyOfDayNode(firstDay);
//var placeForDay = firstMonth.getElementsByTagName('td')[0];
//placeForDay.appendChild(firstDay);

//insertNodesToContainer(firstDay, firstMonth.getElementsByTagName('td')[0]);



//newTable.appendChild(newTBody);
//	function removeEmptyAttributes(trTags){
/*
		var trTags = newTBody.getElementsByTagName('tr');
		removeEmptyAttributes(trTags);
		var tdTags = newTBody.getElementsByTagName('td');
		removeEmptyAttributes(tdTags);
		var divTags = newTBody.getElementsByTagName('div');
		removeEmptyAttributes(divTags);
		var tableTags = newTBody.getElementsByTagName('table');
		removeEmptyAttributes(tableTags);
		var aTags = newTBody.getElementsByTagName('a');
		removeEmptyAttributes(aTags);
*/

//$(scheduleLayer)


//scheduleLayer.appendChild(newTable);


//	scheduleLayer.appendChild(firstDay);

//	var secondTr = newTBody.getElementsByTagName('tr')[1];
//	$(secondTr).remove();

//	firstDay.removeChild(secondTr);
//console.log(scheduleLayer);	
//console.log(result);
//console.log(firstDay);
	

var xmlParent = document.getElementById('scheduleEntryTableId');

var xmlTag = xmlParent.getElementsByTagName('xml')[0];
if(xmlTag == null){
//	alert('null');
	var hiddenParent = xmlParent.getElementsByTagName('div')[0];
	var hiddenTags = hiddenParent.getElementsByTagName('input')[0];
	
	if(hiddenParent){
		hiddenParent.removeChild(hiddenTags);
	}
	hiddenTags = hiddenParent.getElementsByTagName('input')[0];
	if(hiddenParent){
		hiddenParent.removeChild(hiddenTags);
	}
	hiddenTags = hiddenParent.getElementsByTagName('input')[0];
	if(hiddenParent){
		hiddenParent.removeChild(hiddenTags);
	}		

//	insertNodesToContainer(result, firstDay);
	
}
else{
//	alert('xml');
	xmlParent.removeChild(xmlTag);
	var hiddenParent = xmlParent.getElementsByTagName('div')[0];
	var hiddenTags = hiddenParent.getElementsByTagName('input')[0];
	if(hiddenParent){
		hiddenParent.removeChild(hiddenTags);
	}
	hiddenTags = hiddenParent.getElementsByTagName('input')[0];
	if(hiddenParent){
		hiddenParent.removeChild(hiddenTags);
	}	
	hiddenTags = hiddenParent.getElementsByTagName('input')[0];
	if(hiddenParent){
		hiddenParent.removeChild(hiddenTags);
	}	
}

/* 
var baseLayer = document.getElementById('scheduleEntryTableId');
var scheduleLayerChild = baseLayer.getElementsByTagName('table')[0];
*/ 
//baseLayer.removeChild(scheduleLayerChild);
//		scheduleLayerChild = scheduleLayer.getElementsByTagName('div')[0];
//scheduleLayer.removeChild(scheduleLayerChild);f
		//$(scheduleLayerChild).removeClass('schedule-compact-default');
		
		$$('td.workday').each(
			function(element) {
				if(!element) return;
				$(element).setStyle('height', '121px');
	  			$(element).setStyle('width', '16.6667%');	  	
	  			if(window.ie){ 
	  				element.removeAttribute('$included');	
	  			}
	  			var tableDay = element.getElementsByTagName('table')[0];
	  			if(!tableDay) return;
	  			$(tableDay).setStyle('height','121px');
	    	}
	    );
	    
		$$('td.weekend').each(
			function(element) {
				if(!element) return;				
				$(element).setStyle('height', '60px');
	  			$(element).setStyle('width', '16.6667%');
	  			if(window.ie){ 
	  				element.removeAttribute('$included');	
	  			}	  	
	  			
	  			var tableDay = element.getElementsByTagName('table')[0];
	  			if(!tableDay) return;
	  			$(tableDay).setStyle('height','60px');	  				
	    	}
	    );

		$$('td.header').each(
			function(element) {
				
				$(element).setStyle('height', '18px');
	  			$(element).setStyle('width', '16.6667%');	
	  			if(window.ie){ 
	  				element.removeAttribute('$included');	
	  			}  		
	    	}
	    );
	
		$$('table.day').each(
			function(element) {
				
//				$(element).setStyle('height', '121px');
//	  			$(element).setStyle('width', '100%');	  	
	  			if(window.ie){ 
	  				element.removeAttribute('$included');	
	  			}	
	    	}
	    );
		
		
/*		
		
	for (var n = 0, d = elements.length; n < d; n++) {
        	//new for IE 6 and IE 7
	  	if(window.ie){ 
	  		elements[n].removeAttribute('$included');
//	  		$(elements[n]).setStyle('height', '103px');
//	  		$(elements[n]).setStyle('width', '100%');	  		
	  	}
	  	else elements[n].$included=false;
 
	 }		
*/	
	
$$.unique = function(array){
	var elements = [];
 
	for (var i = 0, l = array.length; i < l; i++){
		if (array[i].$included) continue;
		var element = $(array[i]);
 
		if (element && !element.$included){
 
			element.$included = true;
			elements.push(element);
		}
	}
 
	for (var n = 0, d = elements.length; n < d; n++) {
        	//new for IE 6 and IE 7
	  	if(window.ie){
	  		elements[n].removeAttribute('$included');
//	  		$(elements[n]).setStyle('height', '103px');
//	  		$(elements[n]).setStyle('width', '100%');	 
	  	}
	  	else elements[n].$included=false;
 
	 }
 
	return new Elements(elements);
};

//		
		setBehaviourOnScheduleEntries();
		closeLoadingMessage();		
	}
	
//	firstDay = makeValidCopyOfDayNode(firstDay);
	function makeValidCopyOfDayNode(firstDay){


//	return firstDay;

//		var tableEl = new Element('table');
		var tableEl = document.createElement('div');
		var tbodyEl = new Element('div');
		var trHeader = new Element('div');
		var trHeader2 = document.createElement('tr');
		var tdHeader2 = document.createElement('td');
		$(trHeader2).removeProperty('language');
		
		
		
//	trHeader2.appendChild(tdHeader2);
//		tableEl.appendChild(trHeader2);
//		$(trHeader2).injectInside(tableEl);
//		tableEl.appendChild(tbodyEl);
//		return tableEl;

		var oldTrHeader = firstDay.getElementsByTagName('tr')[0];		

		var tdHeader = document.createElement('div');
		var oldTdHeader = oldTrHeader.getElementsByTagName('td')[0];
		tdHeader.setAttribute('id',oldTdHeader.id);
		tdHeader.setAttribute('class',oldTdHeader.getAttribute('class'));
		tdHeader.setAttribute('style',oldTdHeader.getAttribute('style'));
		tdHeader.appendChild(oldTdHeader.childNodes[0]);
		trHeader.appendChild(tdHeader);
		tableEl.appendChild(trHeader);
		
		var trContent = new Element('div');
		var oldTrContent = firstDay.getElementsByTagName('tr')[1];		
//console.log(oldTrContent);
		var tdContent = new Element('div');
		var oldTdContent = oldTrContent.getElementsByTagName('td')[0];		
		
		var oldFirstDiv = oldTdContent.getElementsByTagName('div')[0];
		
//console.log(oldTdContent.childNodes[0]);		
		$(tdContent).addClass(oldTdContent.getAttribute('class'));
		$(tdContent).setProperty('style',$(oldTdContent).getProperty('style'));

		var firstDiv = new Element('div');
//		$(firstDiv).addClass(oldFirstDiv.getAttribute('class'));
$(firstDiv).addClass('content');
		$(firstDiv).setProperty('style',$(oldFirstDiv).getProperty('style'));		
		var oldSecondDiv = oldFirstDiv.getElementsByTagName('div')[0];
		var secondDiv = new Element('div');
//		$(secondDiv).addClass(oldSecondDiv.getAttribute('class'));		
		$(secondDiv).setProperty('style',$(oldSecondDiv).getProperty('style'));
		secondDiv.setAttribute('id', oldSecondDiv.getAttribute('id'));
//		var tdText=document.createTextNode('Drop templates here'); 
//		var oldInnerTable = $(secondDiv).getFirst();
		var oldInnerTable = oldSecondDiv.getElementsByTagName('table')[0];
//console.log(secondDiv.childNodes);		
		var innerTable = new Element('div');
		$(innerTable).setProperty('style',$(oldInnerTable).getProperty('style'));
		
		var oldInnerTrTags = oldInnerTable.getElementsByTagName('tr');
		for(var index=0; index<oldInnerTrTags.length; index++) {
			
			var innerDivTag = new Element('div');
			
			var innerTrTag = new Element('div');
			var oldInnerTrTag = oldInnerTrTags[index];
			var innerTdTag = new Element('div');
			var oldInnerTdTag = $(oldInnerTrTag).getFirst();
			$(innerTdTag).setProperty('style',$(oldInnerTdTag).getProperty('style'));
			var innerATag = new Element('a');
			var oldInnerATag = $(oldInnerTdTag).getFirst();
			$(innerATag).setProperty('id',$(oldInnerATag).getProperty('id'));
			$(innerATag).setProperty('href',$(oldInnerATag).getProperty('href'));
			
			$(innerATag).addClass(oldInnerATag.getAttribute('class'));
//			innerATag.appendChild(oldInnerATag.childNodes[0]);
//			innerTdTag.appendChild(oldInnerATag.childNodes[0]);
			innerTdTag.appendChild(innerATag);
			innerTrTag.appendChild(innerTdTag);			
			innerTable.appendChild(innerTrTag);
			
			innerDivTag.appendChild(oldInnerATag.childNodes[0]);
			secondDiv.appendChild(innerDivTag);
		}
		
		
//		secondDiv.appendChild(innerTable);
		firstDiv.appendChild(secondDiv);
		tdContent.appendChild(firstDiv);
		trContent.appendChild(tdContent);
		tableEl.appendChild(trContent);
//		tbodyEl.appendChild(trContent)
//		tableEl.appendChild(tbodyEl);
//console.log(oldTdHeader.childNodes[0]);		
//		return firstDay;
//		return tableEl;
		return tableEl;
	}
	
	//inserts entry list into DOM
/*	
	function removeEmptyAttributes(trTags){
//		var trTags = newTBody.getElementsByTagName('tr');
		for(var index=0; index<trTags.length; index++) {
			var trTag = trTags[index];
		
			if($(trTag).getProperty('language') == null ||$(trTag).getProperty('language') == ""){
				$(trTag).removeProperty('language');
			}
			if($(trTag).getProperty('class') == null ||$(trTag).getProperty('class') == ""){
				$(trTag).removeProperty('class');
			}
			if($(trTag).getProperty('lang') == null ||$(trTag).getProperty('lang') == ""){
				$(trTag).removeProperty('lang');
			}
			if($(trTag).getProperty('title') == null ||$(trTag).getProperty('title') == ""){
				$(trTag).removeProperty('title');
			}
			if($(trTag).getProperty('urn') == null ||$(trTag).getProperty('urn') == ""){
				$(trTag).removeProperty('urn');
			}
			if($(trTag).getProperty('dataSrc') == null ||$(trTag).getProperty('dataSrc') == ""){
				$(trTag).removeProperty('dataSrc');
			}
//			if(($(trTag).getProperty('dataFormatAs') == "null") || ($(trTag).getProperty('dataFormatAs') == "") ||
//				($(trTag).getProperty('dataFormatAs') == null)){
				$(trTag).removeProperty('dataFormatAs');
				$(trTag).removeProperty('dataFormatAs');
				
//			}
			if($(trTag).getProperty('accessKey') == null ||$(trTag).getProperty('accessKey') == ""){
				$(trTag).removeProperty('accessKey');
			}
			if($(trTag).getProperty('chOff') == null ||$(trTag).getProperty('chOff') == ""){
				$(trTag).removeProperty('chOff');
			}
			if($(trTag).getProperty('ch') == null ||$(trTag).getProperty('ch') == ""){
				$(trTag).removeProperty('ch');
			}
			if($(trTag).getProperty('dataFld') == null ||$(trTag).getProperty('dataFld') == ""){
				$(trTag).removeProperty('dataFld');
			}
			if($(trTag).getProperty('implementation') == null ||$(trTag).getProperty('implementation') == ""){
				$(trTag).removeProperty('implementation');
			}
			if($(trTag).getProperty('abbr') == null ||$(trTag).getProperty('abbr') == ""){
				$(trTag).removeProperty('abbr');
			}
			if($(trTag).getProperty('axis') == null ||$(trTag).getProperty('axis') == ""){
				$(trTag).removeProperty('axis');
			}
			if($(trTag).getProperty('headers') == null ||$(trTag).getProperty('headers') == ""){
				$(trTag).removeProperty('headers');
			}
			if($(trTag).getProperty('scope') == null ||$(trTag).getProperty('scope') == ""){
				$(trTag).removeProperty('scope');
			}
			if($(trTag).getProperty('background') == null ||$(trTag).getProperty('background') == ""){
				$(trTag).removeProperty('background');
			}
			if($(trTag).getProperty('target') == null ||$(trTag).getProperty('target') == ""){
				$(trTag).removeProperty('target');
			}
			if($(trTag).getProperty('rev') == null ||$(trTag).getProperty('rev') == ""){
				$(trTag).removeProperty('rev');
			}
			if($(trTag).getProperty('hreflang') == null ||$(trTag).getProperty('hreflang') == ""){
				$(trTag).removeProperty('hreflang');
			}
			if($(trTag).getProperty('shape') == null ||$(trTag).getProperty('shape') == ""){
				$(trTag).removeProperty('shape');
			}
			if($(trTag).getProperty('type') == null ||$(trTag).getProperty('type') == ""){
				$(trTag).removeProperty('type');
			}
			if($(trTag).getProperty('coords') == null ||$(trTag).getProperty('coords') == ""){
				$(trTag).removeProperty('coords');
			}
			if($(trTag).getProperty('rel') == null ||$(trTag).getProperty('rel') == ""){
				$(trTag).removeProperty('rel');
			}
			if($(trTag).getProperty('charset') == null ||$(trTag).getProperty('charset') == ""){
				$(trTag).removeProperty('charset');
			}
			if($(trTag).getProperty('tabIndex') == null ||$(trTag).getProperty('tabIndex') == ""){
				$(trTag).removeProperty('tabIndex');
			}
			if($(trTag).getProperty('nofocusrect') == null ||$(trTag).getProperty('nofocusrect') == ""){
				$(trTag).removeProperty('nofocusrect');
			}
			if($(trTag).getProperty('methods') == null ||$(trTag).getProperty('methods') == ""){
				$(trTag).removeProperty('methods');
			}
			if($(trTag).getProperty('summary') == null ||$(trTag).getProperty('summary') == ""){
				$(trTag).removeProperty('summary');
			}

		//	console.log($(trTag).getProperties());
			
		}	
		
	}
*/	
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
			closeLoadingMessage();
			return;
		}
		
		var scheduleList = document.createElement('div');
//		scheduleList.setAttribute('class', entryListTableStyleClass);
		$(scheduleList).addClass(entryListTableStyleClass);
		scheduleList.setAttribute('id', 'listOfEntries');
		
		var listCaptionRow = document.createElement('div');
//		listCaptionRow.setAttribute('class', entryListCaptionStyleClass);
		$(listCaptionRow).addClass(entryListCaptionStyleClass);
		
		var nameOfEntry=document.createElement('div');
//		nameOfEntry.setAttribute('class',entryListElementStyleClass);		
		$(nameOfEntry).addClass(entryListElementStyleClass);
		var dateOfEntry=document.createElement('div');
//		dateOfEntry.setAttribute('class',entryListElementStyleClass);		
		$(dateOfEntry).addClass(entryListElementStyleClass);
		var timeOfEntry=document.createElement('div');
//		timeOfEntry.setAttribute('class',entryListElementStyleClass);
		$(timeOfEntry).addClass(entryListElementStyleClass);
		
		var txtName=document.createTextNode(entryName);
		var txtDate=document.createTextNode(entryDate);		
		var txtTime=document.createTextNode(entryTime);	
							
		nameOfEntry.appendChild(txtName);
		dateOfEntry.appendChild(txtDate);
		timeOfEntry.appendChild(txtTime);		
				  	
		listCaptionRow.appendChild(dateOfEntry);
		listCaptionRow.appendChild(timeOfEntry);
		listCaptionRow.appendChild(nameOfEntry);

		scheduleList.appendChild(listCaptionRow);
		testTable = listCaptionRow;
		for(var index=0; index<entries.length; index++) {
			var listRow=document.createElement('a');
			if(index % 2 == 0){
//				listRow.setAttribute('class', entryListEvenRowStyleClass+' '+entries[index].entryTypeName);
				$(listRow).addClass(entryListEvenRowStyleClass);
				$(listRow).addClass(entries[index].entryTypeName);
			}
			else{
//				listRow.setAttribute('class', entryListOddRowStyleClass+' '+entries[index].entryTypeName);				
				$(listRow).addClass(entryListOddRowStyleClass);
				$(listRow).addClass(entries[index].entryTypeName);
			}
			listRow.setAttribute('id', entryIdPrefix+index);
			$(listRow).setProperty('rel', 'moodalbox');
			$(listRow).setProperty('href', '/servlet/ObjectInstanciator?idegaweb_instance_class=com.idega.block.cal.presentation.EntryInfoBlock&entryName='+entries[index].entryName+'&entryStartTime='+entries[index].localizedEntryDate+' '+entries[index].entryDate.substring(11,16)+ '&entryEndTime='+entries[index].entryEndDate.substring(11,16)+'&entryDescription='+entries[index].entryDescription);
			var nameOfEntry=document.createElement('div');
//			nameOfEntry.setAttribute('class',entryListElementStyleClass);
			$(nameOfEntry).addClass(entryListElementStyleClass);
			var dateOfEntry=document.createElement('div');		
//			dateOfEntry.setAttribute('class',entryListElementStyleClass);
			$(dateOfEntry).addClass(entryListElementStyleClass);
			var timeOfEntry=document.createElement('div');	
//			timeOfEntry.setAttribute('class',entryListElementTimeStyleClass);
			$(timeOfEntry).addClass(entryListElementTimeStyleClass);
			if(showTime){
				timeOfEntry.style.display = 'inline';
			}
			
			var txtName=document.createTextNode(entries[index].entryName);
			var txtDate=document.createTextNode(entries[index].localizedEntryDate);		
			var txtTime=document.createTextNode(entries[index].entryDate.substring(11,16)+'-'+
				entries[index].entryEndDate.substring(11,16));	
			nameOfEntry.appendChild(txtName);
			dateOfEntry.appendChild(txtDate);
			timeOfEntry.appendChild(txtTime);		
					  	
			listRow.appendChild(dateOfEntry);
			listRow.appendChild(timeOfEntry);
			listRow.appendChild(nameOfEntry);
			scheduleList.appendChild(listRow);
		}
		scheduleEntries.appendChild(scheduleList);
		setBehaviourOnListRows();
		Window.onDomReady(MOOdalBox.init.bind(MOOdalBox));
		closeLoadingMessage();
	}

//	creates entry info table

	function createInfoTable(entryNameValue, entryDateValue, entryTimeValue, entryEndTimeValue, entryTypeValue, entryDescriptionValue){
		var txtName=document.createTextNode(entryNameValue);
		var txtDate=document.createTextNode(entryDateValue);		
		var txtTime=document.createTextNode(entryTimeValue+'-'+entryEndTimeValue);	
		var txtType=document.createTextNode(entryTypeValue);		
		var txtDescription=document.createTextNode(entryDescriptionValue);		
		
		var nameOfEntry=document.createElement('div');
		nameOfEntry.appendChild(txtName);
//		nameOfEntry.setAttribute('class', entryInfoNameStyleClass);
		$(nameOfEntry).addClass(entryInfoNameStyleClass);
		var dateOfEntry=document.createElement('div');			
		dateOfEntry.appendChild(txtDate);			
//		dateOfEntry.setAttribute('class', entryInfoDateStyleClass);		
		$(dateOfEntry).addClass(entryInfoDateStyleClass);
		var timeOfEntry=document.createElement('div');			
		timeOfEntry.appendChild(txtTime);			
//		timeOfEntry.setAttribute('class', entryInfoTimeStyleClass);
		$(timeOfEntry).addClass(entryInfoTimeStyleClass);
		var typeOfEntry=document.createElement('div');			
		typeOfEntry.appendChild(txtType);			
//		typeOfEntry.setAttribute('class', entryInfoTypeStyleClass);
		$(typeOfEntry).addClass(entryInfoTypeStyleClass);
		var descriptionOfEntry=document.createElement('div');			
		descriptionOfEntry.appendChild(txtDescription);			
//		descriptionOfEntry.setAttribute('class', entryInfoDescriptionStyleClass);
		$(descriptionOfEntry).addClass(entryInfoDescriptionStyleClass);
		var entryInfoHeader = document.createElement('div');
//		entryInfoHeader.setAttribute('class', 'entryInfoHeader');
		$(entryInfoHeader).addClass('entryInfoHeader');		
		var entryInfoBody = document.createElement('div');
//		entryInfoBody.setAttribute('class', 'entryInfoBody');
		$(entryInfoBody).addClass('entryInfoBody');		
		
		entryInfoHeader.appendChild(dateOfEntry);
		entryInfoHeader.appendChild(timeOfEntry);
		entryInfoHeader.appendChild(nameOfEntry);
		entryInfoHeader.appendChild(typeOfEntry);
		entryInfoBody.appendChild(descriptionOfEntry);
		
		entryInfo.appendChild(entryInfoHeader);
		entryInfo.appendChild(entryInfoBody);
//		document.getElementById(scheduleEntryTableId).appendChild(entryInfo);
		document.getElementsByTagName('body')[0].appendChild(entryInfo);
//		entryInfo.setAttribute('class', entryInfoStyleClass);
		$(entryInfo).addClass(entryInfoStyleClass);		
	}

//	sets behaviour on entry list rows

	function setBehaviourOnListRows(){
		$$('div.'+entryListEvenRowStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
//					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
//					removeChildren(entryInfo);
				}
	    	}
	    );	
		$$('div.'+entryListOddRowStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
//					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );	
	}

	//setting behaviour on entries in schedule

	function setBehaviourOnScheduleEntries(){
		$$('a.'+entryInScheduleStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
//					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
/*
				element.onclick = function(){

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
					}

					var entryInfoParent = document.getElementById('mb_contents');
					if(entryInfoParent){
						entryInfoParent.appendChild(entryInfo);
					}

				}
*/
    	}
	    );		
		$$('a.entry').each(
			function(element) {
				element.onmouseover = function(e) {
//					displayEntryInfo(element, e);
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );
	    if(currentMode == monthMode){
		    $$('td.content').each(
		    	function(element){
					element.onclick = function(){
						var d = new Date();
						var beginOfFunction = d.getTime();
	
						if(resizedElements){
							for(var index=0; index<resizedElements.length; index++) {
								resizedElements[index].removeClass(classNamesForExpanding[index]);
								if(index < 5){
//									resizedElements[index].addClass('workdayCollapsed');
									resizedElements[index].addClass('collapsed');
								}
								else{
									resizedElements[index].addClass('collapsed');
								}
							}
						}
						var dd = new Date();
						var after = dd.getTime();
						resizedElements = new Array();
						classNamesForExpanding = new Array();
						var trElement = element.parentNode;
						var tableElement = trElement.parentNode;
						var tdDay = tableElement.parentNode;
	//					tdDay.addClass('selectedDay'); 
						var trParent = tdDay.parentNode;
						var tbody = trParent.parentNode;
						var isSunday = false;
						
						if (trParent.getChildren().length == 1)
							isSunday = true;
						for(var i=0; i<tbody.getChildren().length; i++) {
							trParent = tbody.getChildren()[i];
							if(isSunday){
								if(i % 2 != 0){
									trParent.getChildren()[0].removeClass('notSelectedDay');
									trParent.getChildren()[0].addClass('selectedDay');								
								}
								else{
									for(var index=0; index<trParent.getChildren().length; index++) {
										trParent.getChildren()[index].removeClass('selectedDay');
										trParent.getChildren()[index].addClass('notSelectedDay'); 
									}
								}	
							}		//workday or saturday (not sunday)
							else{
								if(i % 2 == 0){
									for(var index=0; index<trParent.getChildren().length; index++) {
										if(trParent.getChildren()[index] != tdDay){
											trParent.getChildren()[index].removeClass('selectedDay');
											trParent.getChildren()[index].addClass('notSelectedDay'); 
										}
										else{
											trParent.getChildren()[index].removeClass('notSelectedDay');										
											trParent.getChildren()[index].addClass('selectedDay'); 
										}			 
									}							
								}
								else{
									trParent.getChildren()[0].removeClass('selectedDay');
									trParent.getChildren()[0].addClass('notSelectedDay');
								}
							}
						}
/*				
var widthTime = new Date();
var afterExpandingWidth = widthTime.getTime();					
*/ 
						trParent = tdDay.parentNode;
	//					if (tsesedDay.getAttribute('class').substring(4,11) == 'workday'){			//WORKDAY
						if (tdDay.getAttribute('class').match('workday')){			//WORKDAY
							resizedElements.push(trParent);
							classNamesForExpanding.push('expandedWorkday');
							trParent.removeClass('collapsed');
							trParent.addClass('expandedWorkday');
							trParent = trParent.getNext();
							trParent.removeClass('collapsed');
							trParent.addClass('expandedWorkday');
							resizedElements.push(trParent);
							classNamesForExpanding.push('expandedWorkday');
							
/*						
							resizedElements.push(element);
							classNamesForExpanding.push('workdayExpanded');  
							element.removeClass('workdayCollapsed');
							element.addClass('workdayExpanded');
							var tdElements = trParent.getElementsByTagName('td');
							var dayNumber = 0;
							for(var index=0; index<tdElements.length; index++) {
								var currentTdElement = tdElements[index];
								if(currentTdElement.getAttribute('class')){
									if(currentTdElement.getAttribute('class').toString().match('content')){
	
										dayNumber++;
										if(dayNumber != 6){
											if(element != tdElements[index]){
												resizedElements.push(tdElements[index]);
												classNamesForExpanding.push('workdayExpanded');
												tdElements[index].removeClass('workdayCollapsed');
												tdElements[index].addClass('workdayExpanded');
											}
										}
										else{
											resizedElements.push(tdElements[index]);
											classNamesForExpanding.push('weekendOnExpandedWorkday');
											tdElements[index].removeClass('weekendCollapsed');										
											tdElements[index].addClass('weekendOnExpandedWorkday');
										}
									}
								}
							}	
							trParent = trParent.getNext();
							tdElements = trParent.getElementsByTagName('td');
							for(var index=0; index<tdElements.length; index++) {
								var currentTdElement = tdElements[index];
								if(currentTdElement.getAttribute('class')){
	//								if((currentTdElement.getAttribute('class').toString() == 'content')||
	//									(currentTdElement.getAttribute('class').toString() == 'content weekendCollapsed')){
									if(currentTdElement.getAttribute('class').toString().match('content')){
										resizedElements.push(tdElements[index]);
										classNamesForExpanding.push('weekendOnExpandedWorkday');
										tdElements[index].removeClass('weekendCollapsed');
										tdElements[index].addClass('weekendOnExpandedWorkday');
									}
								}						
							}		
*/											
						}
						else{
							if (trParent.getChildren().length == 1){				//SUNDAY
								trParent = trParent.getPrevious();
								
								trParent.removeClass('collapsed');
								trParent.addClass('expandedSunday');
								resizedElements.push(trParent);
								classNamesForExpanding.push('expandedSunday');
								trParent = trParent.getNext();
								trParent.removeClass('collapsed');
								trParent.addClass('expandedSunday');	
								resizedElements.push(trParent);
								classNamesForExpanding.push('expandedSunday');							
								
								var tdElements = trParent.getElementsByTagName('td');
								var dayNumber = 0;
/*								
								for(var index=0; index<tdElements.length; index++) {
									var currentTdElement = tdElements[index];
									if(currentTdElement.getAttribute('class')){
										if((currentTdElement.getAttribute('class').toString() == 'content')){
											dayNumber++;
											if(dayNumber != 6){
												resizedElements.push(tdElements[index]);
												classNamesForExpanding.push('workdayOnExpandedWeekend');
												tdElements[index].removeClass('workdayCollapsed');
												tdElements[index].addClass('workdayOnExpandedWeekend');
											}
										}
									}
								}
*/								
/*
								resizedElements.push(element);
								classNamesForExpanding.push('weekendExpanded');							
								element.removeClass('weekendCollapsed');
								element.addClass('weekendExpanded');
*/								
							}
							else{	//SATURDAY
							
								trParent.removeClass('collapsed');
								trParent.addClass('expandedSaturday');
								resizedElements.push(trParent);
								classNamesForExpanding.push('expandedSaturday');
								trParent = trParent.getNext();
								trParent.removeClass('collapsed');
								trParent.addClass('expandedSaturday');	
								resizedElements.push(trParent);
								classNamesForExpanding.push('expandedSaturday');
								
//								var tdElements = trParent.getElementsByTagName('td');
//								var dayNumber = 0;
/*								
								for(var index=0; index<tdElements.length; index++) {
									var currentTdElement = tdElements[index];
									if(currentTdElement.getAttribute('class')){
										if((currentTdElement.getAttribute('class').toString() == 'content')||
											(currentTdElement.getAttribute('class').toString() == 'content weekendCollapsed')||
											(currentTdElement.getAttribute('class').toString() == 'content workdayCollapsed')){
											dayNumber++;
											if(dayNumber != 6){
												resizedElements.push(tdElements[index]);
												classNamesForExpanding.push('workdayOnExpandedWeekend');
												currentTdElement.removeClass('workdayCollapsed');
												currentTdElement.addClass('workdayOnExpandedWeekend');
											}
											else{
												resizedElements.push(currentTdElement);
												classNamesForExpanding.push('weekendExpanded');
												currentTdElement.removeClass('weekendCollapsed');
												currentTdElement.addClass('weekendExpanded');
	
	//											tdElements[index].style.height = tdElements[index].getCoordinates().height+heightToAdd+'px';
											}
										}
									}
								}
*/								
//								var trParent = trParent.getNext();
//								tdElements = trParent.getElementsByTagName('td');
	/*							
								for(var index=0; index<tdElements.length; index++) {
									var currentTdElement = tdElements[index];
									if(currentTdElement.getAttribute('class')){
										if(currentTdElement.getAttribute('class').toString() == 'content'){
	//										tdElements[index].style.height = ((currentHeight-18)/2)+'px';
	//										tdElements[index].style.height = previousHeight+'px';
										}	
									}					
								}
	*/
/*
								resizedElements.push(element);
								classNamesForExpanding.push('weekendExpanded');			
								element.removeClass('weekendCollapsed');				
								element.addClass('weekendExpanded');
*/
							}						
							
						}
						var d2 = new Date();
						var endOfFunction = d2.getTime();
					}
	//	    		var previousHeight = null;
	    		
		    	}
		    );
	    }
	}

//	displays entry info table

	function displayEntryInfo(element, e){
//element.appendChild(entryInfo);
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
/*
if(window.event.clientX == null){
	alert('e.clientX == null');
}
else{
	alert(window.event.clientX/1);
}
*/			
//			dragDrop_x = e.clientX/1 + document.body.scrollLeft;
//			dragDrop_x = window.event.clientX/1 + document.body.scrollLeft;
var event = new Event(e);
			dragDrop_x = event.page.x/1 + document.body.scrollLeft;
			
//			dragDrop_y = e.clientY/1 + document.documentElement.scrollTop;	
//			dragDrop_y = event.page.y/1;
//			dragDrop_y = e.clientY/1;
//			dragDrop_y = event.page.y/1 -$('listOfEntries').getCoordinates().top;
			dragDrop_y = event.client.y/1;
			var clientY = event.client.y/1;
			var pageY = event.page.y/1;
//			var windPageY = window.event.clientY/1;
//alert('event.client.y '+clientY+' event.page.y '+pageY+' windPageY '+windPageY);

			// + document.documentElement.scrollTop;	
//			dragDrop_y = e.clientY/1;				
			
	
//			dragDrop_x = 400;
						
//			entryInfo.style.left = dragDrop_x + 'px';
			if(!showEntriesAsList){
				if (dragDrop_x > 600){
					dragDrop_x = dragDrop_x - 200;
				}
//				entryInfo.style.left = dragDrop_x + 5 + 'px';
				$(entryInfo).setStyle('left',dragDrop_x + 5 + 'px');
			}
			else{
				var groupListElement = $('listOfEntries');
				var groupListElementXCoordinate = groupListElement.getPosition().x;
				var groupListElementWidth = groupListElement.getCoordinates().width;
				
				$(entryInfo).setStyle('left',groupListElementXCoordinate +groupListElementWidth +10+ 'px');
				
//				entryInfo.style.left = groupListElementXCoordinate +groupListElementWidth +10+ 'px';
			}
			
			
//			$(entryInfo).setStyle('top', dragDrop_y + 'px');
			$(entryInfo).setStyle('top', element.getCoordinates().top + 'px');
//			entryInfo.style.top = dragDrop_y + 'px';
//alert(entryInfo.style.top);			
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
/*
	function createEmptySchedule(result){
		var scheduleLayer = document.getElementById('calendarViewerScheduleId');
		var scheduleEntries = document.getElementById(scheduleEntryTableId);
		scheduleEntries = document.createElement('div');
		scheduleEntries.setAttribute('id',scheduleEntryTableId);
		scheduleLayer.appendChild(scheduleEntries);
		scheduleLayer.appendChild(getScheduleButtons());
		insertNodesToContainer(result, scheduleEntries);
	}
*/

// sets behaviour on schedule buttons (previous, next, day...)

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

	// returns calendar properties bean

	function getCalendarProperties(){
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getCalendarProperties(scheduleId, {
			callback: function(result) {
				calendarProperties = result;
				getEntries(result.remoteMode, result.server, result.login, result.password, result.calendarAttributes);
			}
		});
	}
	
	// prepares DWR to get remote or local calendar parameters
	
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
	
	//returns calendar parameters from local server
	
	function getLocalCalendarParameters(groupId){
		showLoadingMessage(loadingMsg);
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getCalendarParameters(groupId, displayCalendarAttributes);
	}
		
//checks if remote server can be reached	
		
	function canUseRemoteCalendar(groupId){
		showLoadingMessage(loadingMsg);
		CalService.canUseRemoteServer(SERVER, {
			callback: function(result) {
				canUseRemoteCalendarCallback(result, groupId);
			}
		});
	}
	
//returns calendar parameters from remote server
	
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
	
// get entries from local server or checks connection with remote server
	
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
	
/*	
	function getCalendarConnectionPropertiesCallback(){
		CalService.canUseRemoteServer(server, {
			callback: function(result) {
				getEntriesCallback(result, server, login, password, calendarAttributes);
			}
		});		
	}
*/
	
	//gets entries from remote server
	
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
	
	//sends package of entries to server side
	
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
	
	//entries are displayed as list or sent to server side to get schedule DOM object
	
	function setEntries(entries){
		entriesToList = entries;
		if(showEntriesAsList){
			displayEntriesAsList(entries);
		}
		else{
			currentMode = monthMode;
			ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
		}
	}
	
	//sends first package of entries to serverside
	
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
	
