package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.SiteJson;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.additionalservices.domain.site.SiteBuilder;
import reactor.core.publisher.Mono;

public class SiteJson2DomainMapper implements Mapper<SiteJson, Site> {

  private final AdditionalServiceJson2DomainMapper additionalServiceMapper;

  public SiteJson2DomainMapper(AdditionalServiceJson2DomainMapper additionalServiceMapper) {
    this.additionalServiceMapper = additionalServiceMapper;
  }

  @Override
  public Mono<Site> map(SiteJson dto) {
    return Mono.fromCallable(() -> dto.getAdditionalServices())
        .flatMapIterable(additionalServiceJsons -> additionalServiceJsons)
        .flatMap(additionalServiceMapper::map)
        .collectList()
        .map(additionalServices -> SiteBuilder.create()
            .withId(dto.getId())
            .withVersion(dto.getVersion())
            .withAdditionalServices(additionalServices)
            .build()
        );

  }
}
