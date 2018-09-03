package it.saydigital.vdr.security;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordGenerator {
	
	public static String getRandomPassword() {
		int length = 8;
	    boolean useLetters = true;
	    boolean useNumbers = true;
	    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
	 
	    return generatedString;
	}

}
