package com.spring.security.safeguardspring.service.mapper;

import com.spring.security.safeguardspring.entity.Authority;
import com.spring.security.safeguardspring.entity.Member;
import com.spring.security.safeguardspring.model.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,componentModel = "spring")
public interface MemberMapper {

	@Mapping(target = "fullName", expression = "java(getFullName(signUpRequest))")
	@Mapping(target = "authorities", expression = "java(mapAuthorities(signUpRequest))")
	Member signUpToMemberEntity(SignUpRequest signUpRequest);

	default String getFullName(SignUpRequest signUpRequest) {
		return signUpRequest.getFirstName() + " " + signUpRequest.getLastName();
	}

	default Set<Authority> mapAuthorities(SignUpRequest signUpRequest) {
		return signUpRequest.getAuthorities()
				.stream()
				.map(Authority::getRole)
				.map(Authority::new)
				.collect(Collectors.toSet());
	}
	default List<Authority> mapAuthoritiesOfList(SignUpRequest signUpRequest) {
		return new ArrayList<>();
	}
}
