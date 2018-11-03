package it.saydigital.vdr.util;

import java.util.Locale;

public class GeneralHelper {
	
	public static Locale getLoacaleFromLanguage_Country(String code) {
		String[] split = code.split("_");
		return new Locale(split[0], split[1]);
	}

}
