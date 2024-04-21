package org.valerochka1337.exceptions;

import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class ApiException {
  private final String message;
  private final int status;
  private final String reasonPhrase;
  private final ZonedDateTime timestamp;

  public ApiException(String message, int status, String reasonPhrase, ZonedDateTime timestamp) {
    this.message = message;
    this.status = status;
    this.reasonPhrase = reasonPhrase;
    this.timestamp = timestamp;
  }
}
