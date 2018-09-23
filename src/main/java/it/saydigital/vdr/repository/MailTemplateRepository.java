package it.saydigital.vdr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.saydigital.vdr.model.MailTemplate;


public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long>{

}
