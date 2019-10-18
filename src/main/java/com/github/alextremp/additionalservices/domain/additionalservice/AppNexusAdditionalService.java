package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

public class AppNexusAdditionalService extends AdditionalService {
  private final String code;

  protected AppNexusAdditionalService(String id, LoadRule loadRule, String code) {
    super(id, loadRule);
    this.code = code;
  }

  public String code() {
    return code;
  }
}
