package com.spring.security.safeguardspring.repository;

import com.spring.security.safeguardspring.entity.Authority;
import com.spring.security.safeguardspring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	boolean existsByRole(Role role);
}
