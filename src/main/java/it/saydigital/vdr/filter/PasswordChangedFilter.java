package it.saydigital.vdr.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.saydigital.vdr.model.User;
import it.saydigital.vdr.repository.UserRepository;

@Component
public class PasswordChangedFilter implements Filter{

	@Autowired
	private UserRepository userRepository;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		Principal principal = req.getUserPrincipal();
		boolean toRedirect = false;
		String path = req.getRequestURI();
		if (principal != null && !(path.equals("/user/changePassword")||path.equals("/ajax/changePsw"))) {
			User user = userRepository.findByEmail(principal.getName());
			toRedirect = user.getPasswordModifiedDate() == null;
		}
		if (toRedirect)
			resp.sendRedirect("/user/changePassword");
		else
			chain.doFilter(req, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
