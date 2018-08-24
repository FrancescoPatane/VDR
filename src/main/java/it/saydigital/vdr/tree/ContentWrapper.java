package it.saydigital.vdr.tree;

import java.util.ArrayList;
import java.util.List;

import it.saydigital.vdr.model.Content;


public class ContentWrapper {

	private long id;
	private String type;
	private String icon = null;
	private String text;
	private List<ContentWrapper> nodes = new ArrayList<>();



	public ContentWrapper(Content content) {
		this.id = content.getId();
		this.type = content.getType().toString();
		this.text = content.getName();
		this.assignIcon(content.getType().toString());
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public List<ContentWrapper> getNodes() {
		return nodes;
	}

	public void setNodes(List<ContentWrapper> nodes) {
		this.nodes = nodes;
	}
	
	

	public String getIcon() {
		return icon;
	}



	public void setIcon(String icon) {
		this.icon = icon;
	}



	private void assignIcon (String type) {
		String iconName = "";
		switch (type) {
		case "DOCUMENT":  
			iconName = "far fa-file-pdf";
			break;
		case "IMAGE":  
			iconName = "far fa-file-image";
			break;
		case "ARCHIVE":  
			iconName = "far fa-file-archive";
			break;
		}
		this.icon = iconName+" fa-2x";
	}



}
