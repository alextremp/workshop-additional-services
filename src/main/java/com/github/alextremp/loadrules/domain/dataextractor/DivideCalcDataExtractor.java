package com.github.alextremp.loadrules.domain.dataextractor;

public class DivideCalcDataExtractor extends CalcDataExtractor {

  protected DivideCalcDataExtractor(DataExtractor left, DataExtractor right) {
    super(left, right);
  }

  @Override
  protected String calc(String left, String right) {
    return Integer.toString(Integer.valueOf(left) / Integer.valueOf(right));
  }
}
