package it.saydigital.vdr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.saydigital.vdr.api.MarketingEntityJSON;
import it.saydigital.vdr.api.MktApiImpl;
import it.saydigital.vdr.api.UserApiImpl;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.repository.MarketingEntityRepository;

@RestController
public class RestInterfaceController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MktApiImpl apiImpl;

	@Autowired
	private UserApiImpl usrApiImpl;
	
	

	@GetMapping("/api/testconn")
	public String testConnection() {
		return "VDR Reached!";
	}


	@PostMapping("/api/mktentity")
	public Map<String, Object> createMktEntity(@RequestBody MarketingEntityJSON payload, HttpServletResponse response) {

		log.info("Received webservice call for createMktEntity.");
		
		Map<String, Object> result = apiImpl.createMktEntity(payload);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			} 
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for createMktEntity. Payload: " + payload.toString());
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}
	}

	@DeleteMapping("/api/mktentity/{id}")
	public Map<String, Object> deleteMktEntity(@PathVariable String originId, HttpServletResponse response) {
		
		log.info("Received webservice call for deleteMktEntity.");
		
		Map<String, Object> result = apiImpl.deleteMktEntity(originId);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			} 
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for deleteMktEntity. OriginId: " + originId);
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}
	}


	@PostMapping("/api/authorization")
	public Map<String, Object> authorize(@RequestBody Map<String, String> payload, HttpServletResponse response) {
		
		log.info("Received webservice call for authorize.");
		
		Map<String, Object> result =  apiImpl.createAuthorization(payload);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			}
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for authorize. Payload: " + payload.toString());
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}
	}

	@PostMapping("/api/contentlink")
	public Map<String, Object> linkContent (@RequestBody Map<String, String> payload, HttpServletResponse response) {

		log.info("Received webservice call for linkContent.");
		
		Map<String, Object> result =  apiImpl.linkContent(payload);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			}
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for linkContent. Payload: " + payload.toString());
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}

	}



	@PostMapping("/api/user")
	public Map<String, Object> createUser(@RequestBody Map<String, String> payload, HttpServletResponse response) {

		log.info("Received webservice call for createUser.");
		
		Map<String, Object> result =  usrApiImpl.createUser(payload);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			}
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for createUser. Payload: " + payload.toString());
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}
	}
	
	@PutMapping("/api/user")
	public Map<String, Object> changeUserStatus(@RequestBody Map<String, Object> payload, HttpServletResponse response) {

		log.info("Received webservice call for changeUserStatus.");
		
		Map<String, Object> result =  usrApiImpl.changeUserStatus(payload);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			}
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for changeUserStatus. Payload: " + payload.toString());
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}
	}
	
	@DeleteMapping("/api/user/{email}")
	public Map<String, Object> deleteUser(@PathVariable String email, HttpServletResponse response) {

		log.info("Received webservice call for createUser.");
		
		Map<String, Object> result =  usrApiImpl.deleteUser(email);
		try {
			if (result.containsKey("statusCode")) {
				this.assignSatusCode(response, result);
			}
			return result;
		} catch (Exception e) {
			log.error("Error after webservice call for deleteUser. User mail: " + email);
			e.printStackTrace();
			return this.returnServerError(e.getMessage());
		}
	}
	
	
	private void assignSatusCode (HttpServletResponse response, Map<String, Object> result) {

		Object status =  result.get("statusCode");
		int statusCode = (int) status;
		response.setStatus(statusCode);
	}

	private Map<String, Object> returnServerError(String exceptionMessage){
		Map<String, Object> resultMessage = new HashMap<>();
		resultMessage.put("error", exceptionMessage);
		resultMessage.put("statusCode", 500);
		return resultMessage;
	}



}
