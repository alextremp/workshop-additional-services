package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;

public enum TypeJson {
  MARKETPLACE, APPNEXUS;

  private static final String MARKETPLACE_VALUE = "marketplace";
  private static final String APPNEXUS_VALUE = "appnexus";

  @JsonValue
  public String toValue() {
    switch (this) {
      case MARKETPLACE:
        return MARKETPLACE_VALUE;
      case APPNEXUS:
        return APPNEXUS_VALUE;
    }
    return null;
  }

  @JsonCreator
  public static TypeJson forValue(String value) throws IOException {
    switch (value) {
      case MARKETPLACE_VALUE:
        return MARKETPLACE;
      case APPNEXUS_VALUE:
        return APPNEXUS;
      default:
        throw new IOException("Cannot deserialize Type: " + value);
    }
  }

}