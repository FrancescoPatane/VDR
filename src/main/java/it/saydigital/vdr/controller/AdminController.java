package it.saydigital.vdr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.saydigital.vdr.model.Privilege;
import it.saydigital.vdr.model.Role;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.PrivilegeRepository;
import it.saydigital.vdr.repository.RoleRepository;
import it.saydigital.vdr.repository.UserRepository;

@Controller
public class AdminController {
	
	 @Autowired
	 private UserRepository userRepository;
	
	 @Autowired
	 private RoleRepository roleRepository;
	 
	 @Autowired
	 private PrivilegeRepository privilegeRepository;
	
	@GetMapping("/admin/system")
	public String systemAdministration() {
		return "/system-admin";
	}
	
	@GetMapping("/admin/system/user-roles")
	public String manageRoles(Model model) {
		List<Privilege> privileges= privilegeRepository.findAll();
		List<Role> roles = roleRepository.findAll();
		List<User> users =userRepository.findAll();
		model.addAttribute("roles",roles);
		model.addAttribute("users",users);
		model.addAttribute("privileges",privileges);

		return "/user-roles";
	}

}
