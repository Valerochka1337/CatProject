package org.valerochka1337.dto;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatDTO {
  private UUID id;
  private String birthDate;
  private String name;
  private String breed;
  private String color;
  private OwnerDTO owner;
  @Singular @EqualsAndHashCode.Exclude private List<CatDTO> friendCats;
}
