package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataValueJson {
  private SourceJson source;
  private String value;
  private String datalayer;
  private CalcJson calc;
  private String platform;
}
