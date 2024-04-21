package org.valerochka1337.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.valerochka1337.entity.Cat;
import org.valerochka1337.entity.Color;

public interface CatRepository extends JpaRepository<Cat, UUID> {

  boolean existsById(UUID id);

  List<Cat> findCatsByBreed(String breed);

  List<Cat> findCatsByColor(Color color);
}
