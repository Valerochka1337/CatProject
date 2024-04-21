package org.valerochka1337.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.valerochka1337.entity.Cat;
import org.valerochka1337.model.CatModel;

public class CatServiceUtilMapper {
  public static List<CatModel> mapCatsToModel(Set<Cat> cats) {
    if (cats == null) {
      return null;
    }
    List<CatModel> resultList = new ArrayList<>();
    for (Cat cat : cats) {
      CatModel newFriend =
          CatModel.builder()
              .id(cat.getId())
              .name(cat.getName())
              .birthDate(cat.getBirthDate())
              .color(cat.getColor())
              .breed(cat.getBreed())
              .build();

      resultList.add(newFriend);
    }
    return resultList;
  }

  public static Set<Cat> mapCatsToEntity(List<CatModel> cats) {
    if (cats == null) {
      return null;
    }
    Set<Cat> resultList = new HashSet<>();
    for (CatModel cat : cats) {
      Cat newFriend =
          Cat.builder()
              .id(cat.getId())
              .name(cat.getName())
              .birthDate(cat.getBirthDate())
              .color(cat.getColor())
              .breed(cat.getBreed())
              .build();
      resultList.add(newFriend);
    }
    return resultList;
  }
}
