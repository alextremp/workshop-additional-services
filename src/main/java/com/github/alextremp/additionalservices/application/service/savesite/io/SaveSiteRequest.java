package com.github.alextremp.additionalservices.application.service.savesite.io;

import com.github.alextremp.additionalservices.application.dto.SiteJson;
import lombok.Data;

@Data
public class SaveSiteRequest {

  private final SiteJson site;
}
