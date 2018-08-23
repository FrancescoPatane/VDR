package it.saydigital.vdr.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class MarketingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 500)
    private String description;
    private String transactionManager;
    private String company;
    private String tmEmail;
    private Boolean isLocked;
    @ManyToMany
    @JoinTable( 
        joinColumns = @JoinColumn(
          name = "mkt_entity_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id")) 
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy="mktEntityId")
    private List<Document> documents;
    
    
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

	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	public String getCoverImageName () {
		if (this.company != null && this.company.length()>0)
			return "/icons/"+this.company.replaceAll("[^a-zA-Z]| +","")+".png".toLowerCase();
		else
			return "/img/defaultcover.png";
	}
    

}
