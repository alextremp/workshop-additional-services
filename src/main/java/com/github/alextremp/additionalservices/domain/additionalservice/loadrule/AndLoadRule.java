package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.List;

public class AndLoadRule extends ListLoadRule {

  public AndLoadRule(List<LoadRule> loadRules) {
    super(loadRules);
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return loadRules().stream().allMatch(loadRule -> loadRule.evaluate(datalayer));
  }
}
