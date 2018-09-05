function filter() {
	var input = $("#nameInput").val().toUpperCase();

	$(".listContainer").children("div.row").each(function () {
		var name = $(this).find("#entityName").html().toUpperCase();
		if (name.indexOf(input) > (-1) || input === "") 
			$(this).show();
		else
			$(this).hide();
	});
}