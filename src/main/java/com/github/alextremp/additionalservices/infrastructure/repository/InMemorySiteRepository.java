package com.github.alextremp.additionalservices.infrastructure.repository;

import com.github.alextremp.additionalservices.application.mapper.FluentMap;
import com.github.alextremp.additionalservices.domain.additionalservice.AdditionalServiceBuilder;
import com.github.alextremp.additionalservices.domain.additionalservice.dataextractor.FixedValueDataExtractor;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.EqualLoadRule;
import com.github.alextremp.additionalservices.domain.additionalservice.loadrule.NotLoadRule;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.additionalservices.domain.site.SiteBuilder;
import com.github.alextremp.additionalservices.domain.site.SiteRepository;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

public class InMemorySiteRepository implements SiteRepository {

  private static final Map<String, Site> IN_MEMORY = new FluentMap<String, Site>()
      .fluentPut("fc", SiteBuilder.create()
          .withId("fc")
          .withVersion("111")
          .withAdditionalServices(
              Arrays.asList(
                  AdditionalServiceBuilder.appnexus()
                      .withCode("code_appn")
                      .withId("in_memory_1")
                      .withLoadRules(
                          Arrays.asList(
                              new NotLoadRule(
                                  new EqualLoadRule(
                                      new FixedValueDataExtractor("1"),
                                      new FixedValueDataExtractor("2")
                                  )
                              )
                          )
                      )
                      .build()
              )
          )
          .build()
      );

  @Override
  public Mono<Site> findById(String id) {
    return Mono.fromCallable(() -> IN_MEMORY.containsKey(id))
        .flatMap(has -> has ? Mono.just(IN_MEMORY.get(id)) : Mono.empty());
  }
}
