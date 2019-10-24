package com.github.alextremp.additionalservices.infrastructure.framework.controller.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {

  private final LocalDateTime timestamp;
  private final Integer status;
  private final String error;
  private final String message;
  private final String origin;

  public ErrorResponse(Exception error, HttpStatus httpStatus) {
    this(
        LocalDateTime.now(),
        httpStatus.value(),
        httpStatus.getReasonPhrase(),
        error.getLocalizedMessage(),
        error.getClass().getName()
    );
  }

  @JsonCreator
  public ErrorResponse(
      @JsonProperty("timestamp") LocalDateTime timestamp,
      @JsonProperty("status") Integer status,
      @JsonProperty("error") String error,
      @JsonProperty("message") String message,
      @JsonProperty("origin") String origin) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.message = message;
    this.origin = origin;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Integer getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public String getOrigin() {
    return origin;
  }
}
