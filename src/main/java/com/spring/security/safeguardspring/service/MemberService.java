package com.spring.security.safeguardspring.service;

import com.spring.security.safeguardspring.entity.Member;

public interface MemberService {

	Member findByUsername(String username);

	Member saveMember(Member username);

	boolean existByUsername(String name);
}