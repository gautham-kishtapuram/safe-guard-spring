package com.spring.security.safeguardspring.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final @NonNull MemberDetailsService userDetailsService;
	private final @NonNull AuthorizationExceptionHandler unauthorizedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/getAuth")
						.authenticated()
						.requestMatchers("/admin")
						.hasAuthority("ADMIN")
						.requestMatchers("/user")
						.hasAuthority("USER")
						.requestMatchers("/**")
						.permitAll())
				.build();

	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		return auth;
	}

	@Bean
	public AuthenticationManager authenticationManager(
			@Qualifier("daoAuthenticationProvider") AuthenticationProvider authenticationProvider) {
		ProviderManager providerManager = new ProviderManager(authenticationProvider);
//		providerManager.setEraseCredentialsAfterAuthentication(false);
		return providerManager;
	}

}

