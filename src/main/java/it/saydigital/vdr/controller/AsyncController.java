package it.saydigital.vdr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import it.saydigital.vdr.async.AsyncService;
import it.saydigital.vdr.async.task.BackgroundTask;
import it.saydigital.vdr.async.task.FullDownloadTask;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.security.PermissionChecker;
import it.saydigital.vdr.security.password.InvalidPasswordException;
import it.saydigital.vdr.security.password.PasswordUtilities;

@RestController
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


	@GetMapping("/ajax/fullDonwload")
	public String fullDonwload(@RequestParam("id") long id, @RequestParam("baseUrl") String baseUrl) throws IOException, DocumentException  {
		String resultMessage = "";
		Optional<MarketingEntity> optContent = mktRepository.findById(id);
		User user = this.getUser(this.getAuthentication().getName());
		if (optContent.isPresent()) {
			if (!asyncService.hasRunningFullDownloadTasks(user.getId(), optContent.get().getId())) {
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

	@PostMapping("/ajax/changePsw")
	public Map<String, Object> changePassword(@Valid @RequestBody String newPsw) {
		Map<String, Object> result = new HashMap<>();
		User user = this.getUser(this.getAuthentication().getName());
		try {
			pswUtils.changePsw(newPsw, user);
			result.put("executed", true);
		} catch (InvalidPasswordException e) {
			result.put("executed", false);
			result.put("message", e.getMessage());
		}
		return result;
	}
	
	@PostMapping("/ajaxPublic/changePswWithToken")
	public void changePasswordWithToken( @RequestBody Map<String, String> params) {
		String token = params.get("resetToken");
		String newPsw = params.get("newPsw");
		pswUtils.changePswByResetToken(newPsw, token);
	}

	@GetMapping("/ajax/checkTasks")
	public String getTasksByUser() {
		User user = this.getUser(this.getAuthentication().getName());
		List<BackgroundTask> tasks = asyncService.getTasksForUser(user.getId());
		StringBuilder sbuilder = new StringBuilder();
		for (BackgroundTask task : tasks) {
			if (task.getName().equalsIgnoreCase("Full Download")) {
				this.writeFDTask(sbuilder, (FullDownloadTask)task);
			}

		}
		return sbuilder.toString();

	}

	private void writeFDTask(StringBuilder sbuilder, FullDownloadTask task) {
		sbuilder.append("<tr><td>");
		sbuilder.append(task.getName() + " " + task.getMktEntity().getName());
		sbuilder.append("</td><td>");
		String progressBarClass = "";
		if (!task.getStatus().toString().equals("RUNNING")) {
			if (task.getStatus().toString().equals("COMPLETED"))
				progressBarClass = "bg-success";
			else
				progressBarClass = "bg-danger";
		}
		sbuilder.append("<div class=\"progress\"><div class=\"progress-bar " + progressBarClass + "\" role=\"progressbar\" style=\"width: ");
		sbuilder.append(task.getCompletePct());
		sbuilder.append("%;\">");
		sbuilder.append(task.getCompletePct());
		sbuilder.append("%</div></div></div></td><td>");
		sbuilder.append(task.getStatus());
		sbuilder.append("</td></tr>");
	}

	private Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}

	private User getUser (String email) {
		return userRepository.findByEmail(email);
	}

}
