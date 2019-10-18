package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

public class FixedValueDataExtractor implements DataExtractor {
  private final String value;

  public FixedValueDataExtractor(String value) {
    this.value = value;
  }

  @Override
  public String value(Datalayer datalayer) {
    return value;
  }
}
