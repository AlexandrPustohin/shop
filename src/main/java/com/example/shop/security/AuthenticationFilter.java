package com.example.shop.security;

import com.example.shop.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest  request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)  throws ServletException, java.io.IOException {
        // Get token from the Authorization header
        String jws = request.getHeader  (HttpHeaders.AUTHORIZATION);
        if (jws != null) {
        // Verify token and get user
            String user = jwtService.getAuthUser(request);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                                                             java.util.Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
