package com.github.alextremp.additionalservices.domain.site;

import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;

import java.util.List;

public class SiteBuilder {

  private String id;
  private String version;
  private List<AdditionalService> additionalServices;

  public static SiteBuilder create() {
    return new SiteBuilder();
  }

  public SiteBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public SiteBuilder withVersion(String version) {
    this.version = version;
    return this;
  }

  public SiteBuilder withAdditionalServices(List<AdditionalService> additionalServices) {
    this.additionalServices = additionalServices;
    return this;
  }

  public Site build() {
    return new Site(id, version, additionalServices);
  }
}
