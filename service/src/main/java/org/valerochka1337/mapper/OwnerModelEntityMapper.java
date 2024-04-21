package org.valerochka1337.mapper;

import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.valerochka1337.entity.Cat;
import org.valerochka1337.entity.Owner;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

@Mapper(componentModel = "spring")
public interface OwnerModelEntityMapper {

  @Mapping(target = "ownedCats", qualifiedByName = "ownedCatsModelMapper")
  OwnerModel toModel(Owner entity);

  @Named("ownedCatsModelMapper")
  default List<CatModel> mapOwnedCatsDTO(Set<Cat> ownedCats) {
    return CatServiceUtilMapper.mapCatsToModel(ownedCats);
  }

  @Mapping(target = "ownedCats", qualifiedByName = "ownedCatsEntityMapper")
  Owner toEntity(OwnerModel model);

  @Named("ownedCatsEntityMapper")
  default Set<Cat> mapOwnedCatsModel(List<CatModel> ownedCats) {
    return CatServiceUtilMapper.mapCatsToEntity(ownedCats);
  }
}
