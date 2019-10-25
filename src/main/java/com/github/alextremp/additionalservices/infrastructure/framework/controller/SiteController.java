package com.github.alextremp.additionalservices.infrastructure.framework.controller;

import com.github.alextremp.additionalservices.application.AdditionalServicesApplication;
import com.github.alextremp.additionalservices.application.dto.SiteJson;
import com.github.alextremp.additionalservices.application.service.getsite.io.GetSiteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/site")
public class SiteController {

  private final AdditionalServicesApplication additionalServicesApplication;

  @Autowired
  public SiteController(AdditionalServicesApplication additionalServicesApplication) {
    this.additionalServicesApplication = additionalServicesApplication;
  }

  @GetMapping("/{siteId}")
  public Mono<ResponseEntity<SiteJson>> getSite(@PathVariable String siteId) {
    return Mono.fromCallable(() -> new GetSiteRequest(siteId))
        .subscribeOn(Schedulers.parallel())
        .flatMap(additionalServicesApplication::getSite)
        .map(response -> ResponseEntity.ok(response.getSite()));
  }
}
