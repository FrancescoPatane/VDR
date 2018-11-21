package it.saydigital.vdr.security.password;

public class ExpiredPasswordResetTokenException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ExpiredPasswordResetTokenException(String errorMessage) {
		super(errorMessage);
	}
}
