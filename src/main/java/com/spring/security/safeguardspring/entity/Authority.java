package com.spring.security.safeguardspring.entity;

import com.spring.security.safeguardspring.model.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "authority")
public class Authority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Role role;

  public Authority() {
  }

  public Authority(Role role) {
    this.role = role;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Role getRole() {
    return role;
  }

  public Authority setRole(Role role) {
    this.role = role;
    return this;
  }
}
