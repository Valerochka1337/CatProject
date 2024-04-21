package org.valerochka1337.exceptions.cat;

public class NoNameCatException extends CatException {
  public NoNameCatException() {
    super("Cat must have name");
  }
}
