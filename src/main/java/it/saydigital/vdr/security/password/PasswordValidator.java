package it.saydigital.vdr.security.password;

import it.saydigital.vdr.model.PasswordPolicy;

public class PasswordValidator {
	
	private PasswordPolicy policy;
	
	public PasswordValidator(PasswordPolicy policy) {
		super();
		this.policy = policy;
	}

	public void validate(String password) throws InvalidPasswordException {
		
		if (!this.checkMinLength(password))
			throw new InvalidPasswordException("Password too short");
		
		if (!this.checkInlcudesNumbers(password))
			throw new InvalidPasswordException("Password must include at least a number.");
		
		if (!this.checkInlcudesUpperCase(password))
			throw new InvalidPasswordException("Password must include at least one upper case character.");
		
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

}
