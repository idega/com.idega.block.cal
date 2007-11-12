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

//	Labels
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
var entryInScheduleStyleClass = null;

	//	Displays calendar entry types and ledgers. This is used while selecting group, ledgers and types
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
		$(getEntriesButton).addClass('calendarButtonStyleClass');
		getEntriesButton.setAttribute('value', 'Get entries');		
		parentOfList.appendChild(tableOfParameters);
		addButtonBehaviour();
	}

	//	Adds behaviour on 'Refresh' button and checkboxes in ledgers and types list
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
					
					prepareDwr(CalService, getDefaultDwrPath());
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

	function saveGroupName(name){
		groupName = name;
	}

	//	Returns layer with schedule buttons (previous, next, day...). Those buttons swithches to next or previous day/workweek/week/month, or changes view mode.
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
	
	//	Switches schedule to next	
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
	
	//	Switches schedule to previous	
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
	
	//	Changes mode to day	
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

	//	Changes mode to workweek		
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
	
	// Changes mode to week
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

	//	Changes mode to month
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

	//	Inserts schedule DOM object
	function displayEntries(result) {			

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
		insertNodesToContainer(result, scheduleEntries);
		
		Window.onDomReady(MOOdalBox.init.bind(MOOdalBox));

		var xmlParent = document.getElementById('scheduleEntryTableId');
		
		var xmlTag = xmlParent.getElementsByTagName('xml')[0];
		if(xmlTag == null){
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
		else{
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
	  			if(window.ie){ 
	  				element.removeAttribute('$included');	
	  			}	
	    	}
	    );
	    
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
			  	}
			  	else elements[n].$included=false;
		 
			 }
		 
			return new Elements(elements);
		};

		setBehaviourOnScheduleEntries();
		closeLoadingMessage();		
	}
	
	function makeValidCopyOfDayNode(firstDay){
//	return firstDay;
		var tableEl = document.createElement('div');
		var tbodyEl = new Element('div');
		var trHeader = new Element('div');
		var trHeader2 = document.createElement('tr');
		var tdHeader2 = document.createElement('td');
		$(trHeader2).removeProperty('language');

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
		var tdContent = new Element('div');
		var oldTdContent = oldTrContent.getElementsByTagName('td')[0];		
		
		var oldFirstDiv = oldTdContent.getElementsByTagName('div')[0];	
		$(tdContent).addClass(oldTdContent.getAttribute('class'));
		$(tdContent).setProperty('style',$(oldTdContent).getProperty('style'));

		var firstDiv = new Element('div');
		$(firstDiv).addClass('content');
		$(firstDiv).setProperty('style',$(oldFirstDiv).getProperty('style'));		
		var oldSecondDiv = oldFirstDiv.getElementsByTagName('div')[0];
		var secondDiv = new Element('div');	
		$(secondDiv).setProperty('style',$(oldSecondDiv).getProperty('style'));
		secondDiv.setAttribute('id', oldSecondDiv.getAttribute('id'));
		var oldInnerTable = oldSecondDiv.getElementsByTagName('table')[0];	
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
			innerTdTag.appendChild(innerATag);
			innerTrTag.appendChild(innerTdTag);			
			innerTable.appendChild(innerTrTag);
			
			innerDivTag.appendChild(oldInnerATag.childNodes[0]);
			secondDiv.appendChild(innerDivTag);
		}
		
		firstDiv.appendChild(secondDiv);
		tdContent.appendChild(firstDiv);
		trContent.appendChild(tdContent);
		tableEl.appendChild(trContent);
		return tableEl;
	}
	
	//	Inserts entry list into DOM
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
		$(scheduleList).addClass(entryListTableStyleClass);
		scheduleList.setAttribute('id', 'listOfEntries');
		
		var listCaptionRow = document.createElement('div');
		$(listCaptionRow).addClass(entryListCaptionStyleClass);
		
		var nameOfEntry=document.createElement('div');
		$(nameOfEntry).addClass(entryListElementStyleClass);
		var dateOfEntry=document.createElement('div');
		$(dateOfEntry).addClass(entryListElementStyleClass);
		var timeOfEntry=document.createElement('div');
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
				$(listRow).addClass(entryListEvenRowStyleClass);
				$(listRow).addClass(entries[index].entryTypeName);
			}
			else{
				$(listRow).addClass(entryListOddRowStyleClass);
				$(listRow).addClass(entries[index].entryTypeName);
			}
			listRow.setAttribute('id', entryIdPrefix+index);
			$(listRow).setProperty('rel', 'moodalbox');
			$(listRow).setProperty('href', '/servlet/ObjectInstanciator?idegaweb_instance_class=com.idega.block.cal.presentation.EntryInfoBlock&entryName='+entries[index].entryName+'&entryStartTime='+entries[index].localizedEntryDate+' '+entries[index].entryDate.substring(11,16)+ '&entryEndTime='+entries[index].entryEndDate.substring(11,16)+'&entryDescription='+entries[index].entryDescription);
			var nameOfEntry=document.createElement('div');
			$(nameOfEntry).addClass(entryListElementStyleClass);
			var dateOfEntry=document.createElement('div');
			$(dateOfEntry).addClass(entryListElementStyleClass);
			var timeOfEntry=document.createElement('div');
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

	//	Creates entry info table
	function createInfoTable(entryNameValue, entryDateValue, entryTimeValue, entryEndTimeValue, entryTypeValue, entryDescriptionValue){
		var txtName=document.createTextNode(entryNameValue);
		var txtDate=document.createTextNode(entryDateValue);		
		var txtTime=document.createTextNode(entryTimeValue+'-'+entryEndTimeValue);	
		var txtType=document.createTextNode(entryTypeValue);		
		var txtDescription=document.createTextNode(entryDescriptionValue);		
		
		var nameOfEntry=document.createElement('div');
		nameOfEntry.appendChild(txtName);
		$(nameOfEntry).addClass(entryInfoNameStyleClass);
		var dateOfEntry=document.createElement('div');			
		dateOfEntry.appendChild(txtDate);
		$(dateOfEntry).addClass(entryInfoDateStyleClass);
		var timeOfEntry=document.createElement('div');			
		timeOfEntry.appendChild(txtTime);
		$(timeOfEntry).addClass(entryInfoTimeStyleClass);
		var typeOfEntry=document.createElement('div');			
		typeOfEntry.appendChild(txtType);
		$(typeOfEntry).addClass(entryInfoTypeStyleClass);
		var descriptionOfEntry=document.createElement('div');			
		descriptionOfEntry.appendChild(txtDescription);	
		$(descriptionOfEntry).addClass(entryInfoDescriptionStyleClass);
		var entryInfoHeader = document.createElement('div');
		$(entryInfoHeader).addClass('entryInfoHeader');		
		var entryInfoBody = document.createElement('div');
		$(entryInfoBody).addClass('entryInfoBody');		
		
		entryInfoHeader.appendChild(dateOfEntry);
		entryInfoHeader.appendChild(timeOfEntry);
		entryInfoHeader.appendChild(nameOfEntry);
		entryInfoHeader.appendChild(typeOfEntry);
		entryInfoBody.appendChild(descriptionOfEntry);
		
		entryInfo.appendChild(entryInfoHeader);
		entryInfo.appendChild(entryInfoBody);
		document.getElementsByTagName('body')[0].appendChild(entryInfo);
		$(entryInfo).addClass(entryInfoStyleClass);		
	}

	//	Sets behaviour on entry list rows
	function setBehaviourOnListRows(){
		$$('div.'+entryListEvenRowStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
				}
				element.onmouseout = function(){
				}
	    	}
	    );	
		$$('div.'+entryListOddRowStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
	    	}
	    );	
	}

	//	Setting behaviour on entries in schedule
	function setBehaviourOnScheduleEntries(){
		$$('a.'+entryInScheduleStyleClass).each(
			function(element) {
				element.onmouseover = function(e) {
				}
				element.onmouseout = function(){
					removeChildren(entryInfo);
				}
    	}
	    );		
		$$('a.entry').each(
			function(element) {
				element.onmouseover = function(e) {
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
						trParent = tdDay.parentNode;
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
							}						
							
						}
						var d2 = new Date();
						var endOfFunction = d2.getTime();
					}
		    	}
		    );
	    }
	}

	//	Displays entry info table
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
			
			var event = new Event(e);
			dragDrop_x = event.page.x/1 + document.body.scrollLeft;
			dragDrop_y = event.client.y/1;
			var clientY = event.client.y/1;
			var pageY = event.page.y/1;
			
			if(!showEntriesAsList){
				if (dragDrop_x > 600){
					dragDrop_x = dragDrop_x - 200;
				}
				$(entryInfo).setStyle('left',dragDrop_x + 5 + 'px');
			}
			else{
				var groupListElement = $('listOfEntries');
				var groupListElementXCoordinate = groupListElement.getPosition().x;
				var groupListElementWidth = groupListElement.getCoordinates().width;
				
				$(entryInfo).setStyle('left',groupListElementXCoordinate +groupListElementWidth +10+ 'px');
			}
			
			$(entryInfo).setStyle('top', element.getCoordinates().top + 'px');	
		}	
	}

	//	Sets behaviour on schedule buttons (previous, next, day...)
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

	//	Returns calendar properties bean
	function getCalendarProperties(){
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getCalendarProperties(scheduleId, {
			callback: function(result) {
				calendarProperties = result;
				getEntries(result.remoteMode, result.server, result.login, result.password, result.calendarAttributes);
			},
			rpcType:dwr.engine.XMLHttpRequest
		});
	}
	
	// Prepares DWR to get remote or local calendar parameters
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
	
	//	Returns calendar parameters from local server
	function getLocalCalendarParameters(groupId){
		showLoadingMessage(loadingMsg);
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.getCalendarParameters(groupId, {
			callback: function(info) {
				displayCalendarAttributes(info);
			},
			rpcType:dwr.engine.XMLHttpRequest
		});
	}
		
	//	Checks if remote server can be reached
	function canUseRemoteCalendar(groupId){
		showLoadingMessage(loadingMsg);
		prepareDwr(CalService, getDefaultDwrPath());
		CalService.canUseRemoteServer(SERVER, {
			callback: function(result) {
				canUseRemoteCalendarCallback(result, groupId);
			}
		});
	}
	
	//	Returns calendar parameters from remote server
	function canUseRemoteCalendarCallback(canUse, groupUniqueId){
		if(canUse){
			prepareDwr(CalService, SERVER + getDefaultDwrPath());
			//	Getting info from remote server
			CalService.getRemoteCalendarParameters(groupUniqueId, LOGIN, PASSWORD, {
				callback: function(info) {
					displayCalendarAttributes(info);
				},
				rpcType:dwr.engine.ScriptTag
			});		
		}
		else{
			//	Cannot use remote server
			closeLoadingMessage();
			alert(serverErrorMessage + ' ' + server);
			return false;
		}
	}
	

	//	Get entries from local server or checks connection with remote server
	function getEntries(isRemoteMode, server, login, password, calendarAttributes){
		if(isRemoteMode == true){
			showLoadingMessage(loadingMsg);
			prepareDwr(CalService, getDefaultDwrPath());
			CalService.canUseRemoteServer(server, {
				callback: function(result) {
					getEntriesCallback(result, server, login, password, calendarAttributes);
				}
			});
		}
		else{
			prepareDwr(CalService, getDefaultDwrPath());
			CalService.getEntries(calendarAttributes, addEntriesToListOrSchedule);		
		}
	}
	
	//	Gets entries from remote server
	function getEntriesCallback(canUse, server, login, password, calendarAttributes) {
		if (canUse) {
			prepareDwr(CalService, server + getDefaultDwrPath());
			CalService.getRemoteEntries(calendarAttributes, login, password, {
				callback: function(data) {
					addEntriesToListOrSchedule(data);
				},
				rpcType:dwr.engine.ScriptTag
			});
		}
		else {
			closeLoadingMessage();
			alert(serverErrorMessage + ' ' + server);
			return false;
		}
	}
	
	//	Sends package of entries to server side
	function addEntriesCallback(result) {
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
			else {
				ScheduleSession.getListOfEntries(scheduleId, setEntries);
			}
		});
	}
	
	//	Entries are displayed as list or sent to server side to get schedule DOM object
	function setEntries(entries) {
		entriesToList = entries;
		if(showEntriesAsList){
			displayEntriesAsList(entries);
		}
		else{
			currentMode = monthMode;
			ScheduleSession.getScheduleDOM(scheduleId, displayEntries);
		}
	}
	
	//	Sends first package of entries to server side
	function addEntriesToListOrSchedule(entries) {
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