package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalServiceBuilder;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdditionalServiceJson2DomainMapper implements Mapper<AdditionalServiceJson, AdditionalService> {

    private final LoadRuleJson2DomainMapper loadRuleMapper;

    public AdditionalServiceJson2DomainMapper(LoadRuleJson2DomainMapper loadRuleMapper) {
        this.loadRuleMapper = loadRuleMapper;
    }

    @Override
    public Mono<AdditionalService> map(AdditionalServiceJson dto) {
        return Mono.defer(() -> {
            switch (dto.getType()) {
                case APPNEXUS:
                    if (dto.getAppnexus() == null) {
                        throw new IllegalArgumentException("appNexus is required with 'appnexus' type");
                    }
                    return mapAppnexus(dto);
                case MARKETPLACE:
                    if (dto.getMarketplace() == null) {
                        throw new IllegalArgumentException("appNexus is required with 'appnexus' type");
                    }
                    return mapMarketplace(dto);
                default:
                    throw new IllegalArgumentException("Type not supported: " + dto.getType());
            }
        });
    }

    private Mono<AdditionalServiceJson> mapLoadRules(LoadRule loadRules, AdditionalServiceJson dto) {
        return null;
    }

    private Mono<AdditionalService> mapAppnexus(AdditionalServiceJson additionalServiceJson) {
        return Mono.fromCallable(() -> AdditionalServiceBuilder.appnexus())
                .map(builder -> builder.withCode(additionalServiceJson.getAppnexus().getCode()))
                .flatMap(builder -> mapCommonAdditionalServiceFields(builder, additionalServiceJson));
    }

    private Mono<AdditionalService> mapMarketplace(AdditionalServiceJson additionalServiceJson) {
        return Mono.fromCallable(() -> AdditionalServiceBuilder.marketplace())
                .map(builder -> builder.withMarketplaceId(additionalServiceJson.getMarketplace().getId()))
                .map(builder -> builder.withCode(additionalServiceJson.getMarketplace().getCode()))
                .flatMap(builder -> mapCommonAdditionalServiceFields(builder, additionalServiceJson));
    }

    private Mono<AdditionalService> mapCommonAdditionalServiceFields(AdditionalServiceBuilder additionalServiceBuilder, AdditionalServiceJson additionalServiceJson) {
        return Mono.fromCallable(() -> additionalServiceBuilder.withId(additionalServiceJson.getId()))
                .flatMap(builder -> mapLoadRules(builder, additionalServiceJson.getLoadRules()))
                .map(builder -> builder.build());
    }

    private Mono<AdditionalServiceBuilder> mapLoadRules(AdditionalServiceBuilder builder, List<LoadRuleJson> loadRuleJsons) {
        return Flux.fromIterable(loadRuleJsons)
                .flatMap(loadRuleJson -> loadRuleMapper.map(loadRuleJson))
                .collectList()
                .map(builder::withLoadRules);
    }
}
