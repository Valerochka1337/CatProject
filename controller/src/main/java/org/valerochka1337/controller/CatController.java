package org.valerochka1337.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.mapper.CatDTOModelMapper;
import org.valerochka1337.services.CatService;

@RestController
@RequestMapping(path = "api/v1/cats")
public class CatController {

  private final CatService catService;

  private final CatDTOModelMapper catDTOModelMapper;

  @Autowired
  public CatController(CatService catService, CatDTOModelMapper catDTOModelMapper) {
    this.catService = catService;
    this.catDTOModelMapper = catDTOModelMapper;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('cats:write')")
  public CatDTO createCat(@RequestBody CatDTO catDTO) {
    return catDTOModelMapper.toDTO(catService.createCat(catDTOModelMapper.toModel(catDTO)));
  }

  @PutMapping(path = "/{id}", params = "ownerID")
  @PreAuthorize("hasAuthority('cats:write')")
  public CatDTO setCatsOwner(
      @PathVariable(name = "id") UUID catId, @RequestParam(name = "ownerID") UUID ownerId)
      throws AccessDeniedException {
    return catDTOModelMapper.toDTO(catService.setCatsOwner(catId, ownerId));
  }

  @DeleteMapping(path = "{id}")
  @PreAuthorize("hasAuthority('cats:write')")
  public void removeCat(@PathVariable UUID id) throws AccessDeniedException {
    catService.removeCat(id);
  }

  @GetMapping(path = "{id}")
  @PreAuthorize("hasAuthority('cats:read')")
  public CatDTO getCatById(@PathVariable UUID id) throws AccessDeniedException {
    return catDTOModelMapper.toDTO(catService.getCatById(id));
  }

  @GetMapping()
  @PreAuthorize("hasAuthority('cats:read')")
  public List<CatDTO> getAllCats() {
    return catService.getAllCats().stream().map(catDTOModelMapper::toDTO).toList();
  }

  @GetMapping(path = "{id}/friends")
  @PreAuthorize("hasAuthority('cats:read')")
  public List<CatDTO> findAllFriends(@PathVariable UUID id) throws AccessDeniedException {
    return catService.findAllFriends(id).stream().map(catDTOModelMapper::toDTO).toList();
  }

  @PutMapping(path = "friend")
  @PreAuthorize("hasAuthority('cats:write')")
  public void friendCats(
      @RequestParam(name = "id1") UUID cat1Id, @RequestParam(name = "id2") UUID cat2Id) {
    catService.friendCats(cat1Id, cat2Id);
  }

  @PutMapping(path = "unfriend")
  @PreAuthorize("hasAuthority('cats:write')")
  public void unfriendCats(
      @RequestParam(name = "id1") UUID cat1Id, @RequestParam(name = "id2") UUID cat2Id) {
    catService.unfriendCats(cat1Id, cat2Id);
  }

  @GetMapping(params = "color")
  @PreAuthorize("hasAuthority('cats:read')")
  public List<CatDTO> findCatsByColor(@RequestParam String color) {
    return catService.findCatsByColor(catDTOModelMapper.mapStringToColor(color)).stream()
        .map(catDTOModelMapper::toDTO)
        .toList();
  }

  @GetMapping(params = "breed")
  @PreAuthorize("hasAuthority('cats:read')")
  public List<CatDTO> findCatsByBreed(@RequestParam String breed) {
    return catService.findCatsByBreed(breed).stream().map(catDTOModelMapper::toDTO).toList();
  }
}
