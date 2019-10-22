package com.github.alextremp.additionalservices.domain.site;

import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;

import java.util.List;

public class Site {
  private final String id;
  private final String version;
  private final List<AdditionalService> additionalServices;

  public Site(String id, String version, List<AdditionalService> additionalServices) {
    this.id = id;
    this.version = version;
    this.additionalServices = additionalServices;
  }

  public String getId() {
    return id;
  }

  public String getVersion() {
    return version;
  }

  public List<AdditionalService> getAdditionalServices() {
    return additionalServices;
  }
}
