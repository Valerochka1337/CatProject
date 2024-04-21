package data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.dto.OwnerDTO;
import org.valerochka1337.entity.Color;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

public class ModelDTOMapperTestData {
  public static OwnerDTO getOwnerDTO() {
    return OwnerDTO.builder()
        .name("Tom")
        .birthDate("2002-02-03")
        .ownedCat(
            CatDTO.builder()
                .id(UUID.fromString("ba7873f7-53b0-4148-b3e8-bbd34ea3923b"))
                .name("Poppy")
                .color("RED")
                .breed("husky")
                .birthDate("2002-02-14")
                .build())
        .build();
  }

  public static OwnerModel getOwnerModel() {
    return OwnerModel.builder()
        .name("Tom")
        .birthDate(LocalDate.parse("2002-02-03"))
        .ownedCats(
            Collections.singletonList(
                CatModel.builder()
                    .id(UUID.fromString("ba7873f7-53b0-4148-b3e8-bbd34ea3923b"))
                    .name("Poppy")
                    .color(Color.RED)
                    .breed("husky")
                    .birthDate(LocalDate.parse("2002-02-14"))
                    .build()))
        .build();
  }

  public static CatDTO getCatDTO() {
    return CatDTO.builder()
        .id(UUID.fromString("1dacfe89-1d1b-4daa-96b2-b1601b43c63d"))
        .name("Nick")
        .color("GREEN")
        .breed("pug")
        .owner(getOwnerDTO())
        .friendCat(
            CatDTO.builder()
                .id(UUID.fromString("ba7873f7-53b0-4148-b3e8-bbd34ea3923b"))
                .name("Poppy")
                .color("RED")
                .breed("husky")
                .owner(getOwnerDTO())
                .birthDate("2002-02-14")
                .build())
        .birthDate("2004-03-04")
        .build();
  }

  public static CatModel getCatModel() {
    return CatModel.builder()
        .id(UUID.fromString("1dacfe89-1d1b-4daa-96b2-b1601b43c63d"))
        .name("Nick")
        .color(Color.GREEN)
        .breed("pug")
        .owner(getOwnerModel())
        .friendCats(
            Collections.singletonList(
                CatModel.builder()
                    .id(UUID.fromString("ba7873f7-53b0-4148-b3e8-bbd34ea3923b"))
                    .name("Poppy")
                    .color(Color.RED)
                    .breed("husky")
                    .owner(getOwnerModel())
                    .birthDate(LocalDate.parse("2002-02-14"))
                    .build()))
        .birthDate(LocalDate.parse("2004-03-04"))
        .build();
  }
}
