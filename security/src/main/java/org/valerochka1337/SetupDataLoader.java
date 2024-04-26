package org.valerochka1337;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.valerochka1337.entity.Permission;
import org.valerochka1337.entity.Role;
import org.valerochka1337.entity.User;
import org.valerochka1337.repository.PermissionRepository;
import org.valerochka1337.repository.RoleRepository;
import org.valerochka1337.repository.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final PasswordEncoder passwordEncoder;
  boolean alreadySetup = false;

  @Value("${spring.security.user.name}")
  private String defaultAdminName;

  @Value("${spring.security.user.password}")
  private String defaultAdminPassword;

  @Autowired
  public SetupDataLoader(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PermissionRepository permissionRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

    if (alreadySetup) return;
    List<Permission> adminPermissions =
        Arrays.asList(
            createPermissionIfNotFound("cats:read"),
            createPermissionIfNotFound("cats:write"),
            createPermissionIfNotFound("owners:read"),
            createPermissionIfNotFound("owners:write"),
            createPermissionIfNotFound("users:write"),
            createPermissionIfNotFound("users:read"));

    List<Permission> userPermissions =
        Arrays.asList(
            createPermissionIfNotFound("cats:read"),
            createPermissionIfNotFound("cats:write"),
            createPermissionIfNotFound("owners:read"),
            createPermissionIfNotFound("users:read"));

    createRoleIfNotFound("ADMIN", adminPermissions);
    createRoleIfNotFound("USER", userPermissions);

    Role adminRole = roleRepository.findByName("ADMIN").get();
    User user =
        User.builder()
            .username(defaultAdminName)
            .password(passwordEncoder.encode(defaultAdminPassword))
            .roles(List.of(adminRole))
            .build();

    if (userRepository.existsByUsername(user.getUsername())) return;
    userRepository.saveAndFlush(user);

    alreadySetup = true;
  }

  @Transactional
  Permission createPermissionIfNotFound(String name) {

    Permission permission = permissionRepository.findByName(name).orElse(null);
    if (permission == null) {
      permission = new Permission(name);
      permissionRepository.saveAndFlush(permission);
    }
    return permission;
  }

  @Transactional
  Role createRoleIfNotFound(String name, Collection<Permission> permissions) {

    Role role = roleRepository.findByName(name).orElse(null);
    if (role == null) {
      role = new Role(name);
      role.setPermissions(permissions);
      roleRepository.saveAndFlush(role);
    }
    return role;
  }
}
