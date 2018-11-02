package it.saydigital.vdr.security;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.UserRepository;

public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserRepository userRepository;
	
	@Resource
    private LocaleResolver localeResolver;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email);
		Locale locale;
		String userLocale = user.getLocale();
		if (userLocale != null && !userLocale.isEmpty()) {
			locale = new Locale(userLocale);
			localeResolver.setLocale(request, response, locale);	
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}


}



