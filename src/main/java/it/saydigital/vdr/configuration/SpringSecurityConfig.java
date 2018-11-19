package it.saydigital.vdr.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import it.saydigital.vdr.security.MyAuthenticationSuccessHandler;

@Configuration
// http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
// Switch off the Spring Boot security configuration
//@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private AccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
//    @Autowired
//    private MyAuthenticationSuccessHandler MyAuthenticationSuccessHandler;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    };
    
    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler(){
    	MyAuthenticationSuccessHandler successHandler = new MyAuthenticationSuccessHandler();
    	successHandler.setDefaultTargetUrl("/user");
    	return successHandler;
    }
    
//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//    	PasswordEncoder passwordEncoder =
//			    PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder);
//        return authProvider;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider());
//    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	 //http.csrf().ignoringAntMatchers("/api/**");
    	 
//    	 http
//         .authorizeRequests()
//         .antMatchers("/api/**").permitAll().anyRequest().permitAll();
    	 
    	 
//         .authenticated()
//         .anyRequest()
//         .authenticated()
//         .and()
//         .httpBasic();
    	
    	http
    		.authorizeRequests()
    		.antMatchers("/", "/home", "/passRecovery1", "/passRecovery2", "/passRecovery3", "/changePassword", "/changeLocale/**", "/ajaxPublic/**").permitAll()
    		.antMatchers("/admin/system/**", "/ajax/admin/system/**").hasAuthority("SYSTEM_ADMINISTRATION")
            .antMatchers("/admin/vdr/**").hasAnyAuthority("SYSTEM_ADMINISTRATION","VDR_ADMINISTRATION")
            .antMatchers("/api/**").hasAuthority("USE_WEBSERVICE")
            .anyRequest().authenticated()
            .and()
            .csrf().ignoringAntMatchers("/api/**")
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .successHandler(this.myAuthenticationSuccessHandler())
            .and()
            .logout()
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403")//.accessDeniedHandler(accessDeniedHandler)
            ;
    	
    	/*
        http
                .authorizeRequests()
                //.antMatchers("/", "/home", "/passRecovery1", "/passRecovery2", "/passRecovery3", "/changePassword", "/changeLocale/**").permitAll()
                //.antMatchers("/ajaxPublic/**").permitAll().anyRequest().permitAll()
                .antMatchers("/ajax/admin/system/**").hasRole("ADMIN")//.hasAuthority("SYSTEM_ADMINISTRATION")
                //.antMatchers("/api/**").permitAll().anyRequest().permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("SYSTEM_ADMINISTRATION","VDR_ADMINISTRATION")
                .antMatchers("/admin/system/**").hasAuthority("SYSTEM_ADMINISTRATION")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successHandler(this.myAuthenticationSuccessHandler())
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error/403")//.accessDeniedHandler(accessDeniedHandler)
                ;*/
    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("admin").password("password").roles("ADMIN");
//    }

    
    //Spring Boot configured this already.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
    }

}
