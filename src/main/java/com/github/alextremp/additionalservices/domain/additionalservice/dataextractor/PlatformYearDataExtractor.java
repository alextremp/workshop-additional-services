package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.time.LocalDate;

public class PlatformYearDataExtractor implements DataExtractor {

  @Override
  public String value(Datalayer datalayer) {
    return Integer.toString(LocalDate.now().getYear());
  }
}
