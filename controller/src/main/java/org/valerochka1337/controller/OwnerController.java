package org.valerochka1337.controller;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.dto.OwnerDTO;
import org.valerochka1337.exceptions.owner.InvalidBirthDateOwnerException;
import org.valerochka1337.mapper.CatDTOModelMapper;
import org.valerochka1337.mapper.OwnerDTOModelMapper;
import org.valerochka1337.services.OwnerService;

@RestController
@RequestMapping(path = "api/v1/owners")
public class OwnerController {

  private final OwnerService ownerService;
  private final CatDTOModelMapper catDTOModelMapper;

  private final OwnerDTOModelMapper ownerDTOModelMapper;

  @Autowired
  public OwnerController(
      OwnerService ownerService,
      CatDTOModelMapper catDTOModelMapper,
      OwnerDTOModelMapper ownerDTOModelMapper) {
    this.ownerService = ownerService;
    this.catDTOModelMapper = catDTOModelMapper;
    this.ownerDTOModelMapper = ownerDTOModelMapper;
  }

  @PostMapping
  public OwnerDTO createOwner(@RequestBody OwnerDTO owner) {
    try {
      return ownerDTOModelMapper.toDTO(
          ownerService.createOwner(ownerDTOModelMapper.toModel(owner)));
    } catch (DateTimeParseException e) {
      throw new InvalidBirthDateOwnerException();
    }
  }

  @DeleteMapping(path = "/{id}")
  public void removeOwner(@PathVariable UUID id) {
    ownerService.removeOwner(ownerService.getOwnerById(id));
  }

  @GetMapping(path = "/{id}")
  public OwnerDTO getOwnerById(@PathVariable UUID id) {
    return ownerDTOModelMapper.toDTO(ownerService.getOwnerById(id));
  }

  @GetMapping
  public List<OwnerDTO> findAllOwners() {
    return ownerService.getAllOwners().stream().map(ownerDTOModelMapper::toDTO).toList();
  }

  @GetMapping(path = "/{id}/ownedCats")
  public List<CatDTO> findAllOwnedCats(@PathVariable UUID id) {
    return ownerService.findAllOwnedCats(ownerService.getOwnerById(id)).stream()
        .map(catDTOModelMapper::toDTO)
        .toList();
  }
}
