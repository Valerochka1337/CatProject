package org.valerochka1337.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.valerochka1337.entity.Permission;
import org.valerochka1337.entity.Role;
import org.valerochka1337.entity.User;
import org.valerochka1337.repository.UserRepository;

@Service
public class CatUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public CatUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user with such username"));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .authorities(getAuthorities(user.getRoles()))
        .build();
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

    return getGrantedAuthorities(getPermissions(roles));
  }

  private List<String> getPermissions(Collection<Role> roles) {

    List<String> permissions = new ArrayList<>();
    List<Permission> collection = new ArrayList<>();
    for (Role role : roles) {
      collection.addAll(role.getPermissions());
      permissions.add("ROLE_" + role.getName());
    }
    for (Permission item : collection) {
      permissions.add(item.getName());
    }
    return permissions;
  }

  private List<? extends GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (String permission : permissions) {
      authorities.add(new SimpleGrantedAuthority(permission));
    }
    return authorities;
  }
}
