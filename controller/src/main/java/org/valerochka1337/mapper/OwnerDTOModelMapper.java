package org.valerochka1337.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.dto.OwnerDTO;
import org.valerochka1337.exceptions.cat.InvalidBirthDateCatException;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

@Mapper(componentModel = "spring")
public interface OwnerDTOModelMapper {

  @Mapping(target = "ownedCats", qualifiedByName = "ownedCatsDTOMapper")
  @Mapping(target = "birthDate", source = "model.birthDate", dateFormat = "yyyy-MM-dd")
  OwnerDTO toDTO(OwnerModel model);

  @Named("ownedCatsDTOMapper")
  default List<CatDTO> mapOwnedCatsDTO(List<CatModel> ownedCats) {
    return CatControllerUtilMapper.mapCatsToDTO(ownedCats);
  }

  @Mapping(target = "ownedCats", qualifiedByName = "ownedCatsModelMapper")
  @Mapping(target = "birthDate", qualifiedByName = "StringToLocalDate")
  OwnerModel toModel(OwnerDTO dto);

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

  @Named("ownedCatsModelMapper")
  default List<CatModel> mapOwnedCatsModel(List<CatDTO> ownedCats) {
    return CatControllerUtilMapper.mapCatsToModel(ownedCats);
  }
}
