package org.team11.tickebook.adminservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    // MUST match the key in Auth Service
    private final String SECRET_KEY = "very_secret_key_which_should_be_long";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        // 1. Find the "accessToken" Cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        // 2. If token exists & user not yet authenticated
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // 3. Parse Token
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                // --- FIX: Normalize the Role Prefix ---
                // If role is "ADMIN" -> becomes "ROLE_ADMIN"
                // If role is "ROLE_ADMIN" -> stays "ROLE_ADMIN"
                String finalRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                System.out.println("Authenticated User Role: " + finalRole);

                // 4. Create Authority (Do NOT add "ROLE_" here again)
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(finalRole);

                User principal = new User(username, "", Collections.singletonList(authority));

                // 5. Authenticate
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Token invalid? Ignore. The SecurityConfig will reject the request next.
                System.out.println("Admin Service: JWT Invalid or Expired");
            }
        }

        // 6. Continue
        filterChain.doFilter(request, response);
    }
}