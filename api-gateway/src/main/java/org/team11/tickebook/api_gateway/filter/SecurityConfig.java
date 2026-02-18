package org.team11.tickebook.api_gateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.team11.tickebook.api_gateway.util.JwtUtil;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public JwtWebFilter jwtWebFilter(JwtUtil jwtUtil) {
        return new JwtWebFilter(jwtUtil);
    }

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http, JwtWebFilter jwtWebFilter) {

        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(ex -> ex

                        // PUBLIC
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll()

                        // ADMIN
                        .pathMatchers(
                                "/api/theatre-approval-requests/**",
                                "/api/role-approval-requests/**",
                                "/api/admins/**"
                        ).hasRole("ADMIN")

                        // OWNER
                        .pathMatchers(
                                "/api/owner/**",
                                "/api/screens/**"
                        ).hasRole("THEATREOWNER")

                        // USER
                        .pathMatchers(
                                "/api/bookings/**",
                                "/api/tickets/**",
                                "/api/user/**"
                        ).hasAnyRole("USER","ADMIN")

                        // GENERAL AUTHENTICATED
                        .pathMatchers(
                                "/api/shows/**",
                                "/api/theatres/**",
                                "/api/seats/**"
                        ).authenticated()

                        //Movie Routes
                        .pathMatchers(HttpMethod.GET, "/api/movies/**").authenticated()
                        .pathMatchers(HttpMethod.POST,"/api/movies/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/movies/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/movies/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .build();
    }
}
