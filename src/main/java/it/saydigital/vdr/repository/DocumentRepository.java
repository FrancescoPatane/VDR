package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.Document;


public interface DocumentRepository extends JpaRepository<Document, Long>{
	
	 @Query("SELECT d FROM Document d  WHERE d.mktEntityId = ?1 AND d.father = null") 
	 List<Document> findRootsByEntityId (Long entityId);
	 
	 List<Document> findByFather (Long fatherId);

}
