package com.example.gateway_service.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final WebClient webClient;
    private final GatewayWhitelistConfig config;
    private final String jwtUri;

    public JwtAuthenticationFilter(
            WebClient webClient,
            GatewayWhitelistConfig config,
            @Value("${jwt.uri}") String jwtUri) {
        this.webClient = webClient;
        this.config = config;
        this.jwtUri = jwtUri;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (config.getWhitelist().stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        return webClient.post()
                .uri(jwtUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toEntity(TokenValidationResponse.class)
                .flatMap(responseEntity -> {
                    HttpStatusCode status = responseEntity.getStatusCode();

                    // If not 2xx, propagate the status
                    if (!status.is2xxSuccessful()) {
                        exchange.getResponse().setStatusCode(status);
                        return exchange.getResponse().setComplete();
                    }

                    TokenValidationResponse body = responseEntity.getBody();

                    if (body == null || !body.isValid()) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // Inject claims into headers
                    ServerWebExchange newExchange = exchange.mutate()
                            .request(r -> r.headers(h -> {
                                h.add("X-User-Id", body.getUserId());
                                h.add("X-User-Roles", String.join(",", body.getRoles()));
                            }))
                            .build();

                    return chain.filter(newExchange);
                })
                .onErrorResume(e -> {
                    // Fallback → treat as auth server unavailable
                    exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    return exchange.getResponse().setComplete();
                });
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class TokenValidationResponse {
        private boolean valid;
        private String userId;
        private List<String> roles;
    }
}
