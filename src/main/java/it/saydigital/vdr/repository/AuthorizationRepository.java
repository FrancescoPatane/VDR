package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.saydigital.vdr.model.Authorization;
import it.saydigital.vdr.model.id.AuthorizationId;


public interface AuthorizationRepository extends JpaRepository<Authorization, AuthorizationId>{

}
