package com.github.alextremp.loadrules.application.savesite.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadRuleDTO {
  private BooleanOperatorDTO operator;
  private LoadRuleDTO not;
  private List<LoadRuleDTO> and;
  private List<LoadRuleDTO> or;
  private InOperatorDTO in;
  private LeftRightOperatorDTO equal;
  private LeftRightOperatorDTO lessThan;
  private LeftRightOperatorDTO greaterThan;
}

