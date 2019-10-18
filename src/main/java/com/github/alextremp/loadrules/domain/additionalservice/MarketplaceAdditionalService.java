package com.github.alextremp.loadrules.domain.additionalservice;

import com.github.alextremp.loadrules.domain.loadrule.LoadRule;

public class MarketplaceAdditionalService extends AppNexusAdditionalService {

  private final String marketplaceId;

  protected MarketplaceAdditionalService(String id, LoadRule loadRule, String marketplaceId, String code) {
    super(id, loadRule, code, Type.MARKETPLACE);
    this.marketplaceId = marketplaceId;
  }

  public String marketplaceId() {
    return marketplaceId;
  }
}
