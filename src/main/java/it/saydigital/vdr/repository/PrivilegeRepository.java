package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.saydigital.vdr.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
	
	
	Privilege findByName(String Name);
	
	 @Query( value="SELECT p.name FROM privilege p WHERE p.id NOT IN (SELECT privilege_id FROM role_privilege WHERE role_id = ?1)",
			 nativeQuery = true) 
	 List<String> findPrivilegesNotInRole (Long roleId);

}
