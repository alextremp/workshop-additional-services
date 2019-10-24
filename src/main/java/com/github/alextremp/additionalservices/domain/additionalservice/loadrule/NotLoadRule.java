package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.Objects;

public class NotLoadRule implements LoadRule {
  private final LoadRule loadRule;

  public NotLoadRule(LoadRule loadRule) {
    Objects.requireNonNull(loadRule, "NOT load rule cannot be null");
    this.loadRule = loadRule;
  }

  public LoadRule loadRule() {
    return loadRule;
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return !loadRule.evaluate(datalayer);
  }
}
