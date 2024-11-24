package com.spring.security.safeguardspring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This gets invoked, when the user is not fully authorized and tries to access Endpoints beyond his authorized scope
 */
@Component
public class AuthorizationExceptionHandler implements AuthenticationEntryPoint {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationExceptionHandler.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", authException.getMessage());
		body.put("path", request.getServletPath());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), body);

		logger.error("Unauthorized error: {}", authException.getMessage());
	}

}
