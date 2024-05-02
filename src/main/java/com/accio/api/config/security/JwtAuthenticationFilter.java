package com.accio.api.config.security;

import com.accio.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;


  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
    var jwt = JwtService.getToken(request);
    if (jwt != null) {
      var userId = jwtService.getUserId(jwt);
      if (userId != null) {
        var securityContext = SecurityContextHolder.getContext();
        var previousAuthentication = securityContext.getAuthentication();
        try {
          var authentication = jwtService.getAuthentication(userId, jwt);
          if (authentication != null) {
            if (previousAuthentication instanceof UsernamePasswordAuthenticationToken previousAuth) {
              authentication = merge(authentication, previousAuth);
            }
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authentication);
          }
        } catch (UsernameNotFoundException e) {
          if (previousAuthentication == null) {
            throw e;
          }
        }
      }
    }
    filterChain.doFilter(request, response);
  }

  private static UsernamePasswordAuthenticationToken merge(UsernamePasswordAuthenticationToken jwtToken,
                                                           UsernamePasswordAuthenticationToken basicToken) {
    var combined = new ArrayList<>(basicToken.getAuthorities());
    combined.addAll(jwtToken.getAuthorities());
    return new UsernamePasswordAuthenticationToken(
        jwtToken.getPrincipal(), jwtToken.getCredentials(), combined);
  }
}
