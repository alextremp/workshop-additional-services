package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

import java.util.List;
import java.util.Objects;

public class AppNexusAdditionalService extends AdditionalService {
  private final String code;

  protected AppNexusAdditionalService(String id, List<LoadRule> loadRules, String code) {
    super(id, loadRules);
    Objects.requireNonNull(code, "code cannot be null");
    this.code = code;
  }

  public String code() {
    return code;
  }
}
