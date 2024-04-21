package org.valerochka1337.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.valerochka1337.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {

  boolean existsById(UUID id);
}
