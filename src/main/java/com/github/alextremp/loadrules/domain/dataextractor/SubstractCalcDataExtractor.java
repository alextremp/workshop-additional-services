package com.github.alextremp.loadrules.domain.dataextractor;

public class SubstractCalcDataExtractor extends CalcDataExtractor {

  protected SubstractCalcDataExtractor(DataExtractor left, DataExtractor right) {
    super(left, right);
  }

  @Override
  protected String calc(String left, String right) {
    return Integer.toString(Integer.valueOf(left) - Integer.valueOf(right));
  }
}
