package com.github.alextremp.additionalservices.domain.error;

public class DomainError extends RuntimeException {
  public DomainError(String message) {
    super(message);
  }
}
