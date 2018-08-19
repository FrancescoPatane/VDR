package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.User;



public interface  UserRepository extends JpaRepository<User, Long>{

	 User findByEmail(String email);
}
