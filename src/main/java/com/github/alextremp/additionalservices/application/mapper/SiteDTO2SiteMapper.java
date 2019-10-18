package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceDTO;
import com.github.alextremp.additionalservices.application.dto.SiteDTO;
import com.github.alextremp.additionalservices.application.dto.TypeDTO;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.additionalservices.domain.site.SiteBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SiteDTO2SiteMapper {

  public Mono<Site> map(SiteDTO dto) {
    return Mono.fromCallable(() -> SiteBuilder.create())
        .map(siteBuilder -> siteBuilder.withId(dto.getId()))
        .map(siteBuilder -> siteBuilder.withVersion(dto.getVersion()))
        .flatMap(siteBuilder -> map(dto.getAdditionalServices())
            .collectList()
            .map(additionalServices -> siteBuilder.withAdditionalServices(additionalServices))
        )
        .map(siteBuilder -> siteBuilder.build());
  }

  private Flux<AdditionalService> map(List<AdditionalServiceDTO> dtoList) {
    return Flux.fromIterable(dtoList)
        .flatMap(this::map);
  }

  private Mono<AdditionalService> map(AdditionalServiceDTO additionalServiceDTO) {
    return Mono.fromCallable(() -> TypeDTO.APPNEXUS.equals(additionalServiceDTO.getType()));
  }
}
