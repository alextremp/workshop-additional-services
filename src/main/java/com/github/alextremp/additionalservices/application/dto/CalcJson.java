package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalcJson {
  private CalcOperatorJson operator;
  private DataValueJson left;
  private DataValueJson right;
}