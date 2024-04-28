package com.spring.security.safeguardspring.service.impl;

import com.spring.security.safeguardspring.entity.Authority;
import com.spring.security.safeguardspring.model.Role;
import com.spring.security.safeguardspring.repository.AuthorityRepository;
import com.spring.security.safeguardspring.service.AuthorityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

	public final @NonNull AuthorityRepository authorityRepository;

	@Override
	public boolean existsByRole(Role role) {
		return authorityRepository.existsByRole(role);
	}

	@Override
	public Authority save(Authority role) {
		return authorityRepository.saveAndFlush(role);
	}
}
