package com.github.alextremp.loadrules.domain.loadrule;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

import java.util.List;

public class AndLoadRule implements LoadRule {
  private final List<LoadRule> loadRules;

  public AndLoadRule(List<LoadRule> loadRules) {
    if (loadRules == null || loadRules.isEmpty()) {
      throw new IllegalArgumentException("AND load rules cannot be empty");
    }
    this.loadRules = loadRules;
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return loadRules.stream().allMatch(loadRule -> loadRule.evaluate(datalayer));
  }
}
