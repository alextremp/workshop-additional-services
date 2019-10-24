package com.github.alextremp.additionalservices.application.service.getsite;

import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteRequest;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteResponse;
import com.github.alextremp.additionalservices.domain.site.SiteRepository;
import reactor.core.publisher.Mono;

public class GetSiteUseCase {

  private final SiteRepository siteRepository;

  public GetSiteUseCase(SiteRepository siteRepository) {
    this.siteRepository = siteRepository;
  }

  public Mono<GetSiteResponse> execute(GetSiteRequest request) {
    return siteRepository.findById(request.getSiteId())
        .map(site -> );
  }
}
