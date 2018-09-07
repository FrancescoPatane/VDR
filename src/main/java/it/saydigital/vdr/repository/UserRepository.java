package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.User;



public interface  UserRepository extends JpaRepository<User, Long>{

	 User findByEmail(String email);
	 

}
