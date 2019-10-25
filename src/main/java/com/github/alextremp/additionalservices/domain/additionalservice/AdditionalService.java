package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.AndLoadRule;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

import java.util.List;
import java.util.Objects;

public abstract class AdditionalService {

  private final String id;
  private final Boolean enabled;
  private final AndLoadRule loadRule;

  protected AdditionalService(String id, Boolean enabled, List<LoadRule> loadRules) {
    Objects.requireNonNull(id, "id cannot be null");
    Objects.requireNonNull(loadRules, "loadRules cannot be null");
    if (loadRules.isEmpty()) {
      throw new IllegalArgumentException("loadRules cannot be empty");
    }
    this.id = id;
    this.enabled = enabled != null ? enabled : Boolean.TRUE;
    this.loadRule = new AndLoadRule(loadRules);
  }

  public boolean active(Datalayer datalayer) throws AdditionalServiceEvaluationException {
    try {
      return enabled && loadRule.evaluate(datalayer);
    } catch (Exception evaluationException) {
      throw new AdditionalServiceEvaluationException("Error evaluating: " + id, evaluationException);
    }
  }

  public String id() {
    return id;
  }

  public AndLoadRule loadRule() {
    return loadRule;
  }

  public Boolean enabled() {
    return enabled;
  }
}
