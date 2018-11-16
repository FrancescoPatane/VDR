$(document).ready(function () {

	$("#pswForm").submit(function (event) {

		//stop submit the form, we will post it manually.
		event.preventDefault();

		fire_ajax_submit();

	});

});

function fire_ajax_submit() {

	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";

	var newPsw = $("#psw1").val();
	var newPsw2 = $("#psw2").val();

	if (newPsw == newPsw2){

		$("#pswButton").prop("disabled", true);

		$.ajax({
			type: "POST",
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
			},
			contentType: "application/json",
			url: "/ajax/changePsw",
			data: newPsw,
			//dataType: 'json',
			cache: false,
			timeout: 600000,
			success: function (data) {

//				var json = "<h4>Ajax Response</h4><pre>"
//				+ JSON.stringify(data, null, 4) + "</pre>";
				
				if (data.executed){
					$('#errPsw').hide();
					$('#succPsw').show();
					setTimeout(function() {$('#succPsw').hide()}, 3000);
				}else{
					$('#errPsw').html(data.message);
					$('#errPsw').show();
				}
				$("#pswButton").prop("disabled", false);
				
				

			},
			error: function (e) {

//				var json = "<h4>Ajax Response</h4><pre>"
//				+ e.responseText + "</pre>";

				$('#errPsw').show();
				setTimeout(function() {$('#errPsw').hide()}, 3000);
				$("#pswButton").prop("disabled", false);

			}
		});
	}else{
		$('#diffPsw').show();
		setTimeout(function() {$('#diffPsw').hide()}, 3000);
	}
}


$('#tasksModal').on('shown.bs.modal', function () {
		setTimeout(updateProgress,1000);
//	updateProgress();

})




function updateProgress (){
//		var token = $("input[name='_csrf']").val();
//		var header = "X-CSRF-TOKEN";
//		var baseUrl = $('head base')[0].href;
		$.ajax({
			type: "GET",
//			beforeSend: function(request) {
//				request.setRequestHeader(header, token);
//			},
			contentType: "application/json",
			url: "/ajax/checkTasks",
//			data: {id:id,baseUrl:baseUrl},
			cache: false,
			timeout: 600000,
			success: function (data) {
				$('#tasksTable').html(data);
			},
			error: function (e) {
				alert(e);

			}
		});
		if ($('#tasksModal').is(':visible')){
			setTimeout(updateProgress,1000);
		}else{
			$('#tasksTable').html("");
			return
		}

}

