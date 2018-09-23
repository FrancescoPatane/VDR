function selectIconClass(type){
	var iconClass="";
	switch (type) {
	case "FOLDER":
		iconClass="far fa-folder-open fa-2x";
		break;
	case "DOCUMENT":
		iconClass="far fa-file-pdf fa-2x";
		break;
	case "IMAGE":
		iconClass="far fa-file-image fa-2x";
		break;
	case "ARCHIVE":
		iconClass="far fa-file-archive fa-2x";
		break;
	}
	return iconClass;
};


function download(){

	var contentId = $("#selectedNode").val();
	var url = document.location.origin;
	url += "/download?contentId="+contentId;
	window.open(url);
}

function fullDownload(id){
//	var token = $("input[name='_csrf']").val();
//	var header = "X-CSRF-TOKEN";
	var baseUrl = $('head base')[0].href;
	$.ajax({
		type: "GET",
//		beforeSend: function(request) {
//			request.setRequestHeader(header, token);
//		},
		contentType: "application/json",
		url: "/ajax/fullDonwload",
		data: {id:id,baseUrl:baseUrl},
		cache: false,
		timeout: 600000,
		success: function (data) {


		},
		error: function (e) {


		}
	});
}

function setUpView(){
	setUpAnimation();
	setUpTree();
}


function setUpAnimation(){
	var dowloadpanelTop = $("#downloadPanel").offset().top;
	var dowloadpanelBottomTop = $("#downloadPanel").offset().top + $("#downloadPanel").outerHeight(true);
	var startDocsOffset = $('#startDocs').offset().top;
	var endDocsOffset = $('#endDocs').offset().top;
	if (dowloadpanelTop < startDocsOffset || dowloadpanelBottomTop > endDocsOffset) {
		$("#downloadPanel").css("right", "-50%");
	}
	$(window).scroll(function() {
		dowloadpanelTop = $("#downloadPanel").offset().top;
		 dowloadpanelBottomTop = $("#downloadPanel").offset().top + $("#downloadPanel").outerHeight(true);
		 endDocsOffset = $('#endDocs').offset().top;
		 startDocsOffset = $('#startDocs').offset().top;
		if (dowloadpanelTop < startDocsOffset || dowloadpanelBottomTop > endDocsOffset) {
			$("#downloadPanel").css("right", "-50%");
		} else {
			$("#downloadPanel").css("right", "inherit");
		}
	});
}



function setUpTree(){
	$('#docTree').treeview({
		expandIcon : 'far fa-folder fa-2x',
		collapseIcon : 'far fa-folder-open fa-2x',
		showTags : true,
		levels : 0,
		onNodeSelected : function(event, node) {
			$("#downloadPanel h5").html(node.text);
			$("#selectedNode").val(node.id);
			var icon = selectIconClass(node.type);
			$("#download #fileIcon").removeClass();
			$("#download #fileIcon").addClass(icon);
		},
		data : $("#doctree").val()
	});
}