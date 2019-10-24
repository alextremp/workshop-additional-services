package com.github.alextremp.additionalservices.domain.site;

import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;

import java.util.List;
import java.util.Objects;

public class Site {
  private final String id;
  private final String version;
  private final List<AdditionalService> additionalServices;

  public Site(String id, String version, List<AdditionalService> additionalServices) {
    Objects.requireNonNull(id, "id cannot be null");
    Objects.requireNonNull(version, "version cannot be null");
    Objects.requireNonNull(additionalServices, "additionalServices cannot be null");
    if (additionalServices.isEmpty()) {
      throw new IllegalArgumentException("additionalServices cannot be empty");
    }
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
