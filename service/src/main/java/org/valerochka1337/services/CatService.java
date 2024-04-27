package org.valerochka1337.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.valerochka1337.entity.Color;
import org.valerochka1337.exceptions.cat.*;
import org.valerochka1337.model.CatModel;

@Service
public interface CatService {
  CatModel createCat(CatModel cat);

  CatModel setCatsOwner(UUID catId, UUID ownerId) throws AccessDeniedException;

  void removeCat(UUID id) throws AccessDeniedException;

  CatModel getCatById(UUID id) throws AccessDeniedException;

  List<CatModel> getCats(Optional<String> breed, Optional<Color> color, Pageable pageable);

  List<CatModel> findAllFriends(UUID id, Pageable pageable) throws AccessDeniedException;

  void friendCats(UUID Cat1Id, UUID Cat2Id);

  void unfriendCats(UUID Cat1Id, UUID Cat2Id);
}
