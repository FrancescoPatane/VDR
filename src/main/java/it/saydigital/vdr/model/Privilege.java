package it.saydigital.vdr.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import it.saydigital.vdr.model.pojo.PrivilegePojo;

@Entity
public class Privilege {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(unique=true)
    private String name;
 
    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

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

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public PrivilegePojo getAsPojo() {
		PrivilegePojo pojo = new PrivilegePojo(this.id, this.name);
		return pojo;
	}
	
    
    
}