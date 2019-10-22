package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalServiceDTO {
  private String id;
  private TypeDTO type;
  private MarketplaceDTO marketplace;
  private AppnexusDTO appnexus;
  private List<LoadRuleDTO> loadRules;
}
