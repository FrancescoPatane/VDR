package it.saydigital.vdr.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.saydigital.vdr.api.MarketingEntityJSON;
import it.saydigital.vdr.api.MktApiImpl;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.repository.MarketingEntityRepository;

@RestController
public class RestInterfaceController {

	@Autowired
	private MarketingEntityRepository mktRepository;

	@Autowired
	private MktApiImpl apiImpl;

	@GetMapping("/api/testconn")
	public String testConnection() {
		return "VDR Reached!";
	}

	@GetMapping("/api/mktentity/{id}")
	public MarketingEntity getMketEntity(@PathVariable Long id) {
		return mktRepository.findById(id).get();
	}

	@PostMapping("/api/mktentity")
	public List<String> createMktEntity(@RequestBody MarketingEntityJSON payload) {
		return apiImpl.createMktEntity(payload);
	}

	@DeleteMapping("/api/mktentity/{id}")
	public void deleteMktEntity(@PathVariable Long id) {
		apiImpl.deleteMktEntity(id);
	}


	@PostMapping("/api/authorization")
	public Map<String, Object> authorize(@RequestBody Map<String, String> payload, HttpServletResponse response) {
		Map<String, Object> result =  apiImpl.createAuthorization(payload);
		if (result.containsKey("statusCode")) {
			this.assignSatusCode(response, result);
		}
		return result;
	}

	@PostMapping("/api/linkcontent")
	public Map<String, Object> linkContent (@RequestBody Map<String, String> payload, HttpServletResponse response) {
		Map<String, Object> result =  apiImpl.linkContent(payload);
		if (result.containsKey("statusCode")) {
			this.assignSatusCode(response, result);
		}
		return result;
	}

	private void assignSatusCode (HttpServletResponse response, Map<String, Object> result) {

		Object status =  result.get("statusCode");
		if (status instanceof Integer) {
			int statusCode = (int) status;
			response.setStatus(statusCode);
		}
	}




}
