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
//	var token = $("meta[name='_csrf']").attr("content");
//	var header = $("meta[name='_csrf_header']").attr("content");
//	var contentId = $("#selectedNode").val();
//	$.ajax({
//		type: "GET",
//		beforeSend: function(request) {
//			request.setRequestHeader(header, token);
//		},
//		contentType: "application/json",
//		url: "/download",
//		data: { contentId:contentId},
//		dataType: 'text',
//		cache: false,
////		success: function (data) {
////			$("#media-"+fishId).find("img").attr("src",data);
////
////		},
//		error: function (e) {
//
//		}
//	});
	var contentId = $("#selectedNode").val();
	var url = document.location.origin;//$("#downloadLink")[0].href;
	url += "/download?contentId="+contentId;
//	$("#downloadLink")[0].href = url;
//	$("#downloadLink")[0].click;
	window.open(url);
}