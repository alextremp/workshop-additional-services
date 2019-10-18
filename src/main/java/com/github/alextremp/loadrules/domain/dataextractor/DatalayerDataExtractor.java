package com.github.alextremp.loadrules.domain.dataextractor;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

public class DatalayerDataExtractor implements DataExtractor {
  private final String key;

  public DatalayerDataExtractor(String key) {
    this.key = key;
  }

  @Override
  public String value(Datalayer datalayer) {
    return datalayer.value(key);
  }
}
