package org.valerochka1337.exceptions.cat;

public class NullBirthDateCatException extends CatException {
  public NullBirthDateCatException() {
    super("Cat must have birth date");
  }
}
