package com.github.alextremp.additionalservices.application.service.getsite.io;

import com.github.alextremp.additionalservices.application.dto.SiteJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSiteResponse {
  private SiteJson site;
}
