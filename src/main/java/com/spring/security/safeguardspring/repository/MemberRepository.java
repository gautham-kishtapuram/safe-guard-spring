package com.spring.security.safeguardspring.repository;

import com.spring.security.safeguardspring.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByUsername(String username);

	boolean existsByUsername(String username);
}
