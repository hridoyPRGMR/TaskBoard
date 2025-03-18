package com.api_gateway.security;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {

    private static final String INTERNAL_AUTH_HEADER = "X-Internal-Auth";
    private final JwtService jwtService;
    private final ReactiveUserDetailsService reactiveUserDetailsService;

    public JwtAuthFilter(JwtService jwtService, ReactiveUserDetailsService reactiveUserDetailsService) {
        this.jwtService = jwtService;
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        try {
        	if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);

                return reactiveUserDetailsService.findByUsername(username)
                    .flatMap(userDetails -> authenticateUser(userDetails, token, exchange, chain))
                    .switchIfEmpty(unauthorized(exchange));
            }
        }
        catch(Exception e) {
        	return unauthorized(exchange);
        }

        // No token found, continue without authentication
        return chain.filter(exchange);
    }

    private Mono<Void> authenticateUser(UserDetails userDetails, String token, ServerWebExchange exchange, WebFilterChain chain) {
        if (!jwtService.validateToken(token, userDetails.getUsername())) {
            System.out.println("⛔ JWT Validation Failed");
            return unauthorized(exchange);
        }

        // ✅ Set authentication context
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Generate internal token for microservices communication
        String internalToken = jwtService.generateInternalToken(userDetails.getUsername());

        // 🔹 Mutate the request to add the internal token header
        ServerWebExchange mutatedExchange = exchange.mutate()
            .request(r -> r.headers(headers -> headers.add(INTERNAL_AUTH_HEADER, "Bearer " + internalToken)))
            .build();

        // 🔹 Attach authentication to Security Context
        return chain.filter(mutatedExchange)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> errorResponse = Map.of(
            "status", "401",
            "error", "Unauthorized",
            "message", "Invalid or expired JWT token",
            "path", exchange.getRequest().getPath().toString()
        );

        try {
            byte[] bytes = new ObjectMapper().writeValueAsBytes(errorResponse);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return response.setComplete();
        }
    }
}
