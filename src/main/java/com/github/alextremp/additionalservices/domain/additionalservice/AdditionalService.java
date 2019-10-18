package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

public abstract class AdditionalService {

  private final String id;
  private final LoadRule loadRule;

  protected AdditionalService(String id, LoadRule loadRule) {
    this.id = id;
    this.loadRule = loadRule;
  }

  public boolean active(Datalayer datalayer) throws AdditionalServiceEvaluationException {
    try {
      return loadRule.evaluate(datalayer);
    } catch (Exception evaluationException) {
      throw new AdditionalServiceEvaluationException("Error evaluating: " + id, evaluationException);
    }
  }

  public String id() {
    return id;
  }

  public LoadRule loadRule() {
    return loadRule;
  }
}
