package it.saydigital.vdr.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.saydigital.vdr.model.Role;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.RoleRepository;
import it.saydigital.vdr.repository.UserRepository;
import it.saydigital.vdr.security.password.PasswordUtilities;

@Service
public class UserApiImpl {

	@Autowired
	private UserRepository usrRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordUtilities pswUtils;



	public Map<String, Object> createUser(Map<String, String> payload) {
		Map<String, Object> resultMessage = new HashMap<>();
		String email = payload.get("email");
		String roleName = payload.get("role");
		User user = usrRepository.findByEmail(email);
		if (user != null) {
			resultMessage.put("error", "The email address in input: " + email + "is already in use.");
			resultMessage.put("statusCode", 400);
		}else {
			Role role = roleRepository.findByName(roleName);
			if (role == null) {
				resultMessage.put("error", "Could not find any role matching the role in input: " + roleName);
				resultMessage.put("statusCode", 400);
			}else {
				String encodedPswd = pswUtils.getRandomPassword();
				user = new User(email, encodedPswd);
				user.getRoles().add(role);
				usrRepository.save(user);
				resultMessage.put("success", "User created.");
			}
		}
		return resultMessage;

	}
	
	
	public Map<String, Object> changeUserStatus(Map<String, Object> payload) {
		Map<String, Object> resultMessage = new HashMap<>();
		String email = (String) payload.get("email");
		boolean active = (boolean) payload.get("active");
		User user = usrRepository.findByEmail(email);
		if (user != null) {
			user.setEnabled(active);
			usrRepository.save(user);
			resultMessage.put("success", "User created.");
		}else {
			resultMessage.put("error", "Could not find any user matching the email address in input.");
			resultMessage.put("statusCode", 404);
		}
		return resultMessage;
	}
	
	public Map<String, Object> deleteUser(String email) {
		Map<String, Object> resultMessage = new HashMap<>();
		User user = usrRepository.findByEmail(email);
		if (user != null) {
			usrRepository.delete(user);
			resultMessage.put("success", "User deleted.");
		}else {
			resultMessage.put("error", "Could not find any user matching the email address in input.");
			resultMessage.put("statusCode", 404);
		}
		return resultMessage;
	}

}
