package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceDTO;
import com.github.alextremp.additionalservices.application.dto.LoadRuleDTO;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalServiceBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdditionalServiceMapper {

    private final LoadRuleMapper loadRuleMapper;

    public AdditionalServiceMapper(LoadRuleMapper loadRuleMapper) {
        this.loadRuleMapper = loadRuleMapper;
    }

    public Mono<AdditionalService> from(AdditionalServiceDTO dto) {
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

    private Mono<AdditionalService> mapAppnexus(AdditionalServiceDTO additionalServiceDTO) {
        return Mono.fromCallable(() -> AdditionalServiceBuilder.appnexus())
                .map(builder -> builder.withCode(additionalServiceDTO.getAppnexus().getCode()))
                .flatMap(builder -> mapCommonAdditionalServiceFields(builder, additionalServiceDTO));
    }

    private Mono<AdditionalService> mapMarketplace(AdditionalServiceDTO additionalServiceDTO) {
        return Mono.fromCallable(() -> AdditionalServiceBuilder.marketplace())
                .map(builder -> builder.withMarketplaceId(additionalServiceDTO.getMarketplace().getId()))
                .map(builder -> builder.withCode(additionalServiceDTO.getMarketplace().getCode()))
                .flatMap(builder -> mapCommonAdditionalServiceFields(builder, additionalServiceDTO));
    }

    private Mono<AdditionalService> mapCommonAdditionalServiceFields(AdditionalServiceBuilder additionalServiceBuilder, AdditionalServiceDTO additionalServiceDTO) {
        return Mono.fromCallable(() -> additionalServiceBuilder.withId(additionalServiceDTO.getId()))
                .flatMap(builder -> mapLoadRules(builder, additionalServiceDTO.getLoadRules()))
                .map(builder -> builder.build());
    }

    private Mono<AdditionalServiceBuilder> mapLoadRules(AdditionalServiceBuilder builder, List<LoadRuleDTO> loadRuleDTOs) {
        return Flux.fromIterable(loadRuleDTOs)
                .flatMap(loadRuleDTO -> loadRuleMapper.from(loadRuleDTO))
                .collectList()
                .map(builder::withLoadRules);
    }
}
