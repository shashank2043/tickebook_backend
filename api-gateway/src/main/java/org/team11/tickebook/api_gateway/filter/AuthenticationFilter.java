//package org.team11.tickebook.api_gateway.filter;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpCookie;
//import org.springframework.stereotype.Component;
//import org.team11.tickebook.api_gateway.util.JwtUtil;
//
//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
//
//    @Autowired
//    private RouteValidator validator;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            // 1. Check if the request is for a secured route
//            if (validator.isSecured.test(exchange.getRequest())) {
//
//                // 2. Extract the Cookie
//                HttpCookie authCookie = exchange.getRequest()
//                        .getCookies()
//                        .getFirst("accessToken");
//
//                if (authCookie == null) {
//                    // Ideally return HTTP 401 here, but exception works for now
//                    throw new RuntimeException("Missing Authorization Cookie");
//                }
//
//                String token = authCookie.getValue();
//
//                try {
//                    // 3. VALIDATE ONLY ("The Bouncer")
//                    // If signature is invalid or expired, this throws an exception
//                    jwtUtil.validateToken(token);
//
//                    // --- DELETED SECTIONS ---
//                    // We removed the code that extracts username/role.
//                    // We removed the code that mutates headers.
//                    // ------------------------
//
//                } catch (Exception e) {
//                    System.out.println("Invalid Access: " + e.getMessage());
//                    throw new RuntimeException("Unauthorized access to application");
//                }
//            }
//
//            // 4. Forward the request EXACTLY as is (Cookies are naturally included)
//            return chain.filter(exchange);
//        });
//    }
//
//    public static class Config { }
//}