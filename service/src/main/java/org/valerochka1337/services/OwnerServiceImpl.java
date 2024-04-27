package org.valerochka1337.services;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.valerochka1337.entity.Owner;
import org.valerochka1337.entity.Role;
import org.valerochka1337.entity.User;
import org.valerochka1337.exceptions.owner.*;
import org.valerochka1337.mapper.CatModelEntityMapper;
import org.valerochka1337.mapper.OwnerModelEntityMapper;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.model.OwnerModel;
import org.valerochka1337.repository.CatRepository;
import org.valerochka1337.repository.OwnerRepository;
import org.valerochka1337.repository.UserRepository;

@Service
public class OwnerServiceImpl implements OwnerService {
  private final UserRepository userRepo;
  private final OwnerRepository ownerRepo;
  private final CatRepository catRepo;

  private final OwnerModelEntityMapper ownerMapper;

  private final CatModelEntityMapper catMapper;

  @Autowired
  public OwnerServiceImpl(
      UserRepository userRepo,
      OwnerRepository ownerRepo,
      CatRepository catRepo,
      OwnerModelEntityMapper ownerMapper,
      CatModelEntityMapper catMapper) {
    this.userRepo = userRepo;
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
  public void removeOwner(OwnerModel owner) throws AccessDeniedException {
    Owner ownerEntity =
        ownerRepo
            .findById(owner.getId())
            .orElseThrow(() -> new NoSuchOwnerException(owner.getId()));
    if (!checkAccess(ownerEntity)) {
      throw new AccessDeniedException("Can't delete another owner");
    }

    ownerRepo.delete(ownerEntity);
  }

  @Override
  public OwnerModel getOwnerById(UUID id) throws AccessDeniedException {
    Owner ownerEntity = ownerRepo.findById(id).orElseThrow(() -> new NoSuchOwnerException(id));
    if (!checkAccess(ownerEntity)) {
      throw new AccessDeniedException("User has no access to this owner");
    }

    return ownerMapper.toModel(ownerEntity);
  }

  @Override
  public List<OwnerModel> getAllOwners() {
    return filterOwners(ownerRepo.findAll()).stream()
        .map(ownerMapper::toModel)
        .collect(Collectors.toList());
  }

  @Override
  public List<CatModel> findAllOwnedCats(OwnerModel owner) throws AccessDeniedException {
    Owner ownerEntity =
        ownerRepo
            .findById(owner.getId())
            .orElseThrow(() -> new NoSuchOwnerException(owner.getId()));
    if (!checkAccess(ownerEntity)) {
      throw new AccessDeniedException("User has no access to this owner");
    }

    return ownerEntity.getOwnedCats().stream().map(catMapper::toModel).toList();
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

  private List<Owner> filterOwners(Collection<Owner> owners) {
    return owners.stream().filter(this::checkAccess).toList();
  }

  private boolean checkAccess(Owner owner) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepo.findByUsername(username).get();

    if (user.getRoles().stream().map(Role::getName).toList().contains("ADMIN")) {
      return true;
    }
    return owner.getId() == user.getOwner().getId();
  }
}
