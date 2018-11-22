package it.saydigital.vdr.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvHandler {
	
	@Autowired
    private Environment environment;
	
	private static Environment environmentObject;
	
	@PostConstruct //to fill the static field with the autowired field not accessible with static
	public void initStaticField() {
		EnvHandler.environmentObject = environment;
	}
	
	
	public static String getProperty(String property) {
		return environmentObject.getProperty(property);
	}

}
