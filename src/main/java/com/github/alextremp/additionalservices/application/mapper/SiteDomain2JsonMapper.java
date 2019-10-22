package com.github.alextremp.additionalservices.application.mapper;

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
        return Mono.fromCallable(() -> new SiteJson()
                .setId(site.getId())
                .setVersion(site.getVersion()))
                .flatMap(siteDTO -> mapAdditionalServices(site.getAdditionalServices(), siteDTO));
    }

    private Mono<SiteJson> mapAdditionalServices(List<AdditionalService> additionalServices, SiteJson siteJson) {
        return Flux.fromIterable(additionalServices)
                .flatMap(additionalServiceMapper::map)
                .collectList()
                .map(siteJson::setAdditionalServices);
    }
}
