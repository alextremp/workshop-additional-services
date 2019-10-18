package com.github.alextremp.loadrules.domain;

import com.github.alextremp.loadrules.domain.additionalservice.AdditionalService;

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
}
