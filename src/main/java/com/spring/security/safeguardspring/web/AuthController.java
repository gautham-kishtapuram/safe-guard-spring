package com.spring.security.safeguardspring.web;

import com.spring.security.safeguardspring.configuration.JwtUtil;
import com.spring.security.safeguardspring.configuration.MemberDetailsService;
import com.spring.security.safeguardspring.entity.Authority;
import com.spring.security.safeguardspring.entity.Member;
import com.spring.security.safeguardspring.model.PreAuthUserDetails;
import com.spring.security.safeguardspring.model.Role;
import com.spring.security.safeguardspring.model.SignInRequest;
import com.spring.security.safeguardspring.model.SignUpRequest;
import com.spring.security.safeguardspring.service.MemberService;
import com.spring.security.safeguardspring.service.mapper.MemberMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final @NonNull MemberService memberService;
	private final @NonNull MemberMapper memberMapper;
	private final @NonNull MemberDetailsService memberDetailsService;
	private final @NonNull JwtUtil jwtUtil;

	private final @NonNull AuthenticationManager authenticationManager;
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	@PostMapping("/signup")
	ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
		ResponseEntity<String> responseEntity = null;
		try {
			Member memberToSave = memberMapper.signUpModelToMemberEntity(signUpRequest);
			if (!memberService.existByUsername(memberToSave.getUsername())) {
				Member savedMember = memberService.saveMember(memberToSave);
				boolean fetchedMember = memberService.existByUsername(savedMember.getUsername());
				if (fetchedMember) {
					responseEntity = new ResponseEntity<>(savedMember.getFullName(), HttpStatusCode.valueOf(200));
				} else {
					responseEntity = new ResponseEntity<>("User Not Saved", HttpStatusCode.valueOf(500));
				}
			} else {
				responseEntity = new ResponseEntity<>(
						"Username Already Exists, please re-try using different 'username'", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception exception) {
			log.error("Error while signing up {}", exception.getMessage());
			responseEntity = new ResponseEntity<>("Error While Signing up because : " + exception.getMessage(),
					HttpStatusCode.valueOf(500));
		}
		return responseEntity;
	}

	private static boolean isValidRole(SignUpRequest signUpRequest) {
		Set<Role> validRoles = EnumSet.of(Role.ADMIN, Role.USER);
		return signUpRequest.getAuthorities() != null && signUpRequest.getAuthorities()
				.stream()
				.map(Authority::getRole)
				.allMatch(validRoles::contains);
	}

	@PostMapping("/signin-memory")
	ResponseEntity<?> signInOnCache(@RequestBody SignInRequest signInRequest, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseEntity<?> responseEntity = null;
		try {

			Authentication authentication = authentication(signInRequest, request, response, true);

			responseEntity = new ResponseEntity<>(authentication, HttpStatus.OK);
		} catch (Exception exception) {
			log.error("Error {}", exception);
			responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return responseEntity;
	}

	@PostMapping("/signin")
	ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseEntity<?> responseEntity = null;
		try {

			Authentication authentication = authentication(signInRequest, request, response, false);
			PreAuthUserDetails preAuthUserDetails = (PreAuthUserDetails) authentication.getPrincipal();
			return new ResponseEntity<>(jwtUtil.generateAccessToken(preAuthUserDetails), HttpStatus.OK);

		} catch (Exception exception) {
			log.error("Error {}", exception);
			responseEntity = new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
		} return responseEntity;
	}

	private Authentication authentication(SignInRequest signInRequest, HttpServletRequest request,
			HttpServletResponse response, boolean toCacheAuthentication) {
		UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
				signInRequest.getUsername(), signInRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		if (toCacheAuthentication) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(authentication);
			securityContextHolderStrategy.setContext(context);
				securityContextRepository.saveContext(context, request, response);
		}
		return authentication;
	}

}
