package it.saydigital.vdr.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import it.saydigital.vdr.model.comparator.MarketingEntityComparator;

@Entity
@Table(name = "user_")
public class User {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(unique=true)
    @NotNull
    private String email;
    @NotNull
    private String password;
    private boolean enabled;
//    @ManyToMany(mappedBy = "users")
//    private Set<MarketingEntity> mktEntities = new HashSet<>();
    
    @OneToMany(mappedBy="user", orphanRemoval = true)
    private List<Authorization> Authorizations;
 
    @ManyToMany
    @JoinTable( 
        name = "user_role", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Set<Role> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

//	public Set<MarketingEntity> getMktEntities() {
//		return mktEntities;
//	}
//
//	public void setMktEntities(Set<MarketingEntity> mktEntities) {
//		this.mktEntities = mktEntities;
//	}
	
	
    
	public List<MarketingEntity> getMktEntitiesOrdered() {
		List<MarketingEntity> entities = new ArrayList<>(/*this.mktEntities*/);
		Collections.sort(entities, new MarketingEntityComparator());
		return entities;
	}

	public List<Authorization> getAuthorizations() {
		return Authorizations;
	}

	public void setAuthorizations(List<Authorization> authorizations) {
		Authorizations = authorizations;
	}
    
    
    
}