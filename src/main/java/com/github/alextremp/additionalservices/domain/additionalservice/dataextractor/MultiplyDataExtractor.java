package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

public class MultiplyDataExtractor extends CalcDataExtractor {

  public MultiplyDataExtractor(DataExtractor left, DataExtractor right) {
    super(left, right);
  }

  @Override
  protected String calc(String left, String right) {
    return Integer.toString(Integer.valueOf(left) * Integer.valueOf(right));
  }
}
