package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.Content;


public interface ContentRepository extends JpaRepository<Content, Long>{
	
	 @Query("SELECT c FROM Content c  WHERE c.mktEntityId = ?1 AND c.father = null AND c.type IN ('FOLDER','DOCUMENT')") 
	 List<Content> findRootsByEntityId (Long entityId);
	 
	 List<Content> findByFather (Long fatherId);
	 
	 @Query("SELECT c FROM Content c  WHERE c.mktEntityId = ?1 AND c.type = 'SLIDER_IMAGE'") 
	 List<Content> findSliderImagesByEntityId (Long entityId);

}
