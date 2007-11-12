var treeUlCounter = 0;
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
function loadLocalCalendarList(id) {
	SERVER = null;
	LOGIN = null;
	PASSWORD = null;

	calendar_list_container_id = id;
}

function loadLocalCalendarList(id) {
	SERVER = null;
	LOGIN = null;
	PASSWORD = null;

	calendar_list_container_id = id;
}