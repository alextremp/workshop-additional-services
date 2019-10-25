package com.github.alextremp.additionalservices.domain.site.error;

import com.github.alextremp.additionalservices.domain.error.EntityNotFoundError;

public class SiteNotFoundError extends EntityNotFoundError {
  public SiteNotFoundError(String id) {
    super("Site not found: " + id);
  }
}
