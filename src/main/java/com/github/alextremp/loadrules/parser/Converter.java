package com.github.alextremp.loadrules.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.alextremp.loadrules.application.savesite.io.SiteDTO;

import java.io.IOException;

public class Converter {

  private final ObjectReader reader;
  private final ObjectWriter writer;

  public Converter() {
    ObjectMapper mapper = new ObjectMapper();
    reader = mapper.readerFor(SiteDTO.class);
    writer = mapper.writerFor(SiteDTO.class);
  }

  public SiteDTO fromJsonString(String json) throws IOException {
    return reader.readValue(json);
  }

  public String toJsonString(SiteDTO obj) throws JsonProcessingException {
    return writer.writeValueAsString(obj);
  }

}
