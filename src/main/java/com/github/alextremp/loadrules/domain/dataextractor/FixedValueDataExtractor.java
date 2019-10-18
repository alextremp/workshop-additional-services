package com.github.alextremp.loadrules.domain.dataextractor;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

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
