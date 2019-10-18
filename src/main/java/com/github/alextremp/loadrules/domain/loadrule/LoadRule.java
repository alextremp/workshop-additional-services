package com.github.alextremp.loadrules.domain.loadrule;

import com.github.alextremp.loadrules.domain.datalayer.Datalayer;

public interface LoadRule {
  boolean evaluate(Datalayer datalayer);
}
