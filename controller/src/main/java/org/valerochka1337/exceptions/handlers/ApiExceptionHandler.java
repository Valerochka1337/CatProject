package org.valerochka1337.exceptions.handlers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.valerochka1337.exceptions.ApiException;
import org.valerochka1337.exceptions.cat.CatException;
import org.valerochka1337.exceptions.owner.OwnerException;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<Object> handleOwnerException(Exception ex) {
    HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
    ApiException apiException =
        new ApiException(
            ex.getMessage(),
            badRequest.value(),
            badRequest.getReasonPhrase(),
            ZonedDateTime.now(ZoneId.of("Z")));

    return new ResponseEntity<>(apiException, badRequest);
  }

  @ExceptionHandler(value = {CatException.class})
  public ResponseEntity<Object> handleCatException(CatException ex) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    ApiException apiException =
        new ApiException(
            ex.getMessage(),
            badRequest.value(),
            badRequest.getReasonPhrase(),
            ZonedDateTime.now(ZoneId.of("Z")));

    return new ResponseEntity<>(apiException, badRequest);
  }

  @ExceptionHandler(value = {OwnerException.class})
  public ResponseEntity<Object> handleOwnerException(OwnerException ex) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    ApiException apiException =
        new ApiException(
            ex.getMessage(),
            badRequest.value(),
            badRequest.getReasonPhrase(),
            ZonedDateTime.now(ZoneId.of("Z")));

    return new ResponseEntity<>(apiException, badRequest);
  }
}
