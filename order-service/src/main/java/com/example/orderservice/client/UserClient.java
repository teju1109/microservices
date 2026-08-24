package com.example.orderservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.retry.annotation.Retry;

import com.example.orderservice.dto.UserResponse;

@Component
public class UserClient {

    private final RestClient restClient;

    @Value("${user.service.base-url}")
    private String userServiceBaseUrl;

    public UserClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Retry(name = "userService")
    public UserResponse getUserById(Long userId) {

        return restClient
                .get()
                .uri(userServiceBaseUrl + "/users/{id}", userId)
                .retrieve()
                .body(UserResponse.class);
    }

    public UserResponse getSlowUserById(Long userId) {

        return restClient
                .get()
                .uri(userServiceBaseUrl + "/users/slow/{id}", userId)
                .retrieve()
                .body(UserResponse.class);
    }
}