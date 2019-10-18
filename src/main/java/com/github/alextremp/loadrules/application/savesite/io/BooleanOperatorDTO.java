package com.github.alextremp.loadrules.application.savesite.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum BooleanOperatorDTO {
  EQUAL, NOT, AND, OR, IN, LESS_THAN, GREATER_THAN;

  private static final String EQUAL_VALUE = "equal";
  private static final String NOT_VALUE = "not";
  private static final String AND_VALUE = "and";
  private static final String OR_VALUE = "or";
  private static final String IN_VALUE = "in";
  private static final String LESS_THAN_VALUE = "lessThan";
  private static final String GREATER_THAN_VALUE = "greaterThan";

  @JsonValue
  public String toValue() {
    switch (this) {
      case EQUAL:
        return EQUAL_VALUE;
      case NOT:
        return NOT_VALUE;
      case AND:
        return AND_VALUE;
      case OR:
        return OR_VALUE;
      case IN:
        return IN_VALUE;
      case LESS_THAN:
        return LESS_THAN_VALUE;
      case GREATER_THAN:
        return GREATER_THAN_VALUE;
    }
    return null;
  }

  @JsonCreator
  public static BooleanOperatorDTO forValue(String value) throws IOException {
    switch (value) {
      case EQUAL_VALUE:
        return EQUAL;
      case NOT_VALUE:
        return NOT;
      case AND_VALUE:
        return AND;
      case OR_VALUE:
        return OR;
      case IN_VALUE:
        return IN;
      case LESS_THAN_VALUE:
        return LESS_THAN;
      case GREATER_THAN_VALUE:
        return GREATER_THAN;
      default:
        throw new IOException("Cannot deserialize Operator: " + value);
    }
  }
}
