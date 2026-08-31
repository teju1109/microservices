package com.example.orderservice.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${user.service.base-url}")
    private String userServiceBaseUrl;

    @Bean
    public RestClient userRestClient() {
        return RestClient.builder()
                .baseUrl(userServiceBaseUrl)
                .requestInterceptor((request, body, execution) -> {

                    String correlationId = MDC.get("X-Correlation-ID");

                    if (correlationId != null) {
                        request.getHeaders()
                                .set("X-Correlation-ID", correlationId);
                    }

                    return execution.execute(request, body);
                })
                .build();
    }
}