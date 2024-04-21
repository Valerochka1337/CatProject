package org.valerochka1337.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.valerochka1337.entity.Color;

@Data
@Builder
public class CatModel {
  private UUID id;

  private LocalDate birthDate;

  private String name;

  private String breed;

  private Color color;

  private OwnerModel owner;

  @ToString.Exclude @EqualsAndHashCode.Exclude private List<CatModel> friendCats;

  public CatModel(
      UUID id,
      LocalDate birthDate,
      String name,
      String breed,
      Color color,
      OwnerModel owner,
      List<CatModel> friendCats) {
    this.id = id;
    this.birthDate = birthDate;
    this.name = name;
    this.breed = breed;
    this.color = color;
    this.owner = owner;
    this.friendCats = friendCats;
    if (friendCats == null) {
      this.friendCats = new ArrayList<>();
    }
  }
}
