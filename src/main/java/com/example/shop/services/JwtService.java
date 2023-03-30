package com.example.shop.services;

import com.example.shop.model.ShopUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;


@Component
public class JwtService {
    static final long EXPIRATIONTIME = 86400000; //1 день в мс

    static final String PREFIX = "Bearer";
    private String jwtCookie = "Shop";

    // Генерация секретного ключа. Only for the demonstration
    // You should read it from the application configuration
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private String jwtSecret = "Shop";

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
            if (user != null) {
                return user;
            }
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {

            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken.split(" ")[1].trim());
            return true;
        } catch (MalformedJwtException e) {
            System.out.printf("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.printf("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.printf("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.printf("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }


    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(ShopUser userPrincipal) {
        String jwt = getToken(userPrincipal.getUserName());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }
}
