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

  private final FluentMap<Class<? extends AdditionalService>, SubAdditionalServiceMapper> subClassMappers;

  public AdditionalServiceDomain2JsonMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
    this.loadRuleDomain2JsonMapper = loadRuleDomain2JsonMapper;
    this.subClassMappers = new FluentMap<Class<? extends AdditionalService>, SubAdditionalServiceMapper>()
        .fluentPut(AppNexusAdditionalService.class, new AppNexusDomainMapper())
        .fluentPut(MarketplaceAdditionalService.class, new MarketplaceDomainMapper());
  }

  @Override
  public Mono<AdditionalServiceJson> map(AdditionalService additionalService) {
    return Mono.fromCallable(() -> subClassMappers.getNotNull(additionalService.getClass()))
        .flatMap(subAdditionalServiceMapper -> subAdditionalServiceMapper.map(additionalService))
        .map(dto -> dto.setId(additionalService.id()))
        .flatMap(dto -> Mono.zip(
            Mono.just(dto),
            loadRuleDomain2JsonMapper.map(additionalService.loadRule()))
        )
        .map(tuple -> tuple.getT1().setLoadRules(tuple.getT2().getAnd()));
  }

  private interface SubAdditionalServiceMapper extends Mapper<AdditionalService, AdditionalServiceJson> {
  }

  private class AppNexusDomainMapper implements SubAdditionalServiceMapper {
    @Override
    public Mono<AdditionalServiceJson> map(AdditionalService additionalService) {
      return Mono.fromCallable(() -> (AppNexusAdditionalService) additionalService)
          .map(appNexusAdditionalService -> new AdditionalServiceJson()
              .setType(TypeJson.APPNEXUS)
              .setAppnexus(new AppnexusJson()
                  .setCode(appNexusAdditionalService.code())
              )
          );
    }
  }

  private class MarketplaceDomainMapper implements SubAdditionalServiceMapper {
    @Override
    public Mono<AdditionalServiceJson> map(AdditionalService additionalService) {
      return Mono.fromCallable(() -> (MarketplaceAdditionalService) additionalService)
          .map(marketplaceAdditionalService -> new AdditionalServiceJson()
              .setType(TypeJson.MARKETPLACE)
              .setMarketplace(new MarketplaceJson()
                  .setCode(marketplaceAdditionalService.code())
                  .setId(marketplaceAdditionalService.marketplaceId())
              )
          );
    }
  }
}
