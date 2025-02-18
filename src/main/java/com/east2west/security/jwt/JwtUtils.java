package com.east2west.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  private final Key key;
  private final int jwtExpirationMs;

  public JwtUtils(@Value("${bezkoder.app.jwtSecret}") String jwtSecret,
                  @Value("${bezkoder.app.jwtExpirationMs}") int jwtExpirationMs) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    this.jwtExpirationMs = jwtExpirationMs;

  }

  public String generateJwtToken(String username, String role,String password) {

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
    return Jwts.builder()
            .setSubject(username + ":" + role + ":" + password)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    String subject = Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().getSubject();
    return subject.split(":")[0];
  }

  public String getPasswordFromJwtToken(String token) {
    String subject = Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().getSubject();
    return subject.split(":")[2];
  }

  public String getRoleFromJwtToken(String token) {
    String subject = Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().getSubject();
    return subject.split(":")[1];
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", authToken, e);
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", authToken, e);
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", authToken, e);
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", authToken, e);
    }
    return false;
  }
}
