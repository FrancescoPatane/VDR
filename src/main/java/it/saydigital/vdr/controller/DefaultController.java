package it.saydigital.vdr.controller;


import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.saydigital.vdr.model.MarketingEntity;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.UserRepository;

@Controller
public class DefaultController {

	
    @Autowired
    private UserRepository userRepository;
    

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
    
    
    private User getUser (String email) {
    	return userRepository.findByEmail(email);
    }

}
