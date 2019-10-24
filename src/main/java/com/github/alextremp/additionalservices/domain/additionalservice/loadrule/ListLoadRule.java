package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import java.util.List;
import java.util.Objects;

public abstract class ListLoadRule implements LoadRule {

  private final List<LoadRule> loadRules;

  public ListLoadRule(List<LoadRule> loadRules) {
    Objects.requireNonNull(loadRules, "List loadRules cannot be null");
    if (loadRules.isEmpty()) {
      throw new IllegalArgumentException("List loadRules cannot be empty");
    }
    this.loadRules = loadRules;
  }

  public List<LoadRule> loadRules() {
    return loadRules;
  }
}
