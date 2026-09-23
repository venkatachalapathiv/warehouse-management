package com.company.wms.security;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(props.secret()));
    }

    /** Access token: carries roles (ROLE_*) and scopes. */
    public String generateAccessToken(String subject, List<String> roles, List<String> scopes) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(props.issuer())
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(props.accessTokenTtl())))
                .claim("typ", "access")
                .claim("roles", roles)                       
                .claim("scope", String.join(" ", scopes))    
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /** Refresh token: only carries subject, used to mint new access tokens. */
    public String generateRefreshToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(props.issuer())
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(props.refreshTokenTtl())))
                .claim("typ", "refresh")
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /** Parse + verify signature + issuer + expiry. Throws JwtException on failure. */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(props.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}