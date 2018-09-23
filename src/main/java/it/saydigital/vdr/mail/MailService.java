package it.saydigital.vdr.mail;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import it.saydigital.vdr.model.MailTemplate;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.MailTemplateRepository;

@Service
public class MailService {

	@Autowired
	private JavaMailSender emailSender;

    @Autowired 
    MailTemplateRepository templateRepository;

//	@Autowired
//	private SpringTemplateEngine engine;

	public void sendMailFullDonwload(String link, User user) {
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setTemplateMode(TemplateMode.HTML);

		SpringStandardDialect dialect = new SpringStandardDialect();
		dialect.setEnableSpringELCompiler(true);

		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setDialect(dialect);
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver);
		
		
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		Context context = new Context();
		context.setVariable("link", link);
		
		MailTemplate template = templateRepository.findByName("fullDownloadTemplate");
        String html = engine.process(template.getBody(), context);

//		helper.setTo(user.getEmail());
//        helper.setText(html, true);
//        helper.setSubject(mail.getSubject());
//        helper.setFrom(mail.getFrom());
		
		
		//		 ITemplateResource t = new  ITemplateResource();
		//		 templateEngine.pr
        
        System.out.println(html);
	}

}
