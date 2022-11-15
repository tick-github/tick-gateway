package com.tick.gateway;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    // Credits to Ihor Kosandyak over at https://www.linkedin.com/in/ihor-kosandiak-401333b0/
    // for the below implementation.

    public static final List<String> openApiEndpoints = List.of(
            "api/v1/settings/ping"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
