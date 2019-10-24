package com.github.alextremp.additionalservices.domain.site;

import reactor.core.publisher.Mono;

public interface SiteRepository {
  Mono<Site> findById(String id);
}
