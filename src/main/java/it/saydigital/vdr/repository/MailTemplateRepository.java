package it.saydigital.vdr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.MailTemplate;


public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long>{
	
	MailTemplate findByName(String name);
	
	MailTemplate findByNameAndLocale(String name, String locale);


}
