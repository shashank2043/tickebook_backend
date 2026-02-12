package org.team11.tickebook.api_gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.team11.tickebook.api_gateway.util.JwtUtil;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtWebFilter implements WebFilter {


    private final JwtUtil jwtUtil;

    public JwtWebFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth/") || path.startsWith("/eureka")) {
            return chain.filter(exchange);
        }

        String token = null;

        // 1. CHECK HEADER FIRST
        String authHeader =
                exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. FALLBACK TO COOKIE
        if (token == null) {
            HttpCookie cookie =
                    exchange.getRequest().getCookies().getFirst("accessToken");

            if (cookie != null) {
                token = cookie.getValue();
            }
        }

        // 3. IF STILL NULL → 401
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            jwtUtil.validateToken(token);

            List<String> roles = jwtUtil.extractRoles(token);
            if (roles == null) roles = List.of();

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                            "user",
                            null,
                            roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );

            return chain.filter(exchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(auth)
                    );

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
