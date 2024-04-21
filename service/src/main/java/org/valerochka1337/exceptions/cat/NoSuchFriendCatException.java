package org.valerochka1337.exceptions.cat;

import java.util.UUID;

public class NoSuchFriendCatException extends CatException {
  public NoSuchFriendCatException(UUID id) {
    super("No friend-cat with id: %s".formatted(id));
  }
}
