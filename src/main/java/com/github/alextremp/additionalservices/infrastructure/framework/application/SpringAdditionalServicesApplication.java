package com.github.alextremp.additionalservices.infrastructure.framework.application;

import com.github.alextremp.additionalservices.application.AdditionalServicesApplication;
import com.github.alextremp.additionalservices.application.service.getsite.GetSiteUseCase;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteRequest;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteResponse;
import com.github.alextremp.additionalservices.application.service.savesite.io.SaveSiteRequest;
import com.github.alextremp.additionalservices.application.service.savesite.io.SaveSiteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SpringAdditionalServicesApplication implements AdditionalServicesApplication {

  private final GetSiteUseCase getSiteUseCase;

  @Autowired
  public SpringAdditionalServicesApplication(GetSiteUseCase getSiteUseCase) {
    this.getSiteUseCase = getSiteUseCase;
  }

  @Override
  public Mono<GetSiteResponse> getSite(GetSiteRequest request) {
    return getSiteUseCase.execute(request);
  }

  @Override
  public Mono<SaveSiteResponse> saveSite(SaveSiteRequest request) {
    return Mono.error(new UnsupportedOperationException("not implemented yet"));
  }
}
