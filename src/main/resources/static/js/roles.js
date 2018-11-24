$(document).ready(function () {

	$("#pvlForm").submit(function (event) {

		//stop submit the form, we will post it manually.
		event.preventDefault();

		addPrivileges();

	});
	
	$("#rolesForm").submit(function (event) {

		//stop submit the form, we will post it manually.
		event.preventDefault();

		addRoles();

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
				$('#selectionModal').modal('toggle');
			},
			error: function (e) {
				alert(e.responseText);

			}
		});
}


function addRoles() {

	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	var checked = $("#rolesForm input:checkbox:checked");
	var userId = $(".list-group-item.list-group-item-action.active.show")[0].id.split("-")[1];
	var checkedRoles = [];
	for (var i = 0; i<checked.length; i++){
		checkedRoles.push(checked[i].value);
	}
	
	var params = {}
	params["checkedRoles"] = checkedRoles;
	params["userId"] = userId;

		$.ajax({
			type: "POST",
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
			},
			contentType: "application/json",
			url: "/ajax/admin/system/addRoleToUser",
			data: JSON.stringify(params),
//			dataType: "json",
			cache: false,
			timeout: 600000,
			success: function () {
				getRoles(userId);
				$('#selectionModal').modal('toggle');
			},
			error: function (e) {
				alert(e.responseText);

			}
		});
}

function getPrivilegesSelection(){
	//pick id of selected user
	var id = $(".list-group-item.list-group-item-action.active.show")[0].id.split("-")[1];
	//empty modal from previous selection
	$("#selectionModal .modal-body").html("");
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/ajax/admin/system/getPrivilegesSelectionForRole",
		data: {id:id},
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function (data) {
			
		for (var i = 0; i < data.length; i++){
			var selection = "<div class='form-check'><input class='form-check-input' type='checkbox' id='"+data[i]+"' value='"+data[i]+"'><label class='form-check-label' for='"+data[i]+"'>"+data[i]+"</label></div>"

			$("#selectionModal .modal-body").append(selection);
		}
			
		},
		error: function (e) {
			alert(e.responseText);

		}
	});
	
	
}


function getRolesSelection(){
	//pick id of selected user
	var id = $(".list-group-item.list-group-item-action.active.show")[0].id.split("-")[1];
	//empty modal from previous selection
	$("#selectionModal .modal-body").html("");
	$("#selectionModal .modal-title").html("Select roles for user");

	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/ajax/admin/system/getRolesSelectionForUser",
		data: {id:id},
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function (data) {
			
		for (var i = 0; i < data.length; i++){
			var selection = "<div class='form-check'><input class='form-check-input' type='checkbox' id='"+data[i]+"' value='"+data[i]+"'><label class='form-check-label' for='"+data[i]+"'>"+data[i]+"</label></div>"

			$("#selectionModal .modal-body").append(selection);
		}
			
		},
		error: function (e) {
			alert(e.responseText);

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

function removeRole(id){
	var roleName = id.split("-")[1];
	var userId = id.split("-")[0];
	var token = $("meta[name='_csrf']").attr("content"); 
	var header = $("meta[name='_csrf_header']").attr("content");
	var params = {}
	params["userId"] = userId;
	params["roleName"] = roleName;
	$.ajax({
		type: "DELETE",
		beforeSend: function(request) {
			request.setRequestHeader(header, token);
		},
		contentType: "application/json",
		url: "/ajax/admin/system/removeRole",
		data: JSON.stringify(params),
		cache: false,
		timeout: 600000,
		success: function () {
//			alert(data);
			getRoles(userId)
		},
		error: function (e) {
			alert(e.statusText);

		}
	});
}

function getRoles(userId){
	console.log(userId);
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/ajax/admin/system/getRolesByUser",
		data: {userId:userId},
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function (data) {
			var roleContainer = "#roleContainer"+userId;
			var html = "<div class='row'>";
			for (var i = 0; i<data.length; i++){
				var id = userId+"-"+data[i];
				var name = data[i].replace("ROLE_", "");
				html = html.concat("<span id=\""+id+"\"><i class=\"fas fa-times fa-1x\" onclick=\"removeRole('"+id+"')\"></i>"+name+"</span>");
				if ((i+1) % 4 == 0){
					html = html.concat("</div><div class='row'>");
				}
			}
			$(roleContainer).html(html.concat("</div>"));
		},
		error: function (e) {
			alert(e);

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