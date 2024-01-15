package com.restapiblog.restapiblog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


@Component
public class JwtUtils {

    // 1st step is creating the getKey and expirationTime and Map our claims
    private Supplier <SecretKeySpec> getKey = () -> {
       Key key = Keys.hmacShaKeyFor("6a1670d6e14042e88091de0b46adaae710e7490c45fc188d3ba173e9d1b137b2bf36a6173c087587cb073f380fd4aa7bb99f71c288843acc8a07a8404f5ffd03"
               .getBytes(StandardCharsets.UTF_8));
       return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    };

    private Supplier<Date> expirationTime = () ->
            Date.from(LocalDateTime.now()
                    .plusMinutes(10)
                    .atZone(ZoneId.systemDefault())
                    .toInstant());

    public Function<UserDetails, String> createJwt = (UserDetails) -> {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .signWith(getKey.get())
                .claims(claims)
                .subject(UserDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationTime.get())
                .compact();
    };

    // 2nd step is to extract userName and expirationDate from token

    // parser helps us pass through payload (claims of the token) the claims are the user info.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = Jwts.parser().verifyWith(getKey.get()).build().parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    public Function<String, String> extractUsername = (token) -> extractClaim(token, Claims::getSubject);

    public Function<String, Date> extractExpirationTime = (token) -> extractClaim(token, Claims::getExpiration);

    //3rd step create function to check the token is still valid or not yet
      // it takes in token and return true or false
    public Function<String, Boolean> isTokenExpired = (token) -> extractExpirationTime.apply(token).after(new Date(System.currentTimeMillis()));

    // BiFunction receives two statements and return one
    //  the first String is the token while the second is the username then the boolean validates if they're true or false
    public BiFunction<String, String, Boolean> isTokenValid = (token, username) -> isTokenExpired.apply(token) &&
            Objects.equals(extractUsername.apply(token), username);
}
