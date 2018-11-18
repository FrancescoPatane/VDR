package it.saydigital.vdr.security.password;

import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import it.saydigital.vdr.model.PasswordPolicy;
import it.saydigital.vdr.model.PasswordResetToken;
import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.PasswordPolicyRepository;
import it.saydigital.vdr.repository.PasswordResetTokenRepository;
import it.saydigital.vdr.repository.UserRepository;

@Component
public class PasswordUtilities {

	@Autowired
	private BCryptPasswordEncoder pswEncoder;

	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Autowired
	private PasswordPolicyRepository policyRepository;

	@Autowired
	private UserRepository userRepository;

	public String getRandomPassword() {
		int length = 8;
		boolean useLetters = true;
		boolean useNumbers = true;
		String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
		String psw = pswEncoder.encode(generatedString);
		return psw;
	}

	public String getToken() {
		String token = UUID.randomUUID().toString();
		return token;
	}

	/**
	 * If there is a password policy active, this method check if the password is valid 
	 * tested against the password policy.
	 * pswValidator.validate(newPsw) will throw an exception if the psw is not valid, 
	 * with the message containing which constraint was violated.
	 * If no exception is thrown the psw will be changed.
	 * @param newPsw
	 * @param user
	 * @throws InvalidPasswordException
	 */
	public void changePsw(String newPsw, User user) throws InvalidPasswordException {
		PasswordPolicy policy = this.policyRepository.findByIsActive(true);
		if (policy != null) {
			PasswordValidator pswValidator = new PasswordValidator (policy);
			pswValidator.validate(newPsw);
		}
		this.saveNewPassword(newPsw, user);
	}

	private void saveNewPassword(String newPsw, User user) {
		String psw = pswEncoder.encode(newPsw);
		user.setPassword(psw);
		userRepository.save(user);
	}

	public void changePswByResetToken(String newPsw, String token) {
		PasswordResetToken resetToken = tokenRepository.findByToken(token);
		User user = resetToken.getUser();
		String psw = pswEncoder.encode(newPsw);
		user.setPassword(psw);
		userRepository.save(user);
		tokenRepository.delete(resetToken);
	}


	public String getPasswordResetToken(User user) {
		String tokenString = this.getToken();
		PasswordResetToken token = new PasswordResetToken();
		token.setUser(user);
		token.setToken(tokenString);
		LocalDateTime expireDate = LocalDateTime.now().plusMinutes(PasswordResetToken.getExpiration());
		token.setExpirationDate(expireDate);
		token = tokenRepository.save(token);
		return tokenString;
	}

	public PasswordResetToken getPasswordResetTokenIfValid(String token) {
		PasswordResetToken resetToken =  tokenRepository.findByToken(token);
		if (resetToken !=null) {
			if (LocalDateTime.now().isBefore(resetToken.getExpirationDate())) {
				return resetToken;
			}else {
				tokenRepository.delete(resetToken);
				return null;
			}
		}
		else {
			return null;
		}
	}




}