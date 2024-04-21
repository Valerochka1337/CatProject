package org.valerochka1337.exceptions.cat;

public class NullCatException extends CatException {
  public NullCatException() {
    super("Cat can't be null");
  }
}
