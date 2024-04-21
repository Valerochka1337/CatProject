package org.valerochka1337.exceptions.owner;

public class NoBirthDateOwnerException extends OwnerException {
  public NoBirthDateOwnerException() {
    super("Owner must have birth date");
  }
}
