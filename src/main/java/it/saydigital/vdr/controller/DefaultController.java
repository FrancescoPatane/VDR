package it.saydigital.vdr.controller;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.DocumentRepository;
import it.saydigital.vdr.repository.MarketingEntityRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.tree.TreeManager;

@Controller
public class DefaultController {

	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DocumentRepository docRepository;
    
    @Autowired
    private MarketingEntityRepository mktRepository;
    
    @Autowired
    private TreeManager treeManager;
    

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
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = getUser(auth.getName());
    	List<MarketingEntity> userEntities = user.getMktEntitiesOrdered();
    	Collections.reverse(userEntities);
    	uiModel.addAttribute("entities", userEntities);
        return "/user";
    }
    
    // the name of the entity is passed in the request to not show the ids, 
    // so that users can't guess other entities
    
    @GetMapping("/detail")
    public String entityDetails(Model uiModel, @RequestParam("entityName") String entityName) throws JsonProcessingException {
    	Long entityId = mktRepository.findByName(entityName).getId();
    	System.out.println(docRepository.findRootsByEntityId(entityId).get(0).getName());
    	
    	System.out.println(docRepository.findByFather(2l).get(0).getName());
    	
//    	Map<String, Object> son = new HashMap<String, Object>();
//    	son.put("text", "son");
//    	Map<String, Object> json = new HashMap<String, Object>();
//    	json.put("text", "NODO1");
//    	json.put("nodes", son);
//    	ObjectMapper mapper = new ObjectMapper();
    	uiModel.addAttribute("json", treeManager.getDocTree(entityId));
        return "/detail";
    }
    
    
    private User getUser (String email) {
    	return userRepository.findByEmail(email);
    }

}
