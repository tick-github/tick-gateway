package com.tick.gateway;

import com.auth0.jwt.interfaces.Claim;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RefreshScope
@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

    private RouterValidator routerValidator;
    private JwtUtility jwtUtility;
    private static final String GOOGLE_JWK_URI = "https://www.googleapis.com/oauth2/v3/certs";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                return this.onError(
                        exchange, "Authorization header is missing in request", HttpStatus.FORBIDDEN
                );
            }

            final String jwt = this.getAuthHeader(request);

            if (!jwtUtility.verify(jwt, GOOGLE_JWK_URI)) {
                return this.onError(
                        exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED
                );
            }

            this.populateRequestWithHeaders(exchange, jwt);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();

    }

    private String getAuthHeader(ServerHttpRequest request) {

        return request.getHeaders().getOrEmpty("Authorization").get(0);

    }

    private boolean isAuthMissing(ServerHttpRequest request) {

        return !request.getHeaders().containsKey("Authorization");

    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {

        Map<String, Claim> claims = jwtUtility.getClaims(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("sub")).replaceAll("\"", ""))
                .header("name", String.valueOf(claims.get("given_name")))
                .build();

    }

}
