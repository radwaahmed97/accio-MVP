package com.accio.api.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ConfigService {
  @Value("${accio.secret}")
  private String secret;
  @Value("${accio.activation-secret}")
  private String secretKey;
  @Value("${accio.development:false}")
  private boolean development;
  @Value("${accio.site-username}")
  private String siteUsername;
  @Value("${accio.site-password:}")
  private String sitePassword;
}
