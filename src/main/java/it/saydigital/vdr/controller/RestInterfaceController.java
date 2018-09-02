package it.saydigital.vdr.controller;

import java.util.List;
import java.util.Map;

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
	public String authorize(@RequestBody Map<String, String> payload) {
		return apiImpl.createAuthorization(payload);
	}

}
