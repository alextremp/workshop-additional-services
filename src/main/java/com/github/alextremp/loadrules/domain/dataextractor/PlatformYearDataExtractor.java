package com.github.alextremp.loadrules.domain.dataextractor;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

import java.time.LocalDate;

public class PlatformYearDataExtractor implements DataExtractor {

  @Override
  public String value(Datalayer datalayer) {
    return Integer.toString(LocalDate.now().getYear());
  }
}
