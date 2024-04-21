package org.valerochka1337.exceptions.cat;

public class InvalidBirthDateCatException extends CatException {
  public InvalidBirthDateCatException() {
    super("Invalid birth date");
  }
}
