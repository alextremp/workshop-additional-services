package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalcDTO {
  private CalcOperatorDTO operator;
  private DataValueDTO left;
  private DataValueDTO right;
}
