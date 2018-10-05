package it.saydigital.vdr.controller;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.async.AsyncService;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.security.PasswordUtilities;
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
	
	@Autowired
	private PasswordUtilities pswUtils;


	@ResponseBody
	@GetMapping("/ajax/fullDonwload")
	public String fullDonwload(@RequestParam("id") long id, @RequestParam("baseUrl") String baseUrl) throws IOException, DocumentException  {
		String resultMessage = "";
		Optional<MarketingEntity> optContent = mktRepository.findById(id);
		User user = this.getUser(this.getAuthentication().getName());
		if (optContent.isPresent()) {
			if (!asyncService.hasRunningTasks(user.getId(), optContent.get().getId())) {
				if (permChecker.hasPermissionForObject(user,  optContent.get())) {
					asyncService.fullDowload(optContent.get(), user, baseUrl);
					resultMessage = "Download requested. You will receive an email with a download link once the resource you requested has been created.";
				}else {
					resultMessage = "You are not authorized to download this package.";
				}
			}else {
				resultMessage = "You have already requeted the download of this package.";
			}
		}else {
			resultMessage = "Error: impossible to require download. Please contact an administrator.";
		}
		return resultMessage;
	}
	
	@ResponseBody
	@PostMapping("/ajax/changePsw")
	public void changePassword(@Valid @RequestBody String newPsw) {
		User user = this.getUser(this.getAuthentication().getName());
		pswUtils.changePsw(newPsw, user, userRepository);
	}

	private Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}

	private User getUser (String email) {
		return userRepository.findByEmail(email);
	}

}
