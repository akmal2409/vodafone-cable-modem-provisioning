package com.akmal.provisioningservice.exceptions;

public class MessageRejectedException extends RuntimeException {

  public MessageRejectedException(String message) {
    super(message);
  }

  public MessageRejectedException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageRejectedException(Throwable cause) {
    super(cause);
  }

  protected MessageRejectedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
