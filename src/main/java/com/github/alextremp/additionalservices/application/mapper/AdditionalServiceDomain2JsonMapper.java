package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceJson;
import com.github.alextremp.additionalservices.application.dto.AppnexusJson;
import com.github.alextremp.additionalservices.application.dto.MarketplaceJson;
import com.github.alextremp.additionalservices.application.dto.TypeJson;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.AppNexusAdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.MarketplaceAdditionalService;
import reactor.core.publisher.Mono;

public class AdditionalServiceDomain2JsonMapper implements Mapper<AdditionalService, AdditionalServiceJson> {

    private final LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper;

    private final FluentMap<String, SubAdditionalServiceMapper> subClassMappers;

    public AdditionalServiceDomain2JsonMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
        this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
        this.subClassMappers = new FluentMap<String, SubAdditionalServiceMapper>()
                .fluentPut(AppNexusAdditionalService.class.getName(), new AppNexusDomainMapper())
                .fluentPut(MarketplaceAdditionalService.class.getName(), new MarketplaceDomainMapper());
    }

    @Override
    public Mono<AdditionalServiceJson> map(AdditionalService additionalService) {
        return Mono.fromCallable(() -> new AdditionalServiceJson())
                .flatMap(dto -> map(additionalService, dto));
    }

    private Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson additionalServiceJson) {
        return Mono.just(additionalServiceJson)
                .map(dto -> dto.setId(additionalService.id()))
                .map(dto -> subClassMappers.getNotNull(additionalService.getClass().getName()))
                .flatMap(subAdditionalServiceMapper -> subAdditionalServiceMapper.map(additionalService, additionalServiceJson))
                .flatMap(dto -> loadRuleDomain2JsonMapper.map(additionalService.loadRule()))
                .map(loadRuleJson -> additionalServiceJson.setLoadRules(loadRuleJson.getAnd()));
    }

    private interface SubAdditionalServiceMapper {
        Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson dto);
    }

    private class AppNexusDomainMapper implements SubAdditionalServiceMapper {
        @Override
        public Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson dto) {
            return Mono.just((AppNexusAdditionalService) additionalService)
                    .map(appNexusAdditionalService -> dto
                            .setType(TypeJson.APPNEXUS)
                            .setAppnexus(new AppnexusJson()
                                    .setCode(appNexusAdditionalService.code())));
        }
    }

    private class MarketplaceDomainMapper implements SubAdditionalServiceMapper {
        @Override
        public Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson dto) {
            return Mono.just((MarketplaceAdditionalService) additionalService)
                    .map(marketplaceAdditionalService -> dto
                            .setType(TypeJson.MARKETPLACE)
                            .setMarketplace(new MarketplaceJson()
                                    .setCode(marketplaceAdditionalService.code())
                                    .setId(marketplaceAdditionalService.marketplaceId())));
        }
    }
}
