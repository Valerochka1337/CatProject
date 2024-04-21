package org.valerochka1337.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.mapstruct.*;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.entity.Color;
import org.valerochka1337.exceptions.cat.InvalidBirthDateCatException;
import org.valerochka1337.exceptions.cat.InvalidColorCatException;
import org.valerochka1337.model.CatModel;

@Mapper(componentModel = "spring")
public interface CatDTOModelMapper {
  @Mapping(target = "friendCats", qualifiedByName = "friendDTOMapper")
  @Mapping(target = "birthDate", source = "model.birthDate", dateFormat = "yyyy-MM-dd")
  CatDTO toDTO(CatModel model);

  @Named("friendDTOMapper")
  default List<CatDTO> mapCatDTOFriends(List<CatModel> friends) {
    return CatControllerUtilMapper.mapCatsToDTO(friends);
  }

  @Mapping(target = "friendCats", qualifiedByName = "friendModelMapper")
  @Mapping(target = "birthDate", qualifiedByName = "StringToLocalDate")
  @Mapping(target = "color", qualifiedByName = "StringToColor")
  CatModel toModel(CatDTO dto);

  @Named("StringToLocalDate")
  default LocalDate mapStringToLocalDate(String date) {
    if (date == null) {
      return null;
    }
    try {
      return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } catch (DateTimeParseException exception) {
      throw new InvalidBirthDateCatException();
    }
  }

  @Named("StringToColor")
  default Color mapStringToColor(String color) {
    if (color == null) {
      return null;
    }
    try {
      return Color.valueOf(color.toUpperCase());
    } catch (IllegalArgumentException exception) {
      throw new InvalidColorCatException();
    }
  }

  @Named("friendModelMapper")
  default List<CatModel> mapCatModelFriends(List<CatDTO> friends) {
    return CatControllerUtilMapper.mapCatsToModel(friends);
  }
}
