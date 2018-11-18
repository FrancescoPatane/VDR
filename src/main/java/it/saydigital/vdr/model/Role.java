package it.saydigital.vdr.model;

import java.util.ArrayList;
import java.util.Collection;
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

import it.saydigital.vdr.model.pojo.PrivilegePojo;

@Entity
public class Role {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(unique=true)
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
 
    @ManyToMany
    @JoinTable(
        name = "role_privilege", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;
    
    

	public Role() {
		super();
	}

	public Role(String name) {
		super();
		this.name = name;
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

	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}   
	
	public String getRoleNameWithoutPrefix() {
		return this.name.replaceAll("^ROLE_", "");
	}
	
	public List<PrivilegePojo> getPrivilegesAsPojos(){
		List<PrivilegePojo> pojos = new ArrayList<>();
		for (Privilege privilege : this.privileges) {
			PrivilegePojo pojo = privilege.getAsPojo();
			pojos.add(pojo);
		}
		return pojos;
	}
    
    
}