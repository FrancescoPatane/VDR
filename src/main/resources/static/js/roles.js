$(document).ready(function () {

	$("#pvlForm").submit(function (event) {

		//stop submit the form, we will post it manually.
		event.preventDefault();

		addPrivileges();

	});

});

function addPrivileges() {

	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	var checked = $("#pvlForm input:checkbox:checked");
	var roleId = $(".list-group-item.list-group-item-action.active.show")[0].id;
	var checkedPrivileges = [];
	for (var i = 0; i<checked.length; i++){
		checkedPrivileges.push(checked[i].value);
	}
	
	var params = {}
	params["checkedPrivileges"] = checkedPrivileges;
	params["roleId"] = roleId;

		$.ajax({
			type: "POST",
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
			},
			contentType: "application/json",
			url: "/ajax/admin/system/addPrivilege",
			data: JSON.stringify(params),
			cache: false,
			timeout: 600000,
			success: function (data) {
				$("#privilegesForRole").html(data);
				$('#privilegeModal').modal('toggle');
			},
			error: function (e) {

			}
		});
}

function getPrivilegesSelection(){
	//pick id of selected role
	var id = $(".list-group-item.list-group-item-action.active.show")[0].id;
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/ajax/admin/system/getPrivilegesSelection",
		data: {id:id},
		dataType: "text",
		cache: false,
		timeout: 600000,
		success: function (data) {
//			alert(data);
			$("#privilegeModal .modal-body").html(data);
		},
		error: function (e) {
			alert(e);

		}
	});
	
	
}

function removePrivilege(privilegeId){
	var roleId = $(".list-group-item.list-group-item-action.active.show")[0].id;
	var token = $("meta[name='_csrf']").attr("content"); 
	var header = $("meta[name='_csrf_header']").attr("content");
	var params = {}
	params["privilegeId"] = privilegeId;
	params["roleId"] = roleId;
	$.ajax({
		type: "DELETE",
		beforeSend: function(request) {
			request.setRequestHeader(header, token);
		},
		contentType: "application/json",
		url: "/ajax/admin/system/removePrivilege",
		data: JSON.stringify(params),
		cache: false,
		timeout: 600000,
		success: function () {
//			alert(data);
			$("#privilege_"+privilegeId).remove();
		},
		error: function (e) {
			alert(e.statusText);

		}
	});
}




function addPrivilege(privilegeId){
	/*var roleId = $(".list-group-item.list-group-item-action.active.show")[0].id;
	var token = $("meta[name='_csrf']").attr("content"); 
	var header = $("meta[name='_csrf_header']").attr("content");
	var params = {}
	params["privilegeId"] = privilegeId;
	params["roleId"] = roleId;
	$.ajax({
		type: "POST",
		beforeSend: function(request) {
			request.setRequestHeader(header, token);
		},
		contentType: "application/json",
		url: "/ajax/admin/system/removePrivilege",
		data: JSON.stringify(params),
		cache: false,
		timeout: 600000,
		success: function () {
//			alert(data);
			$("#privilege_"+privilegeId).remove();
		},
		error: function (e) {
			alert(e.statusText);

		}
	});*/
}