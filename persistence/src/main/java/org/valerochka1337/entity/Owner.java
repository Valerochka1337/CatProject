package org.valerochka1337.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Data
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "owners",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Owner {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  private Set<Cat> ownedCats;

  public Owner(UUID id, String name, LocalDate birthDate, Set<Cat> ownedCats) {
    this.id = id;
    this.name = name;
    this.birthDate = birthDate;
    this.ownedCats = ownedCats;
    if (ownedCats == null) {
      this.ownedCats = new HashSet<>();
    }
  }
}
