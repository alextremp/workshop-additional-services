package com.github.alextremp.loadrules.domain.dataextractor;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

public interface DataExtractor {
  String value(Datalayer datalayer);
}
