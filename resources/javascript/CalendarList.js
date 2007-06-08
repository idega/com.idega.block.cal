var arrayOfParameters = null;
var arrayOfCheckedParameters = null;
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
	parentOfList.setAttribute('class','calendarChooserStyleClass');
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
					CalService.setCheckedParameters(arrayOfCheckedParameters, empty);	
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
	function empty(result){
		
	}	
