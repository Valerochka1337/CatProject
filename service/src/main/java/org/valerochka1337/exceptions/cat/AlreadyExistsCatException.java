package org.valerochka1337.exceptions.cat;

import java.util.UUID;

public class AlreadyExistsCatException extends CatException {
  public AlreadyExistsCatException(UUID id) {
    super("Cat with id:%s already exists".formatted(id));
  }
}
