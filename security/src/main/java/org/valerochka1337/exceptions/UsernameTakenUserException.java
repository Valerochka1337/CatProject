package org.valerochka1337.exceptions;

public class UsernameTakenUserException extends UserException {
  public UsernameTakenUserException() {
    super("This username is already taken");
  }
}
