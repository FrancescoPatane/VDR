function changeLocale(locale){
	$.ajax({
		type: "GET",
//		contentType: "application/json",
		url: "/ajax/changeLocale",
		data: locale,
		cache: false,
		timeout: 600000,
		success: function (data) {
//			alert(data);

		},
		error: function (e) {
			alert(e);

		}
	});
}