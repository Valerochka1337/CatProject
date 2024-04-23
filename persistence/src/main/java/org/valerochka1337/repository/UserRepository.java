package org.valerochka1337.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerochka1337.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);
  boolean existsByUsername(String username);
}
