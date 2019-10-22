package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.List;

public class AndLoadRule implements LoadRule {
  private final List<LoadRule> loadRules;

  public AndLoadRule(List<LoadRule> loadRules) {
    if (loadRules == null || loadRules.isEmpty()) {
      throw new IllegalArgumentException("AND load rules cannot be empty");
    }
    this.loadRules = loadRules;
  }

  public List<LoadRule> loadRules() {
    return loadRules;
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return loadRules.stream().allMatch(loadRule -> loadRule.evaluate(datalayer));
  }
}
