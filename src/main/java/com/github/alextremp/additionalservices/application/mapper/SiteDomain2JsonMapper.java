package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceJson;
import com.github.alextremp.additionalservices.application.dto.SiteJson;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.site.Site;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class SiteDomain2JsonMapper implements Mapper<Site, SiteJson> {

    private final AdditionalServiceDomain2JsonMapper additionalServiceMapper;

    public SiteDomain2JsonMapper(AdditionalServiceDomain2JsonMapper additionalServiceMapper) {
        this.additionalServiceMapper = additionalServiceMapper;
    }

    @Override
    public Mono<SiteJson> map(Site site) {
        return Mono.fromCallable(() -> new SiteJson())
                .flatMap(siteJson -> map(site, siteJson));
    }

    private Mono<SiteJson> map(Site site, SiteJson siteJson) {
        return Mono.just(siteJson)
                .map(dto -> dto.setId(site.getId()))
                .map(dto -> dto.setVersion(site.getVersion()))
                .map(dto -> site.getAdditionalServices())
                .flatMap(this::mapAdditionalServices)
                .map(siteJson::setAdditionalServices);
    }

    private Mono<List<AdditionalServiceJson>> mapAdditionalServices(List<AdditionalService> additionalServices) {
        return Flux.fromIterable(additionalServices)
                .flatMap(additionalServiceMapper::map)
                .collectList();
    }
}
