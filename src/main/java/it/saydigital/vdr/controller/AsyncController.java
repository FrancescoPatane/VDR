package it.saydigital.vdr.controller;

import java.io.IOException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.async.AsyncService;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.security.PermissionChecker;

@Controller
public class AsyncController {
	
	@Autowired
	private MarketingEntityRepository mktRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PermissionChecker permChecker;
	
	@Autowired
	private AsyncService asyncService;
	
	
	@ResponseBody
	@GetMapping("/ajax/fullDonwload")
	public void fullDonwload(@RequestParam("id") long id, @RequestParam("baseUrl") String baseUrl) throws IOException, DocumentException  {
		

		Optional<MarketingEntity> optContent = mktRepository.findById(id);
		User user = this.getUser(this.getAuthentication().getName());
		
		if (optContent.isPresent() && permChecker.hasPermissionForObject(user,  optContent.get())) {
			asyncService.fullDowload(optContent.get(), user, baseUrl);
		}

		
	}
	
	private Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}
	
	private User getUser (String email) {
		return userRepository.findByEmail(email);
	}

}
