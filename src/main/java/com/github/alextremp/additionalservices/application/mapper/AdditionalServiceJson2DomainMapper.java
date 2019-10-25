package com.github.alextremp.additionalservices.application.mapper;

import com.github.alextremp.additionalservices.application.dto.AdditionalServiceJson;
import com.github.alextremp.additionalservices.application.dto.LoadRuleJson;
import com.github.alextremp.additionalservices.application.dto.TypeJson;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalService;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalServiceBuilder;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.LoadRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static com.github.alextremp.additionalservices.application.dto.TypeJson.APPNEXUS;
import static com.github.alextremp.additionalservices.application.dto.TypeJson.MARKETPLACE;

public class AdditionalServiceJson2DomainMapper implements Mapper<AdditionalServiceJson, AdditionalService> {

  private final LoadRuleJson2DomainMapper loadRuleJson2DomainMapper;
  private final FluentMap<TypeJson, TypeJsonMapper> typeJsonMappers;


  public AdditionalServiceJson2DomainMapper(LoadRuleJson2DomainMapper loadRuleJson2DomainMapper) {
    this.loadRuleJson2DomainMapper = loadRuleJson2DomainMapper;
    this.typeJsonMappers = new FluentMap<TypeJson, TypeJsonMapper>()
        .fluentPut(APPNEXUS, new AppnexusMapper())
        .fluentPut(MARKETPLACE, new MarketplaceMapper());
  }

  @Override
  public Mono<AdditionalService> map(AdditionalServiceJson additionalServiceJson) {
    return Mono.fromCallable(() -> typeJsonMappers.getNotNull(additionalServiceJson.getType()))
        .flatMap(typeJsonMapper -> typeJsonMapper.map(additionalServiceJson))
        .flatMap(builder -> Mono.zip(
            Mono.just(builder),
            mapLoadRules(additionalServiceJson.getLoadRules())
        ))
        .map(tuple -> tuple.getT1().withLoadRules(tuple.getT2()))
        .map(builder -> builder
            .withId(additionalServiceJson.getId())
            .withEnabled(additionalServiceJson.getEnabled())
            .build()
        );
  }

  private Mono<List<LoadRule>> mapLoadRules(List<LoadRuleJson> loadRules) {
    return Flux.fromIterable(loadRules)
        .flatMap(loadRuleJson -> loadRuleJson2DomainMapper.map(loadRuleJson))
        .collectList();
  }

  private abstract class TypeJsonMapper implements Mapper<AdditionalServiceJson, AdditionalServiceBuilder> {

    @Override
    public Mono<AdditionalServiceBuilder> map(AdditionalServiceJson additionalServiceJson) {
      return Mono.fromCallable(() -> initBuilder(additionalServiceJson));
    }

    protected abstract AdditionalServiceBuilder initBuilder(AdditionalServiceJson additionalServiceJson);
  }

  private class AppnexusMapper extends TypeJsonMapper {
    @Override
    protected AdditionalServiceBuilder initBuilder(AdditionalServiceJson additionalServiceJson) {
      Objects.requireNonNull(additionalServiceJson.getAppnexus(), "appnexus is required with 'appnexus' type");
      return AdditionalServiceBuilder.appnexus()
          .withCode(additionalServiceJson.getAppnexus().getCode());
    }
  }

  private class MarketplaceMapper extends TypeJsonMapper {
    @Override
    protected AdditionalServiceBuilder initBuilder(AdditionalServiceJson additionalServiceJson) {
      Objects.requireNonNull(additionalServiceJson.getMarketplace(), "marketplace is required with 'marketplace' type");
      return AdditionalServiceBuilder.marketplace()
          .withMarketplaceId(additionalServiceJson.getMarketplace().getId())
          .withCode(additionalServiceJson.getMarketplace().getCode());
    }
  }
}
