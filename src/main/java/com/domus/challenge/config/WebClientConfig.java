package com.domus.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient moviesApiClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://challenge.iugolabs.com/api/movies")
                .build();
    }

}
