package com.github.alextremp.loadrules.domain.loadrule;

import com.github.alextremp.loadrules.domain.dataextractor.DataExtractor;

public class GreaterThanComparisonLoadRule extends ComparisonLoadRule {
  protected GreaterThanComparisonLoadRule(DataExtractor leftDataExtractor, DataExtractor rightDataExtractor) {
    super(leftDataExtractor, rightDataExtractor);
  }

  @Override
  protected boolean evaluate(String left, String right) {
    return Integer.valueOf(left) > Integer.valueOf(right);
  }
}
