package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

import java.util.List;
import java.util.Objects;

public class MarketplaceAdditionalService extends AppNexusAdditionalService {

  private final Integer marketplaceId;

  protected MarketplaceAdditionalService(String id, List<LoadRule> loadRules, String code, Integer marketplaceId) {
    super(id, loadRules, code);
    Objects.requireNonNull(marketplaceId, "marketplaceId cannot be null");
    this.marketplaceId = marketplaceId;
  }

  public Integer marketplaceId() {
    return marketplaceId;
  }
}
