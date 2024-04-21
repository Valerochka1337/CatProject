package org.valerochka1337.exceptions.cat;

import java.util.UUID;

public class AlreadyFriendsCatException extends CatException {
  public AlreadyFriendsCatException(UUID id1, UUID id2) {
    super("Cats with id: %s is already friend with cat with id: %s".formatted(id1, id2));
  }
}
