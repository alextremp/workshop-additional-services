package com.github.alextremp.additionalservices.domain.additionalservice.dataextractor;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

public interface DataExtractor {
  String value(Datalayer datalayer);
}
