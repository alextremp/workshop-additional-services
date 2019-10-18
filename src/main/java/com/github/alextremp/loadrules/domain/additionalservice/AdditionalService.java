package com.github.alextremp.loadrules.domain.additionalservice;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;
import com.github.alextremp.loadrules.domain.loadrule.LoadRule;

public abstract class AdditionalService {

  enum Type {
    MARKETPLACE,
    APPNEXUS
  }

  private final String id;
  private final Type type;
  private final LoadRule loadRule;

  protected AdditionalService(String id, Type type, LoadRule loadRule) {
    this.id = id;
    this.type = type;
    this.loadRule = loadRule;
  }

  public boolean active(Datalayer datalayer) throws AdditionalServiceEvaluationException {
    try{
      return loadRule.evaluate(datalayer);
    } catch (Exception evaluationException) {
      throw new AdditionalServiceEvaluationException("Error evaluating: " + id, evaluationException);
    }
  }

  public String id() {
    return id;
  }

  public Type type() {
    return type;
  }

  public LoadRule loadRule() {
    return loadRule;
  }
}
