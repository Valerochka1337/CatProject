package org.valerochka1337.exceptions.cat;

import java.util.UUID;

public class NotFriendsCatException extends CatException {
  public NotFriendsCatException(UUID id1, UUID id2) {
    super("Cats with ids: 1) %s; 2) %s are not friends yet".formatted(id1, id2));
  }
}
