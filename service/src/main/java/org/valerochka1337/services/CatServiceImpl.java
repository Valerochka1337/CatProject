package org.valerochka1337.services;

import java.nio.file.AccessDeniedException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.valerochka1337.entity.*;
import org.valerochka1337.exceptions.cat.*;
import org.valerochka1337.exceptions.owner.NoSuchOwnerException;
import org.valerochka1337.mapper.CatModelEntityMapper;
import org.valerochka1337.model.CatModel;
import org.valerochka1337.repository.CatRepository;
import org.valerochka1337.repository.OwnerRepository;
import org.valerochka1337.repository.UserRepository;

@Service
public class CatServiceImpl implements CatService {
  private final UserRepository userRepo;
  private final OwnerRepository ownerRepo;
  private final CatRepository catRepo;

  private final CatModelEntityMapper catMapper;

  @Autowired
  public CatServiceImpl(
      UserRepository userRepo,
      CatRepository catRepo,
      OwnerRepository ownerRepo,
      CatModelEntityMapper catMapper) {
    this.userRepo = userRepo;
    this.catRepo = catRepo;
    this.ownerRepo = ownerRepo;
    this.catMapper = catMapper;
  }

  @Override
  public CatModel createCat(CatModel cat) {
    validateCat(cat);

    Cat catEntity = catMapper.toEntity(cat);

    catEntity = catRepo.saveAndFlush(catEntity);

    return catMapper.toModel(catEntity);
  }

  @Override
  public CatModel setCatsOwner(UUID catId, UUID ownerId) throws AccessDeniedException {
    Cat catEntity = catRepo.findById(catId).orElseThrow(() -> new NoSuchCatException(catId));
    Owner ownerEntity =
        ownerRepo.findById(ownerId).orElseThrow(() -> new NoSuchOwnerException(ownerId));

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepo.findByUsername(username).get();
    if (!user.getRoles().stream().map(Role::getName).toList().contains("ADMIN")
        && !user.getOwner().getId().equals(ownerEntity.getId())) {
      throw new AccessDeniedException("Can't set cat to another owner");
    }

    catEntity.setOwner(ownerEntity);
    ownerEntity.getOwnedCats().add(catEntity);

    catEntity = catRepo.saveAndFlush(catEntity);
    ownerRepo.saveAndFlush(ownerEntity);

    return catMapper.toModel(catEntity);
  }

  @Override
  public void removeCat(UUID id) throws AccessDeniedException {
    Cat catEntity = catRepo.findById(id).orElseThrow(() -> new NoSuchCatException(id));

    if (!checkAccess(catEntity)) {
      throw new AccessDeniedException("User has no access to this cat");
    }

    catRepo.deleteById(id);
  }

  @Override
  public CatModel getCatById(UUID id) throws AccessDeniedException {
    Cat catEntity = catRepo.findById(id).orElseThrow(() -> new NoSuchCatException(id));
    if (!checkAccess(catEntity)) {
      throw new AccessDeniedException("User has no access to this cat");
    }

    return catMapper.toModel(catEntity);
  }

  @Override
  public List<CatModel> getAllCats() {
    return filterCats(catRepo.findAll()).stream().map(catMapper::toModel).toList();
  }

  @Override
  public List<CatModel> findAllFriends(UUID id) throws AccessDeniedException {
    Cat catEntity = catRepo.findById(id).orElseThrow(() -> new NoSuchCatException(id));
    if (!checkAccess(catEntity)) {
      throw new AccessDeniedException("User has no access to this cat");
    }

    return catEntity.getFriendCats().stream().map(catMapper::toModel).toList();
  }

  @Override
  public List<CatModel> findCatsByBreed(String breed) {
    return filterCats(catRepo.findCatsByBreed(breed)).stream().map(catMapper::toModel).toList();
  }

  @Override
  public List<CatModel> findCatsByColor(Color color) {
    return filterCats(catRepo.findCatsByColor(color)).stream().map(catMapper::toModel).toList();
  }

  @Override
  public void friendCats(UUID cat1Id, UUID cat2Id) {
    if (cat1Id.equals(cat2Id)) {
      throw new FriendToItselfCatException();
    }

    Cat cat1 = catRepo.findById(cat1Id).orElseThrow(() -> new NoSuchCatException(cat1Id));
    Cat cat2 = catRepo.findById(cat2Id).orElseThrow(() -> new NoSuchCatException(cat2Id));

    if (cat1.getFriendCats().contains(cat2)) {
      throw new AlreadyFriendsCatException(cat1Id, cat2Id);
    }

    cat1.getFriendCats().add(cat2);
    cat2.getFriendCats().add(cat1);

    catRepo.save(cat1);
    catRepo.save(cat2);

    catRepo.flush();
  }

  @Override
  public void unfriendCats(UUID cat1Id, UUID cat2Id) {
    if (cat1Id.equals(cat2Id)) {
      throw new FriendToItselfCatException();
    }

    Cat cat1 = catRepo.findById(cat1Id).orElseThrow(() -> new NoSuchCatException(cat1Id));
    Cat cat2 = catRepo.findById(cat2Id).orElseThrow(() -> new NoSuchCatException(cat2Id));

    if (!cat1.getFriendCats().contains(cat2)) {
      throw new NotFriendsCatException(cat1Id, cat2Id);
    }

    cat1.getFriendCats().remove(cat2);
    cat2.getFriendCats().remove(cat1);

    catRepo.save(cat1);
    catRepo.save(cat2);

    catRepo.flush();
  }

  private void validateCat(CatModel cat) {
    if (cat == null) {
      throw new NullCatException();
    }
    if (cat.getId() != null && catRepo.existsById(cat.getId())) {
      throw new AlreadyExistsCatException(cat.getId());
    }
    if (cat.getName() == null) {
      throw new NoNameCatException();
    }
    if (cat.getBirthDate() == null) {
      throw new NullBirthDateCatException();
    }
    for (CatModel c : cat.getFriendCats()) {
      if (!catRepo.existsById(c.getId())) {
        throw new NoSuchFriendCatException(c.getId());
      }
    }

    if (cat.getOwner() != null && !ownerRepo.existsById(cat.getOwner().getId())) {
      throw new NoSuchOwnerException(cat.getOwner().getId());
    }
  }

  private List<Cat> filterCats(Collection<Cat> cats) {
    return cats.stream().filter(this::checkAccess).toList();
  }

  private boolean checkAccess(Cat cat) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepo.findByUsername(username).get();

    if (user.getRoles().stream().map(Role::getName).toList().contains("ADMIN")) {
      return true;
    }
    return cat.getOwner() != null && cat.getOwner().getId() == user.getOwner().getId();
  }
}
