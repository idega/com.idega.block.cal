var treeUlCounter = 0;
var DEFAULT_DWR_PATH = '/dwr';
var calendar_list_container_id = null;
function setLocal(tableId) {
	var table = document.getElementById(tableId);
	table.style.display = 'none';
}

function setRemote(tableId) {
	var table = document.getElementById(tableId);
	table.style.display = 'inline';
}

function sendConnectionData(serverId, loginId, passwordId) {
	var serverName = document.getElementById(serverId);
	var login = document.getElementById(loginId); 
	var password = document.getElementById(passwordId);
	CalService.setConnectionData(serverName.value, login.value, password.value, empty());
}

function empty(result){}

//	behaviour

function setBehaviour(){
	
	var myrules = {
/*		
		'b.someclass' : function(element){
			element.onclick = function(){
				alert(this.innerHTML);
			}
		},
*/ 
		'#radioBtnLocal input' : function(element){
			element.onclick = function(){
				setLocal('connectionData');
			}
		}
		,
		'#radioBtnRemote input' : function(element){
			element.onclick = function(){
				setRemote('connectionData');
			}
		}
		
	};
	
	Behaviour.register(myrules);	
}

function loadLocalCalendarList(id) {
	SERVER = null;
	LOGIN = null;
	PASSWORD = null;

	calendar_list_container_id = id;
/*
	prepareDwr(CalService, DEFAULT_DWR_PATH);
	CalService.getCalendarParametersList({
		callback: function(calendars) {
			if (calendars == null) {
//				closeLoadingMessage();
				return false;
			}
			setCalendars(calendars, id);
		}
	});
	*/
/*	
	GroupService.getTopGroupNodes({
		callback: function(groups) {
			if (groups == null) {
				closeLoadingMessage();
				return false;
			}
			setNodes(groups, id);
		}
	});
*/
}

function prepareDwr(interfaceClass, path) {
	//	Preparing DWR
	dwr.engine._defaultPath = path;
	interfaceClass._path = path;
	DWREngine.setMethod(DWREngine.ScriptTag);
}
