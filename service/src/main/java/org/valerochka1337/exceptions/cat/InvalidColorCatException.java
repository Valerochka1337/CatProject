package org.valerochka1337.exceptions.cat;

public class InvalidColorCatException extends CatException {
  public InvalidColorCatException() {
    super("Invalid color");
  }
}
