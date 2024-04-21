package org.valerochka1337.services;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerochka1337.entity.Owner;
import org.valerochka1337.exceptions.owner.*;
import org.valerochka1337.mapper.CatModelEntityMapper;
import org.valerochka1337.mapper.OwnerModelEntityMapper;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;
import org.valerochka1337.repository.CatRepository;
import org.valerochka1337.repository.OwnerRepository;

@Service
public class OwnerServiceImpl implements OwnerService {
  private final OwnerRepository ownerRepo;
  private final CatRepository catRepo;

  private final OwnerModelEntityMapper ownerMapper;

  private final CatModelEntityMapper catMapper;

  @Autowired
  public OwnerServiceImpl(
      OwnerRepository ownerRepo,
      CatRepository catRepo,
      OwnerModelEntityMapper ownerMapper,
      CatModelEntityMapper catMapper) {
    this.ownerRepo = ownerRepo;
    this.catRepo = catRepo;
    this.ownerMapper = ownerMapper;
    this.catMapper = catMapper;
  }

  @Override
  public OwnerModel createOwner(OwnerModel owner) {
    validateOwner(owner);

    if (owner.getId() != null && ownerRepo.existsById(owner.getId())) {
      throw new IllegalArgumentException("Owner with such id already exists");
    }

    owner
        .getOwnedCats()
        .forEach(
            (c) -> {
              if (!catRepo.existsById(c.getId())) {
                throw new IllegalArgumentException("No such owned cat");
              }
            });

    Owner ownerEntity = ownerMapper.toEntity(owner);
    ownerEntity = ownerRepo.saveAndFlush(ownerEntity);

    return ownerMapper.toModel(ownerEntity);
  }

  @Override
  public void removeOwner(OwnerModel owner) {
    Owner ownerEntity =
        ownerRepo
            .findById(owner.getId())
            .orElseThrow(() -> new NoSuchOwnerException(owner.getId()));

    ownerRepo.delete(ownerEntity);
  }

  @Override
  public OwnerModel getOwnerById(UUID id) {
    return ownerMapper.toModel(
        ownerRepo.findById(id).orElseThrow(() -> new NoSuchOwnerException(id)));
  }

  @Override
  public List<OwnerModel> getAllOwners() {
    return ownerRepo.findAll().stream().map(ownerMapper::toModel).collect(Collectors.toList());
  }

  @Override
  public List<CatModel> findAllOwnedCats(OwnerModel owner) {
    return ownerRepo
        .findById(owner.getId())
        .orElseThrow(() -> new NoSuchOwnerException(owner.getId()))
        .getOwnedCats()
        .stream()
        .map(catMapper::toModel)
        .toList();
  }

  private void validateOwner(OwnerModel ownerModel) {
    if (ownerModel == null) {
      throw new NullOwnerException();
    }

    if (ownerModel.getName() == null || ownerModel.getName().isEmpty()) {
      throw new NoNameOwnerException();
    }

    if (ownerModel.getBirthDate() == null) {
      throw new NoBirthDateOwnerException();
    }

    for (CatModel c : ownerModel.getOwnedCats()) {
      if (!catRepo.existsById(c.getId())) {
        throw new NoSuchOwnedCatOwnerException();
      }
    }
  }
}
