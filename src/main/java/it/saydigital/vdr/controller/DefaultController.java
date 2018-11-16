package it.saydigital.vdr.controller;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.download.resourceserver.ResourceServer;
import it.saydigital.vdr.download.resourceserver.ResourceServerFactory;
import it.saydigital.vdr.mail.MailService;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.PasswordResetToken;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.security.PermissionChecker;
import it.saydigital.vdr.security.password.PasswordUtilities;
import it.saydigital.vdr.tree.TreeManager;
import it.saydigital.vdr.util.EnvHandler;
import it.saydigital.vdr.util.GeneralHelper;
import it.saydigital.vdr.watermark.Watermarker;

@Controller
public class DefaultController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContentRepository contentRepository;

	@Autowired
	private MarketingEntityRepository mktRepository;

	@Autowired
	private TreeManager treeManager;

	@Autowired
	private ResourceServerFactory serverFactory;

	@Autowired
	private PermissionChecker permChecker;

	@Autowired
	private MailService mailService;

	@Autowired
	private PasswordUtilities pswUtils;



	@GetMapping(value = { "/", "/home" })
	public String home(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(LocaleContextHolder.getLocale());
//		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//        localeResolver.setLocale(request, response, Locale.FRANCE);
//        System.out.println(LocaleContextHolder.getLocale());
		return "/home";
	}

	@GetMapping("/changeLocale/{locale}")
	public String changeLocale(HttpServletRequest request, HttpServletResponse response, @PathVariable String locale) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		System.out.println(LocaleContextHolder.getLocale().toString());
		Locale newLocale =  GeneralHelper.getLoacaleFromLanguage_Country(locale);
		localeResolver.setLocale(request, response, newLocale);		
		return "redirect:/home";
	}

	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/403")
	public String getError403() {
		return "/error/403";
	}

	@GetMapping("/user")
	public String user(Model uiModel) {
		User user = getUser(this.getAuthentication().getName());
		List<MarketingEntity> userEntities = mktRepository.findEntitiesByUserId(user.getId());
		//Collections.reverse(userEntities);
		uiModel.addAttribute("entities", userEntities);
		return "/user";
	}

	// the name of the entity is passed in the request to not show the ids, 
	// so that users can't guess other entities

	@GetMapping("/detail")
	public String getEntityDetailsPage(Model uiModel, @RequestParam("entityName") String entityName) throws JsonProcessingException {
		MarketingEntity mktEntity = mktRepository.findByName(entityName);
		User user = getUser(this.getAuthentication().getName());
		if (permChecker.hasPermissionForObject(user, mktEntity)) {
			long entityId = mktEntity.getId();
			List<ContentLink> sliderImages = contentRepository.findSliderImagesByEntityId(entityId);
			ContentLink firstImage = null;
			if (sliderImages.size()>0) {
				firstImage = sliderImages.get(0);
				sliderImages.remove(0);
			}
			uiModel.addAttribute("entity", mktEntity);
			uiModel.addAttribute("firstImage", firstImage);
			uiModel.addAttribute("sliderImages", sliderImages);
			uiModel.addAttribute("docTree", treeManager.getDocTree(entityId));
			return "/detail";
		}else {
			log.warn("User " + user.getEmail() + " tried to access resource " + mktEntity.getName() + "without  authorization.");
			return "/error/403";
		}
	}


	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam("contentId") long contentId) throws IOException, DocumentException {
		User user = getUser(this.getAuthentication().getName());
		String email = user.getEmail();
		Optional<Content> optContent = contentRepository.findById(contentId);
		if (optContent.isPresent() && permChecker.hasPermissionForObject(user,  optContent.get())) {
			Content content = optContent.get();
			ResourceServer server = serverFactory.createResourceServer(content);
			byte [] bytes = server.serveResource(content, email);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(server.getMimetype()));
			if (content.getType().toString().equalsIgnoreCase("FOLDER")) {
				String filename = content.getName()+".zip";
				headers.add("content-disposition", "attachment; filename=" + filename);
			}
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
			return response;
		}else {
			log.warn("Couldn't find content " + contentId);
			return null;
		}
	}


	@GetMapping("/extDocs/{filename}")
	public ResponseEntity<byte[]> downloadFromLink(@PathVariable String filename)  {

		String externalDocumentsPath = EnvHandler.getProperty("app.external_contents_folder");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/zip"));
		filename +=".zip";
		File downloadFile = new File(externalDocumentsPath+File.separator+filename);
		headers.add("content-disposition", "attachment; filename=" + filename);
		byte[] bytes = {};
		try {
			bytes = Files.readAllBytes(downloadFile.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Couldn't find external downloadable file " + filename);
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.NOT_FOUND);
			return response;
		}
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
		return response;
	}


	@GetMapping("/passRecovery1")
	public String passRecovery1() throws MalformedURLException {
		return "/forgotPassword";
	}

	@PostMapping("/passRecovery2")
	public String passRecovery2(@RequestParam("email") String email, Model uiModel) {
		User user = this.getUser(email);
		if (user == null) {
			uiModel.addAttribute("missingMail", true);
			return "/forgotPassword";
		}else {
			uiModel.addAttribute("userMail", email);
			uiModel.addAttribute("question", user.getSecurityQuestion());
			return "/resetPassword";
		}
	}

	@PostMapping("/passRecovery3")
	public String resetPassword(HttpServletRequest request, @RequestParam("email") String email, @RequestParam("answer") String answer, Model uiModel) throws MessagingException {
		User user = this.getUser(email);
		if (!user.getSecurityAnswer().equalsIgnoreCase(answer.trim())) {
			uiModel.addAttribute("wrongAnswer", true);
			uiModel.addAttribute("userMail", email);
			return "/resetPassword";
		}else {
			String url = this.getBaseUrl(request);
			String token = pswUtils.getPasswordResetToken(user);
			String changePasswordUrl = url+"/changePassword?token="+token+"";
			mailService.sendMailPasswordReset(user, changePasswordUrl);
			uiModel.addAttribute("successfull", true);
			return "/resetPassword";
		}


	}

	@GetMapping("/changePassword")
	public String changePassword(@RequestParam("resetToken") String resetToken, Model uimodel) {
		PasswordResetToken resetTokenObj = pswUtils.getPasswordResetTokenIfValid(resetToken);
		if (resetTokenObj == null) {
			return "403";
		}else {
			uimodel.addAttribute("resetToken", resetTokenObj.getToken());
			return "changePassword";
		}
	}


	private Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}

	private User getUser (String email) {
		return userRepository.findByEmail(email);
	}

	private String getBaseUrl(HttpServletRequest request) {
		String url = "";
		try {
			URL requestURL = new URL(request.getRequestURL().toString());
			url = requestURL.getProtocol() + "://" + requestURL.getHost() + ":" + requestURL.getPort();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}



}
