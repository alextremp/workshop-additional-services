package com.github.alextremp.additionalservices.application;

import com.github.alextremp.additionalservices.application.service.savesite.io.SaveSiteRequest;
import com.github.alextremp.additionalservices.application.service.savesite.io.SaveSiteResponse;
import reactor.core.publisher.Mono;

public interface AdditionalServicesApplication {

  Mono<SaveSiteResponse> saveSite(SaveSiteRequest request);
}
