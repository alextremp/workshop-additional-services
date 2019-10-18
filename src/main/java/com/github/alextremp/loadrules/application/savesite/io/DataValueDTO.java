package com.github.alextremp.loadrules.application.savesite.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataValueDTO {
  private SourceDTO source;
  private String value;
  private String datalayer;
  private CalcDTO calc;
  private String platform;
}
