package com.github.alextremp.loadrules;

import com.github.alextremp.loadrules.application.savesite.io.SiteDTO;
import com.github.alextremp.loadrules.parser.Converter;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

public class LoadAdditionalServicesTest {

  //https://app.quicktype.io/

  @Test
  public void from() throws Exception {
    InputStream jsonStream = getClass().getResourceAsStream("/sample/sample-01.json");
    String jsonString = IOUtils.toString(jsonStream);
    Converter converter = new Converter();
    SiteDTO site = converter.fromJsonString(jsonString);
    System.out.println("Site: " + site);
  }
}