package org.valerochka1337.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.entity.Color;
import org.valerochka1337.exceptions.cat.InvalidColorCatException;
import org.valerochka1337.model.CatModel;

public class CatControllerUtilMapper {
  public static List<CatModel> mapCatsToModel(List<CatDTO> cats) {
    if (cats == null) {
      return null;
    }
    List<CatModel> resultList = new ArrayList<>();
    for (CatDTO cat : cats) {
      CatModel.CatModelBuilder builder = CatModel.builder();
      builder.id(cat.getId());
      builder.name(cat.getName());
      builder.breed(cat.getBreed());
      if (cat.getBirthDate() != null) {
        try {
          builder.birthDate(LocalDate.parse(cat.getBirthDate()));
        } catch (DateTimeParseException exception) {
          throw new InvalidColorCatException();
        }
      }
      if (cat.getColor() != null) {
        try {
          builder.color(Color.valueOf(cat.getColor()));
        } catch (IllegalArgumentException exception) {
          throw new InvalidColorCatException();
        }
      }

      resultList.add(builder.build());
    }
    return resultList;
  }

  public static List<CatDTO> mapCatsToDTO(List<CatModel> cats) {
    if (cats == null) {
      return null;
    }
    List<CatDTO> resultList = new ArrayList<>();
    for (CatModel cat : cats) {
      CatDTO.CatDTOBuilder builder = CatDTO.builder();
      builder.id(cat.getId());
      builder.name(cat.getName());
      if (cat.getBirthDate() != null) {
        builder.birthDate(cat.getBirthDate().toString());
      }
      builder.breed(cat.getBreed());
      if (cat.getColor() != null) {
        builder.color(cat.getColor().toString());
      }

      resultList.add(builder.build());
    }

    return resultList;
  }
}
