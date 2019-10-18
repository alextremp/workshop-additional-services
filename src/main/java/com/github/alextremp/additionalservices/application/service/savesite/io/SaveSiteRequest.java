package com.github.alextremp.additionalservices.application.service.savesite.io;

import com.github.alextremp.additionalservices.application.dto.SiteDTO;
import lombok.Data;

@Data
public class SaveSiteRequest {

  private final SiteDTO site;
}
