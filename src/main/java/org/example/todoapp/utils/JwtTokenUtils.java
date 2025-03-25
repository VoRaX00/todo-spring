package org.example.todoapp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.todoapp.models.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public JwtTokenUtils(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.lifetime}") Duration lifetime) {
        this.secret = secret;
        this.lifetime = lifetime;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roles);

        if (userDetails instanceof UserDetailsImpl userDetailsImpl) {
            claims.put("email", userDetailsImpl.getEmail());
        }

        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + lifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(expiresAt)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public String getUsername(String token) {
        var claims = parseToken(token);
        return claims.getSubject();
    }

    public String getEmail(String token) {
        var claims = parseToken(token);
        return (String) claims.get("email");
    }

    public List<?> getRoles(String token) {
        var claims = parseToken(token);
        return claims.get("roles", List.class);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
