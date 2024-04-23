package org.valerochka1337.exceptions;

public class NonExistingLoginUserException extends UserException {
  public NonExistingLoginUserException() {
    super("No user with such login");
  }
}
