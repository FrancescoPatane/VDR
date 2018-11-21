package it.saydigital.vdr.security.password;

import org.springframework.security.crypto.password.PasswordEncoder;

import it.saydigital.vdr.model.PasswordPolicy;
import it.saydigital.vdr.model.User;

public class PasswordValidator {
	
	private PasswordPolicy policy;
	
	private PasswordEncoder encoder;
	
	private User user;
	
	public PasswordValidator(PasswordPolicy policy, PasswordEncoder encoder, User user) {
		super();
		this.policy = policy;
		this.encoder = encoder;
		this.user = user;
	}

	public void validate(String password) throws InvalidPasswordException {
		
		if (policy != null) {
		if (policy.getMinLength() != null && !this.checkMinLength(password))
			throw new InvalidPasswordException("Password too short");
		
		if (policy.getIncludeNumbers() != null && !this.checkInlcudesNumbers(password))
			throw new InvalidPasswordException("Password must include at least a number.");
		
		if (policy.getIncludeUpperCase() != null && !this.checkInlcudesUpperCase(password))
			throw new InvalidPasswordException("Password must include at least one upper case character.");
		}
		
		if (!this.checkNotSameAsPrevious(password))
			throw new InvalidPasswordException("Password must be different from the last one.");
		
	}
	
	private boolean checkMinLength(String password) {
		return password.length()>=this.policy.getMinLength();
	}
	
	private boolean checkInlcudesNumbers(String password) {
		return password.matches(".*[0-9].*");
	}
	
	private boolean checkInlcudesUpperCase(String password) {
		return password.matches(".*[A-Z].*");
	}
	
	private boolean checkNotSameAsPrevious(String password) {
		return !this.encoder.matches(password, this.user.getPassword());
	}

}
