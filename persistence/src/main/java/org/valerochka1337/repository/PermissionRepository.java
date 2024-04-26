package org.valerochka1337.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.valerochka1337.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
  Optional<Permission> findByName(String name);
}
