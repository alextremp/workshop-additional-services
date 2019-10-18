package com.github.alextremp.loadrules.domain.additionalservice;

import com.github.alextremp.loadrules.domain.loadrule.LoadRule;

public class AppNexusAdditionalService extends AdditionalService {
  private final String code;

  protected AppNexusAdditionalService(String id, LoadRule loadRule, String code) {
    this(id, loadRule, code, Type.APPNEXUS);
  }

  protected AppNexusAdditionalService(String id, LoadRule loadRule, String code, Type type) {
    super(id, type, loadRule);
    this.code = code;
  }

  public String code() {
    return code;
  }
}
