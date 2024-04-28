package com.spring.security.safeguardspring.configuration.handler;


import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

/**
 * An authentication handler that saves an authentication either way.
 *
 * The reason for this is so that the rest of the factors are collected, even if earlier
 * factors failed.
 *
 * @author Josh Cummings
 */
@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler {

	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();


/*
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		Authentication anonymous = new AnonymousAuthenticationToken("key", "anonymousUser",
				AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
		saveMfaAuthentication(request, response, new AuthenticationHandler(anonymous));
	}*/

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		saveMfaAuthentication(request, response, authentication);
	}

	private void saveMfaAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		this.securityContextRepository.saveContext(context, request, response);
		System.out.println("******************************************************************************************************");
		System.out.println("******************************************************************************************************");
		System.out.println("******************************************************************************************************");
		System.out.println("******************************************************************************************************");
		System.out.println("******************************************************************************************************");

	}

}