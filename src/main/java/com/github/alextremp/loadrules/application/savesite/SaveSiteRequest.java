package com.github.alextremp.loadrules.application.savesite;

import com.github.alextremp.loadrules.application.savesite.io.SiteDTO;
import lombok.Data;

@Data
public class SaveSiteRequest {

  private final SiteDTO site;
}
