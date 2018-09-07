package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.Authorization;
import it.saydigital.vdr.model.id.AuthorizationId;


public interface AuthorizationRepository extends JpaRepository<Authorization, AuthorizationId>{
	
	 @Query("SELECT a.AuthorizationId.mktEntityId FROM Authorization a WHERE a.AuthorizationId.userId = ?1") 
	 List<Long> findAuthorizationsForUser(Long userId);

}
