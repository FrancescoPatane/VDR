package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String name);
	
	
	 @Query( value="SELECT r.name FROM role r  WHERE r.id IN (SELECT ur.role_id FROM user_role ur WHERE ur.user_id = ?1)",
			 nativeQuery = true) 
	 List<String> findRolesByUserId (Long userId);

}
