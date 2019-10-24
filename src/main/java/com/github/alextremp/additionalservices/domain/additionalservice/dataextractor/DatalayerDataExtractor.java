package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.Objects;

public class DatalayerDataExtractor implements DataExtractor {
  private final String key;

  public DatalayerDataExtractor(String key) {
    Objects.requireNonNull(key, "'key' cannot be null");
    this.key = key;
  }

  @Override
  public String value(Datalayer datalayer) {
    return datalayer.value(key);
  }

  public String key() {
    return key;
  }
}
