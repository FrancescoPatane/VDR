package it.saydigital.vdr.security.password;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private PasswordEncoder pswEncoder;

	@Autowired
	private PasswordResetTokenRepository tokenRepository;

	@Autowired
	private PasswordPolicyRepository policyRepository;

	@Autowired
	private UserRepository userRepository;



	public PasswordEncoder getPswEncoder() {
		return pswEncoder;
	}


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

	private PasswordPolicy getPasswordPolicyIfPresent() {
		PasswordPolicy policy = this.policyRepository.findByIsActive(true);
		return policy;
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
		PasswordPolicy policy = this.getPasswordPolicyIfPresent();
		PasswordValidator pswValidator = new PasswordValidator (policy, pswEncoder, user);
		pswValidator.validate(newPsw);
		this.saveNewPassword(newPsw, user);
	}

	private void saveNewPassword(String newPsw, User user) {
		String psw = pswEncoder.encode(newPsw);
		user.setPassword(psw);
		user.setPasswordModifiedDate(LocalDateTime.now());
		userRepository.save(user);
	}

	public void changePswByResetToken(String newPsw, String token) throws ExpiredPasswordResetTokenException, InvalidPasswordException {
		PasswordResetToken resetToken = tokenRepository.findByToken(token);
		if (resetToken.getExpirationDate().isBefore(LocalDateTime.now()))
			throw new ExpiredPasswordResetTokenException("Password reset token expired.");
		User user = resetToken.getUser();
		PasswordPolicy policy = this.getPasswordPolicyIfPresent();
		PasswordValidator pswValidator = new PasswordValidator (policy, pswEncoder, user);
		pswValidator.validate(newPsw);
		String psw = pswEncoder.encode(newPsw);
		user.setPassword(psw);
		user.setPasswordModifiedDate(LocalDateTime.now());
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

	public boolean isPasswordNotEXpired(User user) {
		PasswordPolicy policy = this.getPasswordPolicyIfPresent();
		if (policy != null && policy.getExpiration()) {
			LocalDateTime lastTimePasswordUpdated = user.getPasswordModifiedDate();
			int daysBeforeExpiration = policy.getValidityDays();
			LocalDateTime dateExpired = lastTimePasswordUpdated.plusDays(daysBeforeExpiration);
			return LocalDateTime.now().isBefore(dateExpired);
		}else {
			return true;
		}
	}




}
