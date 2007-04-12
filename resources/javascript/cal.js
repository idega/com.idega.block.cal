var treeUlCounter = 0;

function setLocal(tableId) {
	var table = document.getElementById(tableId);
	table.style.visibility = 'hidden';
}

function setRemote(tableId) {
	var table = document.getElementById(tableId);
	table.style.visibility = 'visible';
}

function sendConnectionData(serverId, loginId, passwordId) {
	var serverName = document.getElementById(serverId);
	var login = document.getElementById(loginId);
	var password = document.getElementById(passwordId);
	CalService.setConnectionData(serverName.value, login.value, password.value, empty());
}

function empty(result){}
