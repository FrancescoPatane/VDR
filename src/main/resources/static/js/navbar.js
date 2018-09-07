function getImageOfFish(fishId){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		type: "POST",
		beforeSend: function(request) {
			request.setRequestHeader(header, token);
		},
		contentType: "application/json",
		url: "/ajax/changePsw",
		data: { newPsw:newPsw},
		dataType: 'text',
		cache: false,
		timeout: 600000,
		success: function (data) {
//			$("#media-"+fishId).find("img").attr("src",data);
		},
		error: function (e) {
		}
	});
}