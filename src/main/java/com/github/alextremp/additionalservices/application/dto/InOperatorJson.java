package com.github.alextremp.additionalservices.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InOperatorJson {
  private DataValueJson data;
  private List<String> collection;
}
