package com.github.alextremp.loadrules.domain.loadrule;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

public class NotLoadRule implements LoadRule {
  private final LoadRule loadRule;

  public NotLoadRule(LoadRule loadRule) {
    if (loadRule == null) {
      throw new IllegalArgumentException("NOT load rule cannot be null");
    }
    this.loadRule = loadRule;
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return !loadRule.evaluate(datalayer);
  }
}
