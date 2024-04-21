package org.valerochka1337.exceptions.owner;

public class NoNameOwnerException extends OwnerException {
  public NoNameOwnerException() {
    super("Owner must have name");
  }
}
