package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.DataExtractor;

public class GreaterThanComparisonLoadRule extends ComparisonLoadRule {
  protected GreaterThanComparisonLoadRule(DataExtractor leftDataExtractor, DataExtractor rightDataExtractor) {
    super(leftDataExtractor, rightDataExtractor);
  }

  @Override
  protected boolean evaluate(String left, String right) {
    return Integer.valueOf(left) > Integer.valueOf(right);
  }
}
