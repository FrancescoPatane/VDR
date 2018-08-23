package it.saydigital.vdr.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ContentLink {
	
	@Id
	private String linkId;
	private String filename;
	private String path;
	
	
	
	public ContentLink() {
		super();
	}


	public String getContentId() {
		return linkId;
	}


	public void setContentId(String contentId) {
		this.linkId = contentId;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}
	
	


}
