package com.spring.security.safeguardspring.service.impl;

import com.spring.security.safeguardspring.entity.Member;
import com.spring.security.safeguardspring.repository.MemberRepository;
import com.spring.security.safeguardspring.service.MemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

	private final @NonNull MemberRepository memberRepository;

	public Member findByUsername(String userName) {
		return memberRepository.findByUsername(userName);
	}

	public boolean existByUsername(String userName) {
		return memberRepository.existsByUsername(userName);
	}

	public Member saveMember(Member member) {
		return memberRepository.save(member);
	}

}
