package org.valerochka1337.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.valerochka1337.exceptions.JwtAuthenticationException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
  private final JwtTokenProvider jwtTokenProvider;

  private final HandlerExceptionResolver handlerExceptionResolver;

  public JwtTokenFilter(
      JwtTokenProvider jwtTokenProvider,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        if (authentication != null) {
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
      
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (Exception exception) {
      SecurityContextHolder.clearContext();
      handlerExceptionResolver.resolveException(
          (HttpServletRequest) servletRequest,
          (HttpServletResponse) servletResponse,
          null,
          exception);
    }
  }
}
