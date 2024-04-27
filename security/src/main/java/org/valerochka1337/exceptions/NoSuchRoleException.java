package org.valerochka1337.exceptions;

public class NoSuchRoleException extends Exception {
  public NoSuchRoleException(String roleName) {
    super("No such role: " + roleName);
  }
}
