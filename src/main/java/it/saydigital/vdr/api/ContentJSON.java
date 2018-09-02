package it.saydigital.vdr.api;

import java.util.List;

public class ContentJSON {
	
	private Long id;
	private String type;
	private String name;
	private String contentId;
    private List<ContentJSON> childs;

	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getContentId() {
		return contentId;
	}


	public void setContentLinkId(String contentId) {
		this.contentId = contentId;
	}


	public List<ContentJSON> getChilds() {
		return childs;
	}


	public void setChilds(List<ContentJSON> childs) {
		this.childs = childs;
	}


	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	
	
	
	

}
