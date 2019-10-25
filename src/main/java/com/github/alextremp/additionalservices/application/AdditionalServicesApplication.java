package com.github.alextremp.additionalservices.application;

import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteRequest;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteResponse;
import com.github.alextremp.additionalservices.application.service.savesite.io.SaveSiteRequest;
import com.github.alextremp.additionalservices.application.service.savesite.io.SaveSiteResponse;
import reactor.core.publisher.Mono;

public interface AdditionalServicesApplication {

  Mono<GetSiteResponse> getSite(GetSiteRequest request);

  Mono<SaveSiteResponse> saveSite(SaveSiteRequest request);
}
