package com.akmal.provisioningservice.exceptions;

public class InvalidMacException extends RuntimeException {

  public InvalidMacException(String message) {
    super(message);
  }

  public InvalidMacException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidMacException(Throwable cause) {
    super(cause);
  }

  protected InvalidMacException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
