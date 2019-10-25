package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class AdditionalServiceJson {
  private String id;
  private Boolean enabled = Boolean.TRUE;
  private TypeJson type;
  private MarketplaceJson marketplace;
  private AppnexusJson appnexus;
  private List<LoadRuleJson> loadRules;
}
