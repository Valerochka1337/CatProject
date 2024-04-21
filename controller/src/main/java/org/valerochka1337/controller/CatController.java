package org.valerochka1337.controller;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.valerochka1337.dto.CatDTO;
import org.valerochka1337.entity.Color;
import org.valerochka1337.exceptions.cat.InvalidBirthDateCatException;
import org.valerochka1337.exceptions.cat.InvalidColorCatException;
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
  public CatDTO createCat(@RequestBody CatDTO catDTO) {
    try {
      return catDTOModelMapper.toDTO(catService.createCat(catDTOModelMapper.toModel(catDTO)));
    } catch (DateTimeParseException e) {
      throw new InvalidBirthDateCatException();
    } catch (Exception e) {
      if (Arrays.stream(Color.values()).noneMatch(c -> c.name().equals(catDTO.getColor()))) {
        throw new InvalidColorCatException();
      }

      throw e;
    }
  }

  @GetMapping(path = "/{id}", params = "ownerID")
  public CatDTO setCatsOwner(
      @PathVariable(name = "id") UUID catId, @RequestParam(name = "ownerID") UUID ownerId) {
    return catDTOModelMapper.toDTO(catService.setCatsOwner(catId, ownerId));
  }

  @DeleteMapping(path = "{id}")
  public void removeCat(@PathVariable UUID id) {
    catService.removeCat(id);
  }

  @GetMapping(path = "{id}")
  public CatDTO getCatById(@PathVariable UUID id) {
    return catDTOModelMapper.toDTO(catService.getCatById(id));
  }

  @GetMapping()
  public List<CatDTO> getAllCats() {
    return catService.getAllCats().stream().map(catDTOModelMapper::toDTO).toList();
  }

  @GetMapping(path = "{id}/friends")
  public List<CatDTO> findAllFriends(@PathVariable UUID id) {
    return catService.findAllFriends(id).stream().map(catDTOModelMapper::toDTO).toList();
  }

  @PutMapping(path = "friend")
  public void friendCats(
      @RequestParam(name = "id1") UUID cat1Id, @RequestParam(name = "id2") UUID cat2Id) {
    catService.friendCats(cat1Id, cat2Id);
  }

  @PutMapping(path = "unfriend")
  public void unfriendCats(
      @RequestParam(name = "id1") UUID cat1Id, @RequestParam(name = "id2") UUID cat2Id) {
    catService.unfriendCats(cat1Id, cat2Id);
  }

  @GetMapping(params = "color")
  public List<CatDTO> findCatsByColor(@RequestParam String color) {
    return catService.findCatsByColor(Color.valueOf(color)).stream()
        .map(catDTOModelMapper::toDTO)
        .toList();
  }

  @GetMapping(params = "breed")
  public List<CatDTO> findCatsByBreed(@RequestParam String breed) {
    return catService.findCatsByBreed(breed).stream().map(catDTOModelMapper::toDTO).toList();
  }
}
