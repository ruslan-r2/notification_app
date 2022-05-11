package ru.notifier.WebApp.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    static final String URL_SERVICE = "https://probe.fbrq.cloud/v1/send/";
    static final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE5ODYwMjQsImlzcyI6ImZhYnJpcXVlIiwibmFtZSI6IlJ1c2xhblJlcGluIn0.H04kuBNVmf2OtQ-mSZ5c5xexOFD5Aki7ssDKVydLxDw";

    @Bean(name = "messageRestTemplate")
    public RestTemplate messageRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("URL_SERVICE")
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    request.getHeaders().add("Authorization", "Bearer " + token);
                    return execution.execute(request, body);
                }).build();
    }


}
