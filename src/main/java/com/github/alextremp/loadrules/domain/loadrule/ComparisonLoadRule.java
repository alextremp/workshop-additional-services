package com.github.alextremp.loadrules.domain.loadrule;

import com.github.alextremp.loadrules.domain.dataextractor.DataExtractor;
import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

import java.util.Objects;

public abstract class ComparisonLoadRule implements LoadRule {
  private final DataExtractor leftDataExtractor;
  private final DataExtractor rightDataExtractor;

  protected ComparisonLoadRule(DataExtractor leftDataExtractor, DataExtractor rightDataExtractor) {
    Objects.requireNonNull(leftDataExtractor, "Left data extractor cannot be null");
    Objects.requireNonNull(rightDataExtractor, "Right data extractor cannot be null");
    this.leftDataExtractor = leftDataExtractor;
    this.rightDataExtractor = rightDataExtractor;
  }

  @Override
  public boolean evaluate(Datalayer datalayer) {
    return evaluate(leftDataExtractor.value(datalayer), rightDataExtractor.value(datalayer));
  }

  protected abstract boolean evaluate(String left, String right);
}
