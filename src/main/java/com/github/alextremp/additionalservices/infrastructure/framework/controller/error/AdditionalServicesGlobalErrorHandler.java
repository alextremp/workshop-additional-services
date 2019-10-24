package com.github.alextremp.additionalservices.infrastructure.framework.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class AdditionalServicesGlobalErrorHandler {

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleCustomException(Exception error) {
    return Mono.fromCallable(() -> ResponseStatusException.class.isAssignableFrom(error.getClass()) ? ((ResponseStatusException) error).getStatus() : HttpStatus.INTERNAL_SERVER_ERROR)
        .map(httpStatus -> new ErrorResponse(error, httpStatus))
        .map(errorResponse -> ResponseEntity.status(errorResponse.getStatus()).body(errorResponse));
  }
}
