package org.valerochka1337.exceptions.handlers;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.valerochka1337.exceptions.ApiException;
import org.valerochka1337.exceptions.JwtAuthenticationException;
import org.valerochka1337.exceptions.UserException;
import org.valerochka1337.exceptions.cat.CatException;
import org.valerochka1337.exceptions.owner.OwnerException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(value = {JwtAuthenticationException.class})
  public ResponseEntity<Object> handleAuthException(
      JwtAuthenticationException ex, HttpServletResponse response) {
    HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
    ApiException apiException =
        new ApiException(
            ex.getMessage(),
            badRequest.value(),
            badRequest.getReasonPhrase(),
            ZonedDateTime.now(ZoneId.of("Z")));

    return new ResponseEntity<>(apiException, badRequest);
  }

  @ExceptionHandler(value = {UserException.class})
  public ResponseEntity<Object> handleUserException(UserException ex) {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
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
