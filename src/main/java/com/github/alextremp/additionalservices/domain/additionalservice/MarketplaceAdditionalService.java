package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

import java.util.List;

public class MarketplaceAdditionalService extends AppNexusAdditionalService {

  private final Integer marketplaceId;

  protected MarketplaceAdditionalService(String id, List<LoadRule> loadRules, Integer marketplaceId, String code) {
    super(id, loadRules, code);
    this.marketplaceId = marketplaceId;
  }

  public Integer marketplaceId() {
    return marketplaceId;
  }
}
