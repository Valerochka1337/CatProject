package org.valerochka1337.exceptions.cat;

import java.util.UUID;

public class NoSuchCatException extends CatException {
  public NoSuchCatException(UUID id) {
    super("No such cat with id: %s".formatted(id));
  }
}
