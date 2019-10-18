package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalServiceDTO {
  private String id;
  private TypeDTO type;
  private MarketplaceDTO marketplace;
  private AppnexusDTO appnexus;
  private List<LoadRuleDTO> loadRules;
}
