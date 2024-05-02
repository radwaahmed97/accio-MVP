package com.accio.api.service;

import com.accio.api.config.security.Authority;
import com.accio.api.entity.user.User;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtService {
  public static final String EXTRA_ROLES = "extra_roles";
  private static final String DEVELOPMENT_PREFIX = "token-";
  private static final String AUTHORIZATION = "Authorization";

  private final ConfigService configService;
  private final UserDetailsService userDetailsService;

  public static String getToken(StompHeaderAccessor accessor) {
    var headers = accessor.getNativeHeader(AUTHORIZATION);
    return getTokenFromHeaders(
        headers == null || headers.isEmpty() ? null : headers.get(0), null);
  }

  public static String getToken(HttpServletRequest request) {
    return getTokenFromHeaders(request.getHeader(AUTHORIZATION),
        request.getHeader(HttpHeaders.COOKIE));
  }

  private static String getTokenFromHeaders(String authorization, String cookie) {
    if (authorization != null && authorization.startsWith("Bearer ")) {
      return authorization.substring(7);
    }
    if (cookie != null) {
      for (var c : cookie.split(";")) {
        c = c.trim();
        if (c.startsWith("atip-token=")) {
          return c.substring("atip-token=".length());
        }
      }
    }
    return null;
  }

  /**
   * As string, because the JWT subject is a String.
   */
  public String getUserId(String jwt) {
    if (configService.isDevelopment() && jwt.startsWith(DEVELOPMENT_PREFIX)) {
      return jwt.substring(DEVELOPMENT_PREFIX.length());
    }
    try {
      return getClaims(jwt).getSubject();
    } catch (ClaimJwtException mje) {
      log.info("Not accepted JWT {}", jwt);
      return null;
    } catch (JwtException e) {
      log.info("Error", e);
      return null;
    }
  }

  public Claims getClaims(String jwt) {
    if (configService.isDevelopment() && jwt.startsWith(DEVELOPMENT_PREFIX)) {
      return new DefaultClaims(Map.of());
    }
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(jwt).getPayload();
  }

  public String createToken(UserDetails userDetails, int expiryInMinutes, Authority... extraRoles) {
    return Jwts.builder()
        .claims(Map.of(EXTRA_ROLES, extraRoles))
        .subject(userDetails.getUsername())
        .expiration(Date.from(Instant.now().plus(expiryInMinutes, MINUTES)))
        .signWith(getSigningKey())
        .compact();
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String userId, String jwt) {
    var userDetails = userDetailsService.loadUserByUsername(userId);
    if (isValid(jwt, userDetails)) {
      calculateUserAuthorities(jwt, (User) userDetails);
      return new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
    }
    return null;
  }

  public boolean isValid(String jwt, UserDetails userDetails) {
    if (configService.isDevelopment() && jwt.startsWith(DEVELOPMENT_PREFIX)) {
      return true;
    }
    var user = (User) userDetails;
    return String.valueOf(user.getId()).equals(getUserId(jwt)) && !isExpired(jwt);
  }

  private boolean isExpired(String jwt) {
    try {
      return getClaims(jwt).getExpiration().before(new Date());
    } catch (JwtException e) {
      log.info("Error", e);
      return true;
    }
  }

  private SecretKey getSigningKey() {
    var secret = configService.getSecret();
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public void calculateUserAuthorities(String jwt, User user) {
    var authorities = getUserCustomAuthorities(jwt);
    user.getAuthorities().addAll(authorities);
  }

  @SuppressWarnings("unchecked")
  public Set<GrantedAuthority> getUserCustomAuthorities(String jwt) {
    return (Set<GrantedAuthority>) Optional.ofNullable(getClaims(jwt).get(EXTRA_ROLES, List.class))
        .orElse(List.of())
        .stream().map(role -> new SimpleGrantedAuthority((String) role))
        .collect(Collectors.toSet());
  }
}
