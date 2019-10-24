package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.SiteJson;
import com.github.alextremp.additionalservices.domain.site.Site;
import reactor.core.publisher.Mono;

public class SiteDomain2JsonMapper implements Mapper<Site, SiteJson> {

  private final AdditionalServiceDomain2JsonMapper additionalServiceMapper;

  public SiteDomain2JsonMapper(AdditionalServiceDomain2JsonMapper additionalServiceMapper) {
    this.additionalServiceMapper = additionalServiceMapper;
  }

  @Override
  public Mono<SiteJson> map(Site site) {
    return Mono.fromCallable(() -> site.getAdditionalServices())
        .flatMapIterable(additionalServices -> additionalServices)
        .flatMap(additionalServiceMapper::map)
        .collectList()
        .map(additionalServiceJsons -> new SiteJson()
            .setId(site.getId())
            .setVersion(site.getVersion())
            .setAdditionalServices(additionalServiceJsons)
        );
  }
}
