package it.saydigital.vdr.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import it.saydigital.vdr.model.enumeration.ContentType;

@Entity
public class ContentLink {
	
	@Id
	private String linkId;
	private String filename;
	private String uri;
	private Boolean isExternal = false;
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



	public String getUri() {
		return uri;
	}



	public void setUri(String uri) {
		this.uri = uri;
	}



	public Boolean getIsExternal() {
		return isExternal;
	}



	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}



	public ContentType getType() {
		return type;
	}



	public void setType(ContentType type) {
		this.type = type;
	}

	
	


	
	


}
