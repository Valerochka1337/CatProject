package org.valerochka1337.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.dto.OwnerDTO;
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
  @PreAuthorize("hasAuthority('owners:write')")
  public OwnerDTO createOwner(@RequestBody OwnerDTO owner) {
    return ownerDTOModelMapper.toDTO(ownerService.createOwner(ownerDTOModelMapper.toModel(owner)));
  }

  @DeleteMapping(path = "/{id}")
  @PreAuthorize("hasAuthority('owners:write')")
  public void removeOwner(@PathVariable UUID id) throws AccessDeniedException {
    ownerService.removeOwner(ownerService.getOwnerById(id));
  }

  @GetMapping(path = "/{id}")
  @PreAuthorize("hasAuthority('owners:read')")
  public OwnerDTO getOwnerById(@PathVariable UUID id) throws AccessDeniedException {
    return ownerDTOModelMapper.toDTO(ownerService.getOwnerById(id));
  }

  @GetMapping
  @PreAuthorize("hasAuthority('owners:read')")
  public List<OwnerDTO> findAllOwners() {
    return ownerService.getAllOwners().stream().map(ownerDTOModelMapper::toDTO).toList();
  }

  @GetMapping(path = "/{id}/ownedCats")
  @PreAuthorize("hasAuthority('owners:read')")
  public List<CatDTO> findAllOwnedCats(@PathVariable UUID id) throws AccessDeniedException {
    return ownerService.findAllOwnedCats(ownerService.getOwnerById(id)).stream()
        .map(catDTOModelMapper::toDTO)
        .toList();
  }
}
