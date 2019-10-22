package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalServiceJson {
  private String id;
  private TypeJson type;
  private MarketplaceJson marketplace;
  private AppnexusJson appnexus;
  private List<LoadRuleJson> loadRules;
}
