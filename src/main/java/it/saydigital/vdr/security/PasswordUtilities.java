package it.saydigital.vdr.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.UserRepository;

@Component
public class PasswordUtilities {
	
	@Autowired
	private BCryptPasswordEncoder pswEncoder;
	
	public String getRandomPassword() {
		int length = 8;
	    boolean useLetters = true;
	    boolean useNumbers = true;
	    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
	    String psw = pswEncoder.encode(generatedString);
	    return psw;
	}
	
	
	public void changePsw(String newPsw, User user, UserRepository userRepository) {
	    String psw = pswEncoder.encode(newPsw);
	    user.setPassword(psw);
	    userRepository.save(user);
	}
	
	

}
