package org.valerochka1337.dto;

import java.util.List;
import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerDTO {
  private UUID id;
  private String name;
  private String birthDate;
  @Singular @EqualsAndHashCode.Exclude private List<CatDTO> ownedCats;
}
