package com.spring.security.safeguardspring.configuration;

import com.spring.security.safeguardspring.entity.Authority;
import com.spring.security.safeguardspring.entity.Member;
import com.spring.security.safeguardspring.model.PreAuthUserDetails;
import com.spring.security.safeguardspring.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

	private final @NonNull MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Member member = memberService.findByUsername(userName);

		return new PreAuthUserDetails(member.getFullName(), member.getEmail(), member.getUsername(),
				member.getPassword(), member.getPhoneNumber(), true, member.getAuthorities()
				.stream()
				.map(Authority::getRole)
				.map(Objects::toString)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList()));
	}
}
