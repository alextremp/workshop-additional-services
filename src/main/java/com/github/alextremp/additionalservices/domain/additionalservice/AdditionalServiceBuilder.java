package com.github.alextremp.additionalservices.domain.additionalservice;

import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;

public abstract class AdditionalServiceBuilder {

  protected String id;
  protected LoadRule loadRule;

  public static AdditionalServiceBuilder marketplace() {
    return new MarketplaceAdditionalServiceBuilder();
  }

  public static AdditionalServiceBuilder appnexus() {
    return new AppnexusAdditionalServiceBuilder();
  }

  public abstract AdditionalService build();

  public AdditionalServiceBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public AdditionalServiceBuilder withLoadRule(LoadRule loadRule) {
    this.loadRule = loadRule;
    return this;
  }

  public static class MarketplaceAdditionalServiceBuilder extends AppnexusAdditionalServiceBuilder {
    private String marketplaceId;

    public MarketplaceAdditionalServiceBuilder withMarketplaceId(String marketplaceId) {
      this.marketplaceId = marketplaceId;
      return this;
    }

    @Override
    public AdditionalService build() {
      return new MarketplaceAdditionalService(id, loadRule, marketplaceId, code);
    }

    public AppnexusAdditionalServiceBuilder and() {
      return this;
    }
  }

  public static class AppnexusAdditionalServiceBuilder extends AdditionalServiceBuilder {
    protected String code;

    public AppnexusAdditionalServiceBuilder withCode(String code) {
      this.code = code;
      return this;
    }

    public AdditionalServiceBuilder and() {
      return this;
    }

    @Override
    public AdditionalService build() {
      return new AppNexusAdditionalService(id, loadRule, code);
    }
  }

}
