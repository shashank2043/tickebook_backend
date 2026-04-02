package org.team11.tickebook.api_gateway.controller;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class GlobalFallbackController {

    @RequestMapping("/fallback")
    public Mono<ResponseEntity<Map<String, Object>>> fallback(ServerWebExchange exchange) {

        // ✅ Get original request URL
        Set<URI> originalUris = exchange.getAttribute(
                ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR
        );

        String originalPath = "unknown";
        if (originalUris != null && !originalUris.isEmpty()) {
            originalPath = originalUris.iterator().next().getPath();
        }

        // ✅ Get route (service)
        Route route = exchange.getAttribute(
                ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR
        );
        String routeId = route != null ? route.getId() : "unknown";

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Service temporarily unavailable");
        response.put("service", routeId);
        response.put("path", originalPath);
        response.put("timestamp", System.currentTimeMillis());

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }
}
