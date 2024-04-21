package org.valerochka1337.exceptions.cat;

public class FriendToItselfCatException extends CatException {
  public FriendToItselfCatException() {
    super("Can't friend cat to itself (id1 == id2)");
  }
}
