package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.*;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalServiceBuilder;
import com.github.alextremp.additionalservices.domain.additionalservice.AppNexusAdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.MarketplaceAdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdditionalServiceMapper {

    private final LoadRuleMapper loadRuleMapper;
    private final FluentMap<SubAdditionalServiceMapper> subAdditionalServiceMappers;

    public AdditionalServiceMapper(LoadRuleMapper loadRuleMapper) {
        this.loadRuleMapper = loadRuleMapper;
        this.subAdditionalServiceMappers = new FluentMap<SubAdditionalServiceMapper>()
                .fluentPut(AppNexusAdditionalService.class.getName(), new AppNexusDomainMapper())
                .fluentPut(MarketplaceAdditionalService.class.getName(), new MarketplaceDomainMapper());
    }

    public Mono<AdditionalService> fromDTO(AdditionalServiceDTO dto) {
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

    public Mono<AdditionalServiceDTO> fromDomain(AdditionalService additionalService) {
        return Mono.fromCallable(() -> new AdditionalServiceDTO().setId(additionalService.id()))
                .flatMap(dto -> subAdditionalServiceMappers.getNotNull(additionalService.getClass().getName()).map(additionalService, dto))
                //.flatMap(dto -> mapLoadRules(additionalService.loadRule(), dto))
                ;
    }

    private Mono<AdditionalServiceDTO> mapLoadRules(LoadRule loadRules, AdditionalServiceDTO dto) {
        return null;
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

    private interface SubAdditionalServiceMapper {
        Mono<AdditionalServiceDTO> map(AdditionalService additionalService, AdditionalServiceDTO dto);
    }

    private class AppNexusDomainMapper implements SubAdditionalServiceMapper {
        @Override
        public Mono<AdditionalServiceDTO> map(AdditionalService additionalService, AdditionalServiceDTO dto) {
            return Mono.fromCallable(() -> (AppNexusAdditionalService) additionalService)
                    .map(appNexusAdditionalService -> dto
                            .setType(TypeDTO.APPNEXUS)
                            .setAppnexus(new AppnexusDTO()
                                    .setCode(appNexusAdditionalService.code())));
        }
    }

    private class MarketplaceDomainMapper implements SubAdditionalServiceMapper {
        @Override
        public Mono<AdditionalServiceDTO> map(AdditionalService additionalService, AdditionalServiceDTO dto) {
            return Mono.fromCallable(() -> (MarketplaceAdditionalService) additionalService)
                    .map(marketplaceAdditionalService -> dto
                            .setType(TypeDTO.MARKETPLACE)
                            .setMarketplace(new MarketplaceDTO()
                                    .setCode(marketplaceAdditionalService.code())
                                    .setId(marketplaceAdditionalService.marketplaceId())));
        }
    }

}
