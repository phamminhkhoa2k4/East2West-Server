package com.east2west.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.east2west.models.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private int userId;
  private String username;
  private String firstname;
  private String lastname;
  private String email;
  private String phone;
  private String address;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  // Constructor
  public UserDetailsImpl(int userId, String username, String firstname, String lastname, String email, 
                         String phone, String address, String password,
                         Collection<? extends GrantedAuthority> authorities) {
      this.userId = userId;
      this.username = username;
      this.firstname = firstname;
      this.lastname = lastname;
      this.email = email;
      this.phone = phone;
      this.address = address;
      this.password = password;
      this.authorities = authorities;
  }

  // Static method to build UserDetailsImpl from User entity
  public static UserDetailsImpl build(User user) {
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRoles().getRoleName().toString());


      return new UserDetailsImpl(
          user.getUserId(), 
          user.getUsername(), 
          user.getFirstname(), 
          user.getLastname(),
          user.getEmail(),
          user.getPhone(),
          user.getAddress(),
          user.getPassword(), 
          Collections.singletonList(authority)
      );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
  }

  public int getUserId() {
      return userId;
  }

  public String getFirstname() {
      return firstname;
  }

  public String getLastname() {
      return lastname;
  }

  public String getEmail() {
      return email;
  }

  public String getPhone() {
      return phone;
  }

  public String getAddress() {
      return address;
  }

  @Override
  public String getPassword() {
      return password;
  }

  @Override
  public String getUsername() {
      return username;
  }

  @Override
  public boolean isAccountNonExpired() {
      return true;
  }

  @Override
  public boolean isAccountNonLocked() {
      return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
      return true;
  }

  @Override
  public boolean isEnabled() {
      return true;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o)
          return true;
      if (o == null || getClass() != o.getClass())
          return false;
      UserDetailsImpl user = (UserDetailsImpl) o;
      return Objects.equals(userId, user.userId);
  }

  @Override
  public int hashCode() {
      return Objects.hash(userId);
  }
}