package org.valerochka1337.exceptions.owner;

public class NullOwnerException extends OwnerException {
  public NullOwnerException() {
    super("Owner can't be null");
  }
}
