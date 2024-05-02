package com.accio.api.config.security;

import com.accio.api.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  public static final String SITE_USER = "SiteUser";
  private final AuthenticationProvider authenticationProvider;
  private final PasswordEncoder passwordEncoder;
  private final ConfigService configService;

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    builder.authenticationProvider(authenticationProvider);
    var siteUsername = configService.getSiteUsername();
    var sitePassword = configService.getSitePassword();
    builder.inMemoryAuthentication()
        .withUser(siteUsername)
        .password(passwordEncoder.encode(sitePassword))
        .roles(SITE_USER);
    return builder.build();
  }

  @Bean
  MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
    return new MvcRequestMatcher.Builder(introspector);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(m ->
            m.requestMatchers(mvc.pattern("/swagger-ui.html"),
                mvc.pattern("/swagger-ui/**"),
                mvc.pattern("/v3/api-docs.yaml"),
                mvc.pattern("/v3/api-docs/**")).hasRole(SITE_USER))

        .authorizeHttpRequests(m ->
            m.requestMatchers(mvc.pattern("/api/**")).permitAll())

        .authorizeHttpRequests(m -> m.anyRequest()
            .hasRole(SITE_USER))
        .httpBasic(Customizer.withDefaults())

        .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationManager(authenticationManager(httpSecurity));

    return httpSecurity.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
