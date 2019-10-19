package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

import java.util.List;

public class AppNexusAdditionalService extends AdditionalService {
  private final String code;

  protected AppNexusAdditionalService(String id, List<LoadRule> loadRules, String code) {
    super(id, loadRules);
    this.code = code;
  }

  public String code() {
    return code;
  }
}
