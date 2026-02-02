package org.team11.tickebook.api_gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.team11.tickebook.api_gateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // 1. Check if the request is for a secured route (ignore /auth/login etc.)
            if (validator.isSecured.test(exchange.getRequest())) {

                // 2. Extract the Cookie named "accessToken"
                HttpCookie authCookie = exchange.getRequest()
                        .getCookies()
                        .getFirst("accessToken");

                if (authCookie == null) {
                    throw new RuntimeException("Missing Authorization Cookie");
                }

                String token = authCookie.getValue();

                try {
                    // 3. Validate Token
                    jwtUtil.validateToken(token);

                    // 4. Extract Claims
                    var claims = jwtUtil.getAllClaimsFromToken(token);
                    String username = claims.getSubject();
                    String role = claims.get("role", String.class);

                    // 5. Mutate Request - Forward info to Microservices as HEADERS
                    ServerHttpRequest request = exchange.getRequest()
                            .mutate()
                            .header("X-Auth-User-Email", username)
                            .header("X-Auth-Role", role)
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());

                } catch (Exception e) {
                    System.out.println("Invalid Access: " + e.getMessage());
                    throw new RuntimeException("Unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config { }
}