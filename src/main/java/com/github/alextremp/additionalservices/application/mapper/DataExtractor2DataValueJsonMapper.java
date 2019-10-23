package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.DataValueJson;
import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.DataExtractor;
import reactor.core.publisher.Mono;

public class DataExtractor2DataValueJsonMapper implements Mapper<DataExtractor, DataValueJson> {
  @Override
  public Mono<DataValueJson> map(DataExtractor input) {
    return null;
  }
}
