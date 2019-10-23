package com.github.alextremp.loadrules;

import com.github.alextremp.additionalservices.application.dto.SiteJson;
import com.github.alextremp.additionalservices.application.mapper.*;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.loadrules.parser.Converter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {

    //https://app.quicktype.io/

    @Test
    public void from() throws Exception {
        DataExtractorMapper dataExtractorMapper = new DataExtractorMapper();
        LoadRuleJson2DomainMapper loadRuleMapper = new LoadRuleJson2DomainMapper(dataExtractorMapper);
        LoadRuleDomain2JsonMapper loadRuleDomain2JsonMapper = new LoadRuleDomain2JsonMapper();
        AdditionalServiceJson2DomainMapper additionalServiceToDTOMapper = new AdditionalServiceJson2DomainMapper(loadRuleMapper);
        AdditionalServiceDomain2JsonMapper additionalServiceToDomainMapper = new AdditionalServiceDomain2JsonMapper(loadRuleDomain2JsonMapper);
        SiteJson2DomainMapper siteToDomainMapper = new SiteJson2DomainMapper(additionalServiceToDTOMapper);
        SiteDomain2JsonMapper siteToDTOMapper = new SiteDomain2JsonMapper(additionalServiceToDomainMapper);
        InputStream jsonStream = getClass().getResourceAsStream("/sample/sample-01.json");
        String jsonString = IOUtils.toString(jsonStream);
        Converter converter = new Converter();

        SiteJson siteJson = converter.fromJsonString(jsonString);
        System.out.println("SiteDTO: " + siteJson);

        Site site = siteToDomainMapper.map(siteJson).block();
        System.out.println("Site: " + site);

        SiteJson rebuiltDTO = siteToDTOMapper.map(site).block();
        assertEquals(siteJson, rebuiltDTO);


    }
}