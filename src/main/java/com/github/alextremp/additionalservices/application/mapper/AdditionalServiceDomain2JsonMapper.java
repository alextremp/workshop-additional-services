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


    private final FluentMap<String, SubAdditionalServiceMapper> subAdditionalServiceMappers;

    public AdditionalServiceDomain2JsonMapper() {
        this.subAdditionalServiceMappers = new FluentMap<String, SubAdditionalServiceMapper>()
                .fluentPut(AppNexusAdditionalService.class.getName(), new AppNexusDomainMapper())
                .fluentPut(MarketplaceAdditionalService.class.getName(), new MarketplaceDomainMapper());
    }

    @Override
    public Mono<AdditionalServiceJson> map(AdditionalService additionalService) {
        return Mono.fromCallable(() -> new AdditionalServiceJson().setId(additionalService.id()))
                .flatMap(dto -> subAdditionalServiceMappers.getNotNull(additionalService.getClass().getName()).map(additionalService, dto))
                //.flatMap(dto -> mapLoadRules(additionalService.loadRule(), dto))
                ;
    }

    private interface SubAdditionalServiceMapper {
        Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson dto);
    }

    private class AppNexusDomainMapper implements SubAdditionalServiceMapper {
        @Override
        public Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson dto) {
            return Mono.fromCallable(() -> (AppNexusAdditionalService) additionalService)
                    .map(appNexusAdditionalService -> dto
                            .setType(TypeJson.APPNEXUS)
                            .setAppnexus(new AppnexusJson()
                                    .setCode(appNexusAdditionalService.code())));
        }
    }

    private class MarketplaceDomainMapper implements SubAdditionalServiceMapper {
        @Override
        public Mono<AdditionalServiceJson> map(AdditionalService additionalService, AdditionalServiceJson dto) {
            return Mono.fromCallable(() -> (MarketplaceAdditionalService) additionalService)
                    .map(marketplaceAdditionalService -> dto
                            .setType(TypeJson.MARKETPLACE)
                            .setMarketplace(new MarketplaceJson()
                                    .setCode(marketplaceAdditionalService.code())
                                    .setId(marketplaceAdditionalService.marketplaceId())));
        }
    }
}
