package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

import java.util.List;

public abstract class AdditionalServiceBuilder {

  protected String id;
  protected Boolean enabled;
  protected List<LoadRule> loadRules;

  public static MarketplaceAdditionalServiceBuilder marketplace() {
    return new MarketplaceAdditionalServiceBuilder();
  }

  public static AppnexusAdditionalServiceBuilder appnexus() {
    return new AppnexusAdditionalServiceBuilder();
  }

  public abstract AdditionalService build();

  public AdditionalServiceBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public AdditionalServiceBuilder withEnabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public AdditionalServiceBuilder withLoadRules(List<LoadRule> loadRules) {
    this.loadRules = loadRules;
    return this;
  }

  public static class MarketplaceAdditionalServiceBuilder extends AppnexusAdditionalServiceBuilder {
    private Integer marketplaceId;

    public MarketplaceAdditionalServiceBuilder withMarketplaceId(Integer marketplaceId) {
      this.marketplaceId = marketplaceId;
      return this;
    }

    @Override
    public AdditionalService build() {
      return new MarketplaceAdditionalService(id, enabled, loadRules, code, marketplaceId);
    }

  }

  public static class AppnexusAdditionalServiceBuilder extends AdditionalServiceBuilder {
    protected String code;

    public AppnexusAdditionalServiceBuilder withCode(String code) {
      this.code = code;
      return this;
    }

    @Override
    public AdditionalService build() {
      return new AppNexusAdditionalService(id, enabled, loadRules, code);
    }
  }

}
