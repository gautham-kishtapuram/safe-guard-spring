package com.spring.security.safeguardspring.configuration;

import com.spring.security.safeguardspring.model.PreAuthUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

	private static final String CLAIM_KEY_USER_ID = "user_id";
	private static final String CLAIM_KEY_ACCOUNT_ENABLED = "enabled";

	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	@Autowired
	private JwtTokenConfiguration jwtTokenConfiguration;

	public String getUserIdFromToken(String token) {
		String userId;
		try {
			final Claims claims = getClaimsFromToken(token);
			userId = claims.get(CLAIM_KEY_USER_ID).toString();
		} catch (Exception e) {
			userId = "";
		}
		return userId;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = claims.getIssuedAt();
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(jwtTokenConfiguration.getSecretKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate(long expiration) {
		return new Date(System.currentTimeMillis() + expiration);
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateAccessToken(PreAuthUserDetails preAuthUserDetails) {
		Map<String, Object> claims = generateClaims(preAuthUserDetails);
		return generateAccessToken(preAuthUserDetails.getUsername(), claims);
	}

	private Map<String, Object> generateClaims(PreAuthUserDetails preAuthUserDetails) {
		Map<String, Object> claims = new HashMap<>(2);
		claims.put(CLAIM_KEY_ACCOUNT_ENABLED, preAuthUserDetails.isActive());
		claims.put(CLAIM_KEY_USER_ID, preAuthUserDetails.getPhoneNumber());
		return claims;
	}

	private String generateAccessToken(String subject, Map<String, Object> claims) {
		return generateToken(subject, claims, jwtTokenConfiguration.getTokenExpiration());
	}

	private String generateToken(String subject, Map<String, Object> claims, long expiration) {
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date())
				.expiration(generateExpirationDate(expiration))
				.compressWith(CompressionCodecs.DEFLATE)
				.signWith(SIGNATURE_ALGORITHM, jwtTokenConfiguration.getSecretKey())
				.compact();
	}

	public Boolean validateToken(String token, PreAuthUserDetails user) {
		final String userName = getUsernameFromToken(token);
		return (userName.equals(user.getUsername()) && !isTokenExpired(token));
	}

}
