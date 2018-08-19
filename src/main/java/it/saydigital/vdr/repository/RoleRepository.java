package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String name);

}
