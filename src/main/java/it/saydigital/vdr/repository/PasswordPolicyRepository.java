package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.PasswordPolicy;


public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Long>{
	
	PasswordPolicy findByIsActive(boolean isActive);

}
