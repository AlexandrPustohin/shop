package com.example.shop.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    static final long EXPIRATIONTIME = 86400000; //1 день в мс

    static final String PREFIX = "Shop";

    // Генерация секретного ключа. Only for the demonstration
    // You should read it from the application configuration
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Генерация токена
    public String getToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()
                        + EXPIRATIONTIME))
                .signWith(key)
                .compact();
        return token;
    }


    // Получаем токен из заголовка запроса авторизации
    // проверяем токен и получаем пользователя
    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader (HttpHeaders.AUTHORIZATION);
        if (token != null) {
            String user = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();
            if (user != null)
                return user;
        }
        return null;
    }

}
