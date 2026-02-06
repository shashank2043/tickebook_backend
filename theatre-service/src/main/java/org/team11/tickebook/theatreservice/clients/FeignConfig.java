package org.team11.tickebook.theatreservice.clients;

import feign.RequestInterceptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor authForwardInterceptor() {
        return requestTemplate -> {

            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs == null) return;

            HttpServletRequest request = attrs.getRequest();

            if (request.getCookies() == null) return;

            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {

                    String jwt = cookie.getValue();

                    requestTemplate.header(
                            "Authorization",
                            "Bearer " + jwt
                    );

                    break;
                }
            }
        };
    }
}
