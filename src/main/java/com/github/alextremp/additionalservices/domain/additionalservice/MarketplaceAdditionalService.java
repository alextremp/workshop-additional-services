package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

public class MarketplaceAdditionalService extends AppNexusAdditionalService {

  private final String marketplaceId;

  protected MarketplaceAdditionalService(String id, LoadRule loadRule, String marketplaceId, String code) {
    super(id, loadRule, code);
    this.marketplaceId = marketplaceId;
  }

  public String marketplaceId() {
    return marketplaceId;
  }
}
