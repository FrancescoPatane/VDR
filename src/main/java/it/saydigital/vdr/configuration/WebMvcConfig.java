package it.saydigital.vdr.configuration;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import it.saydigital.vdr.util.EnvHandler;
import it.saydigital.vdr.util.GeneralHelper;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		String defaultLocale = EnvHandler.getProperty("app.default_locale");
		if (defaultLocale != null && !defaultLocale.isEmpty()) {
			Locale locale = GeneralHelper.getLoacaleFromLanguage_Country(defaultLocale);
			if (LocaleUtils.isAvailableLocale(locale))
				slr.setDefaultLocale(locale);
			else
				slr.setDefaultLocale(Locale.US);	
		}else{
			slr.setDefaultLocale(Locale.US);
		}
		return slr;
	}

	//    @Bean
	//    public LocaleChangeInterceptor localeChangeInterceptor() {
	//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	//        lci.setParamName("lang");
	//        return lci;
	//    }

	//    @Override
	//    public void addInterceptors(InterceptorRegistry registry) {
	//        registry.addInterceptor(localeChangeInterceptor());
	//    }

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource
		= new ReloadableResourceBundleMessageSource();

		messageSource.setBasenames("classpath:messages/messages", "classpath:org/springframework/security/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}