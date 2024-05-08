package com.spring.security.safeguardspring.configuration;

import com.spring.security.safeguardspring.model.PreAuthUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final @NonNull MemberDetailsService userDetailsService;
	private final @NonNull JwtTokenConfiguration jwtTokenConfiguration;
	private final @NonNull JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(jwtTokenConfiguration.getTokenHeader());
		if (authHeader != null && authHeader.startsWith(jwtTokenConfiguration.getTokenHead())) {
			final String authToken = authHeader.substring(jwtTokenConfiguration.getTokenHead().length() + 1);
			String username = jwtUtil.getUsernameFromToken(authToken);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				PreAuthUserDetails userDetails = (PreAuthUserDetails) this.userDetailsService.loadUserByUsername(
						username);

				if (jwtUtil.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					logger.info("authenticated user " + username + ", setting security context");
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}
