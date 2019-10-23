package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.List;

public class OrLoadRule extends ListLoadRule {

  public OrLoadRule(List<LoadRule> loadRules) {
    super(loadRules);
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return loadRules().stream().anyMatch(loadRule -> loadRule.evaluate(datalayer));
  }
}
