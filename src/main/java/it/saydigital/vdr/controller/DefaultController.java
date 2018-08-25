package it.saydigital.vdr.controller;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.download.resourceserver.ResourceServer;
import it.saydigital.vdr.download.resourceserver.ResourceServerFactory;
import it.saydigital.vdr.model.Content;
import it.saydigital.vdr.model.ContentLink;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.ContentRepository;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.tree.TreeManager;
import it.saydigital.vdr.watermark.WatermarkPdf;

@Controller
public class DefaultController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContentRepository contentRepository;

	@Autowired
	private MarketingEntityRepository mktRepository;

	@Autowired
	private TreeManager treeManager;

	@Autowired
	ResourceServerFactory serverFactory;

	@GetMapping(value = { "/", "/home" })
	public String home() {
		return "/home";
	}


	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/403")
	public String error403() {
		return "/error/403";
	}

	@GetMapping("/user")
	public String user(Model uiModel) {
		User user = getUser(this.getAuthentication().getName());
		List<MarketingEntity> userEntities = user.getMktEntitiesOrdered();
		Collections.reverse(userEntities);
		uiModel.addAttribute("entities", userEntities);
		return "/user";
	}

	// the name of the entity is passed in the request to not show the ids, 
	// so that users can't guess other entities

	@GetMapping("/detail")
	public String entityDetails(Model uiModel, @RequestParam("entityName") String entityName) throws JsonProcessingException {
		MarketingEntity mktEntity = mktRepository.findByName(entityName);
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
	}


	private User getUser (String email) {
		return userRepository.findByEmail(email);
	}

	@GetMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam("contentId") long contentId) throws IOException, DocumentException {
		String email = this.getAuthentication().getName();
		Optional<Content> optContent = contentRepository.findById(contentId);
		if (optContent.isPresent()) {
			Content content = optContent.get();
			ResourceServer server = serverFactory.createResourceServer(content);
			byte [] bytes = server.serveResource(content, email);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(server.getMimetype()));
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
			return response;
		}else {
			return null;
		}
	}
	
	private Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}
	



}
