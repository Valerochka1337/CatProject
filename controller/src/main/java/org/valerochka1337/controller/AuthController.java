package org.valerochka1337.controller;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.valerochka1337.dto.RegisterDTO;
import org.valerochka1337.entity.Role;
import org.valerochka1337.entity.User;
import org.valerochka1337.exceptions.NoSuchRoleException;
import org.valerochka1337.exceptions.UsernameTakenUserException;
import org.valerochka1337.mapper.OwnerDTOModelMapper;
import org.valerochka1337.mapper.OwnerModelEntityMapper;
import org.valerochka1337.repository.RoleRepository;
import org.valerochka1337.repository.UserRepository;
import org.valerochka1337.services.OwnerService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final OwnerService ownerService;

  private final OwnerDTOModelMapper ownerDTOModelMapper;
  private final OwnerModelEntityMapper ownerModelEntityMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(
      AuthenticationManager authenticationManager,
      OwnerDTOModelMapper ownerDTOModelMapper,
      OwnerModelEntityMapper ownerModelEntityMapper,
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      OwnerService ownerService) {
    this.authenticationManager = authenticationManager;
    this.ownerDTOModelMapper = ownerDTOModelMapper;
    this.ownerModelEntityMapper = ownerModelEntityMapper;
    this.ownerService = ownerService;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("register")
  public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO)
      throws Exception {
    if (userRepository.existsByUsername(registerDTO.getUsername())) {
      throw new UsernameTakenUserException();
    }

    User user =
        User.builder()
            .username(registerDTO.getUsername())
            .password(passwordEncoder.encode(registerDTO.getPassword()))
            .owner(
                ownerModelEntityMapper.toEntity(
                    ownerService.createOwner(ownerDTOModelMapper.toModel(registerDTO.getOwner()))))
            .build();

    Role roles =
        roleRepository.findByName("USER").orElseThrow(() -> new NoSuchRoleException("USER"));

    user.setRoles(Collections.singletonList(roles));

    userRepository.saveAndFlush(user);

    return new ResponseEntity<>("User created successfully!", HttpStatus.OK);
  }
}
