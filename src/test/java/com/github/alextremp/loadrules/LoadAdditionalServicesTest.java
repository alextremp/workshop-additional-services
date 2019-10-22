package com.github.alextremp.loadrules;

import com.github.alextremp.additionalservices.application.dto.SiteDTO;
import com.github.alextremp.additionalservices.application.mapper.AdditionalServiceMapper;
import com.github.alextremp.additionalservices.application.mapper.DataExtractorMapper;
import com.github.alextremp.additionalservices.application.mapper.LoadRuleMapper;
import com.github.alextremp.additionalservices.application.mapper.SiteMapper;
import com.github.alextremp.additionalservices.domain.site.Site;
import com.github.alextremp.loadrules.parser.Converter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadAdditionalServicesTest {

    //https://app.quicktype.io/

    @Test
    public void from() throws Exception {
        DataExtractorMapper dataExtractorMapper = new DataExtractorMapper();
        LoadRuleMapper loadRuleMapper = new LoadRuleMapper(dataExtractorMapper);
        AdditionalServiceMapper additionalServiceMapper = new AdditionalServiceMapper(loadRuleMapper);
        SiteMapper siteMapper = new SiteMapper(additionalServiceMapper);
        InputStream jsonStream = getClass().getResourceAsStream("/sample/sample-01.json");
        String jsonString = IOUtils.toString(jsonStream);
        Converter converter = new Converter();
        SiteDTO siteDTO = converter.fromJsonString(jsonString);
        System.out.println("SiteDTO: " + siteDTO);

        Site site = siteMapper.fromDTO(siteDTO).block();
        System.out.println("Site: " + site);

        SiteDTO rebuiltDTO = siteMapper.fromDomain(site).block();
        System.out.println("Rebuilt DTO: " + rebuiltDTO);
        String rebuiltJsonString = converter.toJsonString(rebuiltDTO);
        assertEquals(jsonString, rebuiltJsonString);


    }
}