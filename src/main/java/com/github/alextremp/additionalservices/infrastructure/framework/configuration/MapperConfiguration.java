package com.github.alextremp.additionalservices.infrastructure.framework.configuration;

import com.github.alextremp.additionalservices.application.mapper.AdditionalServiceDomain2JsonMapper;
import com.github.alextremp.additionalservices.application.mapper.DataExtractor2DataValueJsonMapper;
import com.github.alextremp.additionalservices.application.mapper.LoadRuleDomain2JsonMapper;
import com.github.alextremp.additionalservices.application.mapper.SiteDomain2JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

  @Bean
  SiteDomain2JsonMapper siteDomain2JsonMapper(AdditionalServiceDomain2JsonMapper additionalServiceDomain2JsonMapper) {
    return new SiteDomain2JsonMapper(additionalServiceDomain2JsonMapper);
  }

  @Bean
  AdditionalServiceDomain2JsonMapper additionalServiceDomain2JsonMapper(LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper) {
    return new AdditionalServiceDomain2JsonMapper(loadRuleDomain2JsonMapper);
  }

  @Bean
  LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper(DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper) {
    return new LoadRuleDomain2JsonMapper(dataExtractor2DataValueJsonMapper);
  }

  @Bean
  DataExtractor2DataValueJsonMapper dataExtractor2DataValueJsonMapper() {
    return new DataExtractor2DataValueJsonMapper();
  }
}
