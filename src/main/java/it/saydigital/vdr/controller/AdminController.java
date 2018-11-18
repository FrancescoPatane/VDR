package it.saydigital.vdr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.saydigital.vdr.model.Role;
import it.saydigital.vdr.repository.PrivilegeRepository;
import it.saydigital.vdr.repository.RoleRepository;

@Controller
public class AdminController {
	
	 @Autowired
	 private RoleRepository roleRepository;
	 
	 @Autowired
	 private PrivilegeRepository privilegeRepository;
	
	@GetMapping("/admin/system")
	public String systemAdministration() {
		return "/system-admin";
	}
	
	@GetMapping("/admin/system/roles")
	public String manageRoles(Model model) {
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("roles",roles);
		return "/roles";
	}

}
