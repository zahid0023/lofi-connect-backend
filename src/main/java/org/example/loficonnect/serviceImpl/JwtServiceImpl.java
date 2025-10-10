package org.example.loficonnect.serviceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private SecretKey SIGNING_KEY;

    @PostConstruct
    public void init() {
        String SECRET_KEY = "v0Rp4Wdzf3zK4PVj7N91+n4FBjkD6SacKKctMzNg5Bw=";
        this.SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    @Override
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .signWith(SIGNING_KEY)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return (username.equals(tokenUsername)) &&
                   Jwts.parser().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token)
                           .getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
