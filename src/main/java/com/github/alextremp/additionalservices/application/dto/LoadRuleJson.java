package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadRuleJson {
  private BooleanOperatorJson operator;
  private LoadRuleJson not;
  private List<LoadRuleJson> and;
  private List<LoadRuleJson> or;
  private InOperatorJson in;
  private LeftRightOperatorJson equal;
  private LeftRightOperatorJson lessThan;
  private LeftRightOperatorJson greaterThan;
}

