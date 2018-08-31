package it.saydigital.vdr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestInterfaceController {
	
    @GetMapping("/api/testconn")
    public String testConnection() {
        return "VDR Reached!";
    }
    
    
//    @PostMapping("/rest/user")
//    public String createUser() {
//        return "VDR Reached!";
//    }

}
