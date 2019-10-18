package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum SourceDTO {
  VALUE, DATALAYER, CALC, PLATFORM;

  private static final String VALUE_VALUE = "value";
  private static final String DATALAYER_VALUE = "datalayer";
  private static final String CALC_VALUE = "calc";
  private static final String PLATFORM_VALUE = "platform";

  @JsonValue
  public String toValue() {
    switch (this) {
      case VALUE:
        return VALUE_VALUE;
      case DATALAYER:
        return DATALAYER_VALUE;
      case CALC:
        return CALC_VALUE;
      case PLATFORM:
        return PLATFORM_VALUE;
    }
    return null;
  }

  @JsonCreator
  public static SourceDTO forValue(String value) throws IOException {
    switch (value) {
      case VALUE_VALUE:
        return VALUE;
      case DATALAYER_VALUE:
        return DATALAYER;
      case CALC_VALUE:
        return CALC;
      case PLATFORM_VALUE:
        return PLATFORM;
      default:
        throw new IOException("Cannot deserialize Source: " + value);
    }
  }
}
