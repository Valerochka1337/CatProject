package org.valerochka1337.exceptions.cat;

public class CatException extends RuntimeException {
  public CatException() {}

  public CatException(String message) {
    super(message);
  }
}
