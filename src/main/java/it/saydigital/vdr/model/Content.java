package it.saydigital.vdr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import it.saydigital.vdr.model.enumeration.ContentType;

@Entity
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long originId;
	private Long mktEntityId;
	@Enumerated(EnumType.STRING)
	//@Column(columnDefinition = "ENUM('FOLDER', 'DOCUMENT', 'COVER_IMAGE', 'SLIDER_IMAGE', 'IMAGE')")
	private ContentType type;
	private String name;
	private Long father;
	@Column(length = 1000)
	private String path;
	@ManyToOne
	@JoinColumn(name="link_id")
	private ContentLink content;
	
	
	public Content() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOriginId() {
		return originId;
	}
	public void setOriginId(Long originId) {
		this.originId = originId;
	}
	public Long getMktEntityId() {
		return mktEntityId;
	}
	public void setMktEntityId(Long mktEntityId) {
		this.mktEntityId = mktEntityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getFather() {
		return father;
	}
	public void setFather(Long father) {
		this.father = father;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public ContentLink getContent() {
		return content;
	}
	public void setContent(ContentLink content) {
		this.content = content;
	}
	public ContentType getType() {
		return type;
	}
	public void setType(ContentType type) {
		this.type = type;
	}
	
	


}
