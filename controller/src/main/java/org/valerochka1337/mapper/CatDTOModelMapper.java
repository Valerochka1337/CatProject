package org.valerochka1337.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.valerochka1337.dto.CatDTO;
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
  @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
  CatModel toModel(CatDTO dto);

  @Named("friendModelMapper")
  default List<CatModel> mapCatModelFriends(List<CatDTO> friends) {
    return CatControllerUtilMapper.mapCatsToModel(friends);
  }
}