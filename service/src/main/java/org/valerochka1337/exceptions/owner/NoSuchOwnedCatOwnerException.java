package org.valerochka1337.exceptions.owner;

public class NoSuchOwnedCatOwnerException extends OwnerException {
  public NoSuchOwnedCatOwnerException() {
    super("No such owned cat");
  }
}
