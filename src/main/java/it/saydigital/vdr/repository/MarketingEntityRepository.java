package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.MarketingEntity;


public interface MarketingEntityRepository extends JpaRepository<MarketingEntity, Long>{
	
	MarketingEntity findByName(String Name);

}
