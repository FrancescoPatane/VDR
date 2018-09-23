package it.saydigital.vdr.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresource.ITemplateResource;

@Service
public class MailSender {
	
	 @Autowired
	 private SpringTemplateEngine templateEngine;
	 
	 public void sendMail() {
//		 ITemplateResource t = new  ITemplateResource();
//		 templateEngine.pr
	 }

}
