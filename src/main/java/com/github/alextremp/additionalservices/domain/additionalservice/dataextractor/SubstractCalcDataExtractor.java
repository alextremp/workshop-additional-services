package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

public class SubstractCalcDataExtractor extends CalcDataExtractor {

  public SubstractCalcDataExtractor(DataExtractor left, DataExtractor right) {
    super(left, right);
  }

  @Override
  protected String calc(String left, String right) {
    return Integer.toString(Integer.valueOf(left) - Integer.valueOf(right));
  }
}
