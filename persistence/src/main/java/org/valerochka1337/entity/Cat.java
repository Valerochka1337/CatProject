package org.valerochka1337.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cats")
public class Cat {
  @Id @GeneratedValue private UUID id;

  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "breed")
  private String breed;

  @Enumerated(EnumType.STRING)
  @Column(name = "color")
  private Color color;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner_id")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private Owner owner;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "cat_friendship",
      joinColumns = {@JoinColumn(name = "cat1_id")},
      inverseJoinColumns = {@JoinColumn(name = "cat2_id")})
  @OnDelete(action = OnDeleteAction.CASCADE)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<Cat> friendCats;

  public Cat(
      UUID id,
      LocalDate birthDate,
      String name,
      String breed,
      Color color,
      Owner owner,
      Set<Cat> friendCats) {
    this.id = id;
    this.birthDate = birthDate;
    this.name = name;
    this.breed = breed;
    this.color = color;
    this.owner = owner;
    this.friendCats = friendCats;
    if (friendCats == null) {
      this.friendCats = new HashSet<>();
    }
  }
}
