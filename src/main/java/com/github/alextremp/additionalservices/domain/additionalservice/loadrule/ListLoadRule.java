package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import java.util.List;

public abstract class ListLoadRule implements LoadRule {

  private final List<LoadRule> loadRules;

  public ListLoadRule(List<LoadRule> loadRules) {
    if (loadRules == null || loadRules.isEmpty()) {
      throw new IllegalArgumentException(getClass().getSimpleName() + ": load rules cannot be empty");
    }
    this.loadRules = loadRules;
  }

  public List<LoadRule> loadRules() {
    return loadRules;
  }
}
