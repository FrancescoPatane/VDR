package it.saydigital.vdr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import it.saydigital.vdr.model.enumeration.DocType;

@Entity
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long originId;
	private Long mktEntityId;
	@Enumerated(EnumType.STRING)
	private DocType docType;
	private String name;
	private Long father;
	@Column(length = 1000)
	private String path;
	
	
	public Document() {
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
	public DocType getDocType() {
		return docType;
	}
	public void setDocType(DocType docType) {
		this.docType = docType;
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
	
	


}
