package it.saydigital.vdr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import it.saydigital.vdr.model.enumeration.ContentType;

@Entity
public class ContentLink {
	
	@Id
	private String linkId;
	@NotNull
	private String filename;
	@Column(length = 500)
	private String path;
	@Enumerated(EnumType.STRING)
	//@Column(columnDefinition = "ENUM('FOLDER', 'DOCUMENT', 'COVER_IMAGE', 'SLIDER_IMAGE', 'IMAGE')")
	private ContentType type;
	
	
	public ContentLink() {
		super();
	}



	public String getLinkId() {
		return linkId;
	}



	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}



	public String getFilename() {
		return filename;
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}


	public ContentType getType() {
		return type;
	}



	public void setType(ContentType type) {
		this.type = type;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}

	
	


	
	


}
