package com.accio.api.config;

import com.accio.api.repository.user.UserRepository;
import com.accio.api.service.ConfigService;
import com.accio.api.util.ValidationUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@ConfigurationPropertiesScan("com.accio.api.config")
@Slf4j
public class ApplicationConfig {

  private final UserRepository userRepository;
  private final ConfigService configService;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      username = username.trim();
      log.debug("Username {}", username);
      try {
        return (ValidationUtil.isValidEmail(username) ?
            userRepository.findByEmailIgnoreCase(username) :
            userRepository.findById(Integer.parseInt(username)))
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      } catch (NumberFormatException e) {
        throw new UsernameNotFoundException("User not found");
      }
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    var provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditorAwareImpl();
  }

  @Bean
  public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
    return p -> p.setFallbackPageable(Pageable.unpaged());
  }

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
            .addServersItem(new Server().url("http://localhost:8080"))
            .addServersItem(new Server().url("https://development.apipractikum.com"));
  }
}
