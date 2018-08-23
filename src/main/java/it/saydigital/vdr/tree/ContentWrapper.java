package it.saydigital.vdr.tree;

import java.util.ArrayList;
import java.util.List;

import it.saydigital.vdr.model.Content;


public class ContentWrapper {
	
	private long id;
	private String type;
//	private String name;
	private String text;
	private List<ContentWrapper> nodes = new ArrayList<>();
	
//	public DocumentWrapper(long id, String docType, String name, String text, List<DocumentWrapper> nodes) {
//		super();
//		this.id = id;
//		this.docType = docType;
//		this.name = name;
//		this.text = text;
//		this.nodes = nodes;
//	}
	
	public ContentWrapper(Content content) {
		this.id = content.getId();
		this.type = content.getType().toString();
//		this.name = name;
		this.text = content.getName();
//		this.nodes = nodes;
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
	
	
	
	

}
