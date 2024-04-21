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
public interface CatModelEntityMapper {
  @Mapping(target = "friendCats", qualifiedByName = "friendModelMapper")
  @Mapping(target = "owner", qualifiedByName = "ownerModelMapper")
  CatModel toModel(Cat entity);

  @Named("friendModelMapper")
  default List<CatModel> mapCatModelFriends(Set<Cat> friends) {
    return CatServiceUtilMapper.mapCatsToModel(friends);
  }

  @Named("ownerModelMapper")
  default OwnerModel mapOwnerModel(Owner owner) {
    if (owner == null) {
      return null;
    }

    OwnerModel ownerModel =
        OwnerModel.builder()
            .id(owner.getId())
            .name(owner.getName())
            .birthDate(owner.getBirthDate())
            .build();

    return ownerModel;
  }

  @Mapping(target = "friendCats", qualifiedByName = "friendEntityMapper")
  Cat toEntity(CatModel model);

  @Named("friendEntityMapper")
  default Set<Cat> mapCatEntityFriends(List<CatModel> friends) {
    return CatServiceUtilMapper.mapCatsToEntity(friends);
  }
}
