package com.torrent.torrentApplication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "TorrentApplicationSecretKey12345678901234567890";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    private static final long EXPIRATION =
            1000 * 60 * 60 * 24;

    public String generateToken(String email){

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + EXPIRATION)
                )
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token){

        Claims claims =
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token){

        try{

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        }catch(Exception e){

            return false;
        }
    }
}
