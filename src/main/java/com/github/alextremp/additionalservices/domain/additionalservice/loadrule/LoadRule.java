package com.github.alextremp.additionalservices.domain.additionalservice.loadrule;

import com.github.alextremp.additionalservices.domain.additionalservice.datalayer.Datalayer;

public interface LoadRule {
  boolean evaluate(Datalayer datalayer);
}
