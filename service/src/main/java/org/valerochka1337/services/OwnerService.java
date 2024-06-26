package org.valerochka1337.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;

@Service
public interface OwnerService {
  OwnerModel createOwner(OwnerModel owner);

  OwnerModel getOwnerById(UUID id);

  List<OwnerModel> getAllOwners();

  void removeOwner(OwnerModel owner);

  List<CatModel> findAllOwnedCats(OwnerModel owner);
}
