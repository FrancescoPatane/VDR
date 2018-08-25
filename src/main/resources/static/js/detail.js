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