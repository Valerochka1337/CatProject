package org.valerochka1337.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.dto.OwnerDTO;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

@Mapper(componentModel = "spring")
public interface OwnerDTOModelMapper {

  @Mapping(target = "ownedCats", qualifiedByName = "ownedCatsDTOMapper")
  OwnerDTO toDTO(OwnerModel model);

  @Named("ownedCatsDTOMapper")
  default List<CatDTO> mapOwnedCatsDTO(List<CatModel> ownedCats) {
    return CatControllerUtilMapper.mapCatsToDTO(ownedCats);
  }

  @Mapping(target = "ownedCats", qualifiedByName = "ownedCatsModelMapper")
  OwnerModel toModel(OwnerDTO dto);

  @Named("ownedCatsModelMapper")
  default List<CatModel> mapOwnedCatsModel(List<CatDTO> ownedCats) {
    return CatControllerUtilMapper.mapCatsToModel(ownedCats);
  }
}
