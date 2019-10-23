package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.DataExtractor;
import org.apache.commons.lang3.StringUtils;

public class EqualLoadRule extends ComparisonLoadRule {
  public EqualLoadRule(DataExtractor leftDataExtractor, DataExtractor rightDataExtractor) {
    super(leftDataExtractor, rightDataExtractor);
  }

  @Override
  protected boolean evaluate(String left, String right) {
    return StringUtils.equals(left, right);
  }
}
