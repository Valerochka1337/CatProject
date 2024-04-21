package org.valerochka1337.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.valerochka1337.entity.Color;
import org.valerochka1337.exceptions.cat.*;
import org.valerochka1337.model.CatModel;

@Service
public interface CatService {
  CatModel createCat(CatModel cat);

  CatModel setCatsOwner(UUID catId, UUID ownerId);

  void removeCat(UUID id);

  CatModel getCatById(UUID id);

  List<CatModel> getAllCats();

  List<CatModel> findAllFriends(UUID id);

  List<CatModel> findCatsByBreed(String breed);

  List<CatModel> findCatsByColor(Color color);

  void friendCats(UUID Cat1Id, UUID Cat2Id);

  void unfriendCats(UUID Cat1Id, UUID Cat2Id);
}
