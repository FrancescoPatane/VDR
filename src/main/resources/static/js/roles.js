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
//		data: userId,
		dataType: "json",
		cache: false,
		timeout: 600000,
		success: function (data) {
			var roleContainer = "#roleContainer"+userId;
//			var html = "<span><i class='fas fa-times fa-1x' onclick='removeRole('"+ data[0] +"')'></i>"+data[0]+"asdasd</span>"
//			$("#roleContainer").append(html);
			var html = "<div class='row'>";
			for (var i = 0; i<data.length; i++){
				var id = userId+"-"+data[i];
				var name = data[i].replace("ROLE_", "");
				html = html.concat("<span id=\""+id+"\"><i class=\"fas fa-times fa-1x\" onclick=\"removeRole('"+id+"')\"></i>"+name+"</span>");
//				$("#roleContainer").append(html);
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