package com.github.alextremp.additionalservices.infrastructure.framework.configuration;

import com.github.alextremp.additionalservices.application.mapper.SiteDomain2JsonMapper;
import com.github.alextremp.additionalservices.application.service.getsite.GetSiteUseCase;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.additionalservices.domain.site.SiteRepository;
import com.github.alextremp.additionalservices.infrastructure.repository.InMemorySiteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteConfiguration {

  @Bean
  SiteRepository siteRepository() {
    return new InMemorySiteRepository();
  }

  @Bean
  GetSiteUseCase getSiteUseCase(SiteRepository siteRepository, SiteDomain2JsonMapper siteDomain2JsonMapper) {
    return new GetSiteUseCase(siteRepository, siteDomain2JsonMapper);
  }
}
