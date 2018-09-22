package it.saydigital.vdr.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FilenameUtils;

import it.saydigital.vdr.model.enumeration.ContentType;
import it.saydigital.vdr.util.EnvHandler;

@Entity
public class ContentLink {
	
	@Id
	private String linkId;
	private String filename;
	@Column(length = 500)
	private String path;
	@Enumerated(EnumType.STRING)
	//@Column(columnDefinition = "ENUM('FOLDER', 'DOCUMENT', 'COVER_IMAGE', 'SLIDER_IMAGE', 'IMAGE')")
	private ContentType type;
	@OneToMany(mappedBy="content")
    private List<Content> contents;
	
	

	public ContentLink() {
		super();
	}



	public ContentLink(String linkId, String filename, String path, ContentType type) {
		super();
		this.linkId = linkId;
		this.filename = filename;
		this.path = path;
		this.type = type;
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



	public List<Content> getContents() {
		return contents;
	}



	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
	
	

	public String getBase64 () {
		String base64 = "";
		String resourcePath = this.path;
		if (resourcePath == null || resourcePath.length()<=0)
			resourcePath = EnvHandler.getProperty("app.content_images_folder")+File.separator+this.getFilename();
		try {
			base64 = DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(resourcePath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64;
	}
	
	public String getExtension() {
		return FilenameUtils.getExtension(this.filename);
	}
	


	
	


}
