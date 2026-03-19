package com.ecommerce.backend.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String userName = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .subject(userName)
                .claim("roles",roles)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUserName(String token){
        Claims claims = getAllClaims(token);
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token){
        Claims claims = getAllClaims(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails){
        Claims claims = getAllClaims(token);
        String userName = claims.getSubject();
        Date expiredDate = claims.getExpiration();
        return userName.equals(userDetails.getUsername()) && expiredDate.after(new Date());

    }
    public Date extractExpirationDate(String token){
        return getAllClaims(token).getExpiration();
    }

    private Claims getAllClaims(String token){

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
