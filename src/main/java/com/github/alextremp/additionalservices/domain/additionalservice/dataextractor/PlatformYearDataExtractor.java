package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

import java.time.LocalDate;

public class PlatformYearDataExtractor implements PlatformDataExtractor {

  public static final String PLATFORM_KEY = "year";

  @Override
  public String value(Datalayer datalayer) {
    return Integer.toString(LocalDate.now().getYear());
  }
}
