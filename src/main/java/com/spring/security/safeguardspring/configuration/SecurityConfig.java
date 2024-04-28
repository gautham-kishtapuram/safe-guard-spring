package com.spring.security.safeguardspring.configuration;

import com.spring.security.safeguardspring.configuration.handler.AuthenticationHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final @NonNull MemberDetailsService userDetailsService;
	private final @NonNull AuthEntryPointJwt unauthorizedHandler;
	private final @NonNull AuthenticationHandler authenticationHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
				.formLogin((form) -> form.successHandler(authenticationHandler))
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				//				.securityContext(securityContext -> securityContext.requireExplicitSave(true))
				//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/getAuth")
						.permitAll()
						.requestMatchers("/admin")
						.hasAuthority("ADMIN")
						.requestMatchers("/user")
						.hasAuthority("USER")
						.requestMatchers("/**")
						.permitAll())
				//				.formLogin(Customizer.withDefaults())
				.build();

	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		return auth;
	}

	@Bean // 2
	public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
		ProviderManager providerManager = new ProviderManager(authenticationProvider);
		providerManager.setEraseCredentialsAfterAuthentication(false);
		return providerManager;
	}

	//	@Bean
	//	public HttpSessionEventPublisher httpSessionEventPublisher() {
	//		return new HttpSessionEventPublisher();
	//	}

}

