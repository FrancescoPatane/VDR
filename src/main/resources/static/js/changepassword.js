$(document).ready(function () {

	$("#pswFormToken").submit(function (event) {

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
	var resetToken = $("#resetToken").val();
	var params = {}
	params["resetToken"] = resetToken;
	params["newPsw"] = newPsw;

	if (newPsw == newPsw2){

		$("#pswButton").prop("disabled", true);

		$.ajax({
			type: "POST",
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
			},
			contentType: "application/json",
			url: "/ajaxPublic/changePswWithToken",
			data: JSON.stringify(params),
			cache: false,
			timeout: 600000,
			success: function (data) {


				$('#succPsw').show();
				setTimeout(function() {$('#succPsw').hide()}, 3000);
				$("#pswButton").prop("disabled", false);

				setTimeout(function(){
					var baseUrl = $('head base')[0].href;
					window.location.replace(baseUrl+"login");
				}, 3000);


			},
			error: function (e) {


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