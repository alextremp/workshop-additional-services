package com.github.alextremp.loadrules.domain.datalayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class Datalayer {

  private final Map<String, String> data;

  public Datalayer(Map<String, String> data) {
    this.data = data != null ? data : new LinkedHashMap<>();
  }

  public String value(String key) {
    return data.get(key);
  }
}
