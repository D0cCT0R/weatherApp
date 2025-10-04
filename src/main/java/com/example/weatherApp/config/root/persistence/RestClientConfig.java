package com.example.weatherApp.config.root.persistence;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {

    @Value("${weather.api.base-url}")
    private String baseUrl;

    @Bean
    public RestClient weatherRestClient(ObjectMapper objectMapper) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .messageConverters(converters -> {
                    converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
                    converters.add(new StringHttpMessageConverter());
                })
                .build();
    }
}

