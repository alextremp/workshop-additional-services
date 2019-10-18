package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.util.Objects;

public abstract class CalcDataExtractor implements DataExtractor {

  private final DataExtractor left;
  private final DataExtractor right;

  protected CalcDataExtractor(DataExtractor left, DataExtractor right) {
    Objects.requireNonNull(left, "Left data extractor cannot be null");
    Objects.requireNonNull(right, "Right data extractor cannot be null");
    this.left = left;
    this.right = right;
  }

  protected abstract String calc(String left, String right);

  @Override
  public String value(Datalayer datalayer) {
    return calc(left.value(datalayer), right.value(datalayer));
  }

  public DataExtractor left() {
    return left;
  }

  public DataExtractor right() {
    return right;
  }
}
