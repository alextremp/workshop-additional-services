package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteDTO {
  private String id;
  private String version;
  private List<AdditionalServiceDTO> additionalServices;
}
