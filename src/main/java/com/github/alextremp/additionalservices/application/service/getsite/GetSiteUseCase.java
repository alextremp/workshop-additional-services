package com.github.alextremp.additionalservices.application.service.getsite;

import com.github.alextremp.additionalservices.application.mapper.SiteDomain2JsonMapper;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteRequest;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteResponse;
import com.github.alextremp.additionalservices.domain.site.SiteRepository;
import com.github.alextremp.additionalservices.domain.site.error.SiteNotFoundError;
import reactor.core.publisher.Mono;

public class GetSiteUseCase {

  private final SiteRepository siteRepository;
  private final SiteDomain2JsonMapper siteDomain2JsonMapper;

  public GetSiteUseCase(SiteRepository siteRepository, SiteDomain2JsonMapper siteDomain2JsonMapper) {
    this.siteRepository = siteRepository;
    this.siteDomain2JsonMapper = siteDomain2JsonMapper;
  }

  public Mono<GetSiteResponse> execute(GetSiteRequest request) {
    return siteRepository.findById(request.getSiteId())
        .switchIfEmpty(Mono.error(new SiteNotFoundError(request.getSiteId())))
        .flatMap(siteDomain2JsonMapper::map)
        .map(GetSiteResponse::new);
  }
}
