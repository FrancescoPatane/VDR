package it.saydigital.vdr.tree;

import java.util.ArrayList;
import java.util.List;

import it.saydigital.vdr.model.Document;


public class DocumentWrapper {
	
	private long id;
	private String docType;
//	private String name;
	private String text;
	private List<DocumentWrapper> nodes = new ArrayList<>();
	
//	public DocumentWrapper(long id, String docType, String name, String text, List<DocumentWrapper> nodes) {
//		super();
//		this.id = id;
//		this.docType = docType;
//		this.name = name;
//		this.text = text;
//		this.nodes = nodes;
//	}
	
	public DocumentWrapper(Document document) {
		this.id = document.getId();
		this.docType = document.getDocType().toString();
//		this.name = name;
		this.text = document.getName();
//		this.nodes = nodes;
	}
	
	

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getDocType() {
		return docType;
	}



	public void setDocType(String docType) {
		this.docType = docType;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public List<DocumentWrapper> getNodes() {
		return nodes;
	}

	public void setNodes(List<DocumentWrapper> nodes) {
		this.nodes = nodes;
	}
	
	
	
	

}
