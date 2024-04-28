package com.spring.security.safeguardspring.service;

import com.spring.security.safeguardspring.entity.Authority;
import com.spring.security.safeguardspring.entity.Member;
import com.spring.security.safeguardspring.model.Role;

public interface AuthorityService {

	boolean existsByRole(Role role);
	Authority save(Authority role);
}