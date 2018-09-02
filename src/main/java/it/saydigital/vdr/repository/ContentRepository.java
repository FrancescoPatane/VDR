package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;


public interface ContentRepository extends JpaRepository<Content, Long>{
	
	 @Query("SELECT c FROM Content c  WHERE c.mktEntity.id = ?1 AND c.father = null AND c.type IN ('FOLDER','DOCUMENT')") 
	 List<Content> findRootsByEntityId (Long entityId);
	 
	 List<Content> findByFather (Long fatherId);
	 
	 @Query("SELECT c.content FROM Content c  WHERE c.mktEntity.id = ?1 AND c.type = 'SLIDER_IMAGE'") 
	 List<ContentLink> findSliderImagesByEntityId (Long entityId);

}
