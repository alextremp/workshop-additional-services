package com.github.alextremp.loadrules.application.savesite.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketplaceDTO {
  private Long id;
  private String code;
}

