package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceJson;
import com.github.alextremp.additionalservices.application.dto.SiteJson;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.additionalservices.domain.site.SiteBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SiteJson2DomainMapper implements Mapper<SiteJson, Site> {

    private final AdditionalServiceJson2DomainMapper additionalServiceMapper;

    public SiteJson2DomainMapper(AdditionalServiceJson2DomainMapper additionalServiceMapper) {
        this.additionalServiceMapper = additionalServiceMapper;
    }

    @Override
    public Mono<Site> map(SiteJson dto) {
        return Mono.fromCallable(() -> SiteBuilder.create())
                .map(builder -> builder
                        .withId(dto.getId())
                        .withVersion(dto.getVersion()))
                .flatMap(builder -> mapAdditionalServices(builder, dto.getAdditionalServices()))
                .map(builder -> builder.build());
    }

    private Mono<SiteBuilder> mapAdditionalServices(SiteBuilder siteBuilder, List<AdditionalServiceJson> dtoList) {
        return Flux.fromIterable(dtoList)
                .flatMap(additionalServiceMapper::map)
                .collectList()
                .map(additionalServices -> siteBuilder.withAdditionalServices(additionalServices));
    }
}
