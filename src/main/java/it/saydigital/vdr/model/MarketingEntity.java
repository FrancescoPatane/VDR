package it.saydigital.vdr.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.DatatypeConverter;

import it.saydigital.vdr.util.EnvHandler;

@Entity
public class MarketingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	private String originId;
	@Column(unique=true)
	private String name;
	@Column(length = 500)
	private String description;
	private String transactionManager;
	private String company;
	private String tmEmail;
	private LocalDateTime creationDate;
	private Boolean isLocked;
	@OneToMany(mappedBy="mktEntity", orphanRemoval = true)
	private List<Content> contents;
	@OneToMany(mappedBy="mktEntity", orphanRemoval = true)
	private List<Authorization> Authorizations;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransactionManager() {
		return transactionManager;
	}
	public void setTransactionManager(String transactionManager) {
		this.transactionManager = transactionManager;
	}
	public String getTmEmail() {
		return tmEmail;
	}
	public void setTmEmail(String tmEmail) {
		this.tmEmail = tmEmail;
	}
	public Boolean getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	//	public Set<User> getUsers() {
	//		return users;
	//	}
	//	public void setUsers(Set<User> users) {
	//		this.users = users;
	//	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public List<Content> getContents() {
		return contents;
	}
	public void setContents(List<Content> contents) {
		this.contents = contents;
	}


	public String getOriginId() {
		return originId;
	}
	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getCompanyImage() {
		String base64 = null;
		if (this.company != null && this.company.length()>0) {
			String resourcePath = EnvHandler.getProperty("app.company_icons_folder")+File.separator+this.getCompanyImageName();
			try {
				base64 = DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(resourcePath)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return base64;
	}


	private String getCompanyImageName () {
		if (this.company != null && this.company.length()>0)
			return this.company.replaceAll("[^a-zA-Z]| +","")+".png".toLowerCase();
		else
			return "";
	}

	public ContentLink  getCoverLink() {
		for (Content content: this.contents) {
			if (content.getType().toString().equals("COVER_IMAGE"))
				return content.getContent();
		}
		return null;
	}

	public int getNumberOfAssets() {
		int c = 0;
		for (Content content : contents) {
			if (content.getType().toString().equals("FOLDER") && content.getFather() == null)
				c++;
		}
		return c;
	}


}
