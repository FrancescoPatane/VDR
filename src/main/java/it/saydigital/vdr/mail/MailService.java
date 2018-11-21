package it.saydigital.vdr.mail;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import it.saydigital.vdr.model.MailTemplate;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.MailTemplateRepository;
import it.saydigital.vdr.util.EnvHandler;
import it.saydigital.vdr.util.GeneralHelper;

@Service
public class MailService {
	
	//todo log
	
	@Value("${app.mail_from_address}")
	private String from;

	@Autowired
	private JavaMailSender emailSender;

    @Autowired 
    MailTemplateRepository templateRepository;

//	public void sendMailFullDonwload(String link, User user, String tmAddress) throws MessagingException {
//		StringTemplateResolver templateResolver = new StringTemplateResolver();
//		templateResolver.setTemplateMode(TemplateMode.HTML);
//
//		SpringStandardDialect dialect = new SpringStandardDialect();
//		dialect.setEnableSpringELCompiler(true);
//
//		SpringTemplateEngine engine = new SpringTemplateEngine();
//		engine.setDialect(dialect);
//		engine.setEnableSpringELCompiler(true);
//		engine.setTemplateResolver(templateResolver);
//		
//		
//		MimeMessage message = emailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//		
//		Context context = new Context();
//		context.setVariable("link", link);
//		
//		MailTemplate template = templateRepository.findByName("fullDownloadTemplate");
//        String html = engine.process(template.getBody(), context);
//
//		helper.setTo(tmAddress);
//        helper.setText(html, true); //true means it is html
//        helper.setSubject(template.getSubject());
//        helper.setFrom("no-reply@vdr.com");
//        emailSender.send(message);
//		
//	}
	
	public void sendMailFullDonwload(String link, User user, String tmAddress) throws MessagingException {
		String locale;
		if (user.getLocale()!=null)
			locale = user.getLocale();
		else;
			locale = Locale.US.toString();
		String to = user.getEmail();
		MailTemplate template = templateRepository.findByNameAndLocale("fullDownloadTemplate", locale);
		Context context = new Context();
		context.setVariable("link", link);
		this.sendMail(template, tmAddress, context);
	}
	
	public void sendMailPasswordReset(User user, String resetUrl) throws MessagingException {
		String locale;
		if (user.getLocale()!=null)
			locale = user.getLocale();
		else;
			locale = Locale.US.toString();
		String to = user.getEmail();
		MailTemplate template = templateRepository.findByNameAndLocale("resetPasswordTemplate", locale);
		Context context = new Context();
		context.setVariable("resetUrl", resetUrl);
		this.sendMail(template, to, context);
	}
	
	public void sendMailAuthorized() {
		
	}
	
	private void sendMail(MailTemplate template, String to, Context context) throws MessagingException {
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
		
//		Context context = new Context();
//		context.setVariable("resetUrl", resetUrl);
		
        String html = engine.process(template.getBody(), context);

		helper.setTo(to);
        helper.setText(html, true); //true means it is html
        helper.setSubject(template.getSubject());
        helper.setFrom(from);
        emailSender.send(message);
	}
	

}
