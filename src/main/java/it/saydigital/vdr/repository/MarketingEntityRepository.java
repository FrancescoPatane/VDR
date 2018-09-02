package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.MarketingEntity;


public interface MarketingEntityRepository extends JpaRepository<MarketingEntity, Long>{
	
	MarketingEntity findByName(String Name);
	
	MarketingEntity findByOriginId(String originId);
	
	 @Query("SELECT e FROM MarketingEntity e  WHERE e.id IN (SELECT a.mktEntity.id FROM Authorization a WHERE a.user.id = ?1) ") 
	 List<MarketingEntity> findEntitiesByUserId (Long userId);

	

}
