package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.List;

public class OrLoadRule implements LoadRule {
  private final List<LoadRule> loadRules;

  public OrLoadRule(List<LoadRule> loadRules) {
    if (loadRules == null || loadRules.isEmpty()) {
      throw new IllegalArgumentException("OR load rules cannot be empty");
    }
    this.loadRules = loadRules;
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return loadRules.stream().anyMatch(loadRule -> loadRule.evaluate(datalayer));
  }
}