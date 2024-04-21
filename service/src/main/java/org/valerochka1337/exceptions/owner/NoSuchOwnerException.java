package org.valerochka1337.exceptions.owner;

import java.util.UUID;

public class NoSuchOwnerException extends OwnerException {
  public NoSuchOwnerException(UUID id) {
    super("No such owner with id: %s".formatted(id));
  }
}
