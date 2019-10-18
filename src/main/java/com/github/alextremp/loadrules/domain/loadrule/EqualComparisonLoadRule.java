package com.github.alextremp.loadrules.domain.loadrule;

import com.github.alextremp.loadrules.domain.dataextractor.DataExtractor;
import org.apache.commons.lang3.StringUtils;

public class EqualComparisonLoadRule extends ComparisonLoadRule {
  protected EqualComparisonLoadRule(DataExtractor leftDataExtractor, DataExtractor rightDataExtractor) {
    super(leftDataExtractor, rightDataExtractor);
  }

  @Override
  protected boolean evaluate(String left, String right) {
    return StringUtils.equals(left, right);
  }
}
