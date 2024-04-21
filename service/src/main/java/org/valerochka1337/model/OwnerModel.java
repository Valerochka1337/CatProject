package org.valerochka1337.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Data
@Builder
public class OwnerModel {
  private UUID id;

  private String name;

  private LocalDate birthDate;

  @ToString.Exclude @EqualsAndHashCode.Exclude private List<CatModel> ownedCats;

  public OwnerModel(UUID id, String name, LocalDate birthDate, List<CatModel> ownedCats) {
    this.id = id;
    this.name = name;
    this.birthDate = birthDate;
    this.ownedCats = ownedCats;
    if (ownedCats == null) {
      this.ownedCats = new ArrayList<>();
    }
  }
}
