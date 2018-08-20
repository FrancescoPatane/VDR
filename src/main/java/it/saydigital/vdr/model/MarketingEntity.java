package it.saydigital.vdr.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class MarketingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String transactionManager;
    private String tmEmail;
    private Boolean isLocked;
    private String coverImage;
    @ManyToMany
    private Set<User> users = new HashSet<>();
    
    
	public MarketingEntity() {
		super();
	}
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
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
    
    

}
