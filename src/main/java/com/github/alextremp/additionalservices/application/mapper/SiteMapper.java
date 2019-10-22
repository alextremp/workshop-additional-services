package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceDTO;
import com.github.alextremp.additionalservices.application.dto.SiteDTO;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.additionalservices.domain.site.SiteBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SiteMapper {

    private final AdditionalServiceMapper additionalServiceMapper;

    public SiteMapper(AdditionalServiceMapper additionalServiceMapper) {
        this.additionalServiceMapper = additionalServiceMapper;
    }

    public Mono<Site> fromDTO(SiteDTO dto) {
        return Mono.fromCallable(() -> SiteBuilder.create())
                .map(builder -> builder
                        .withId(dto.getId())
                        .withVersion(dto.getVersion()))
                .flatMap(builder -> mapAdditionalServices(builder, dto.getAdditionalServices()))
                .map(builder -> builder.build());
    }

    public Mono<SiteDTO> fromDomain(Site site) {
        return Mono.fromCallable(() -> new SiteDTO()
                .setId(site.getId())
                .setVersion(site.getVersion()))
                .flatMap(siteDTO -> mapAdditionalServices(site.getAdditionalServices(), siteDTO));
    }

    private Mono<SiteDTO> mapAdditionalServices(List<AdditionalService> additionalServices, SiteDTO siteDTO) {
        return Flux.fromIterable(additionalServices)
                .flatMap(additionalServiceMapper::fromDomain)
                .collectList()
                .map(siteDTO::setAdditionalServices);
    }

    private Mono<SiteBuilder> mapAdditionalServices(SiteBuilder siteBuilder, List<AdditionalServiceDTO> dtoList) {
        return Flux.fromIterable(dtoList)
                .flatMap(additionalServiceMapper::fromDTO)
                .collectList()
                .map(additionalServices -> siteBuilder.withAdditionalServices(additionalServices));
    }
}
