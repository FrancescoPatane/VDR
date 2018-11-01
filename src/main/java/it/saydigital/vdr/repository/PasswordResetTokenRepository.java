package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.PasswordResetToken;
import it.saydigital.vdr.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	PasswordResetToken findByToken(String token);


}
