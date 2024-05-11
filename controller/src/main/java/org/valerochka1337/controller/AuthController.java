package org.valerochka1337.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.valerochka1337.jwt.JwtTokenProvider;
import org.valerochka1337.dto.AuthDTO;
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
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;
  private final OwnerService ownerService;

  private final OwnerDTOModelMapper ownerDTOModelMapper;
  private final OwnerModelEntityMapper ownerModelEntityMapper;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(
      AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
      OwnerDTOModelMapper ownerDTOModelMapper,
      OwnerModelEntityMapper ownerModelEntityMapper,
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      OwnerService ownerService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.ownerDTOModelMapper = ownerDTOModelMapper;
    this.ownerModelEntityMapper = ownerModelEntityMapper;
    this.ownerService = ownerService;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("register")
  @PreAuthorize("hasAuthority('users:write')")
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

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthDTO authRequest) {
    try{
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
      User user =
          userRepository
              .findByUsername(authRequest.getUsername())
              .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

      String token = jwtTokenProvider.createToken(authRequest.getUsername());
      Map<Object, Object> response = new HashMap<>();
      response.put("username", authRequest.getUsername());
      response.put("token", token);
      
      return ResponseEntity.ok(response);
    } catch (AuthenticationException exception) {
      return new ResponseEntity<>("Invalid email/password", HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request, response, null);
  }
}
