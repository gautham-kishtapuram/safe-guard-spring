package com.spring.security.safeguardspring.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenConfiguration {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.expiration}")
	private Long tokenExpiration;

	@Value("${jwt.token.head}")
	private String tokenHead;

	@Value("${jwt.secret}")
	private String secretKey;


	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	public Long getTokenExpiration() {
		return tokenExpiration * 1000;
	}

	public void setTokenExpiration(Long tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

	public String getTokenHead() {
		return tokenHead;
	}

	public void setTokenHead(String tokenHead) {
		this.tokenHead = tokenHead;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
