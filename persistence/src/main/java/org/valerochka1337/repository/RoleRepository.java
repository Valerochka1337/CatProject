package org.valerochka1337.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerochka1337.entity.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByName(String name);
}
