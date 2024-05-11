package org.valerochka1337.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.valerochka1337.exceptions.JwtAuthenticationException;

@Component
public class JwtTokenProvider {
  private final UserDetailsService userDetailsService;

  @Value("${spring.security.jwt.secret}")
  private String secretKey;

  @Value("${spring.security.jwt.header}")
  private String authorizationHeader;

  @Value("${spring.security.jwt.expiration}")
  private Long validityMillis;

  public JwtTokenProvider(
      @Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String username) {
    Claims claims = Jwts.claims().subject(username).build();
    Date now = new Date();
    Date validity = new Date(now.getTime() + this.validityMillis);

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(validity)
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateToken(String token) throws JwtAuthenticationException {
    try {
      Jws<Claims> claimsJws =
          Jwts.parser().setSigningKey(getSigningKey()).build().parseSignedClaims(token);

      return !claimsJws.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      throw new JwtAuthenticationException(
          "Jwt token is expired or invalid", HttpStatus.UNAUTHORIZED);
    }
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public String resolveToken(HttpServletRequest request) {
    return request.getHeader(authorizationHeader);
  }

  private Key getSigningKey() {
    byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
