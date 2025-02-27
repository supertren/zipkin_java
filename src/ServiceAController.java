package com.example.servicea;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RestController
@RequestMapping("/service-a")
public class ServiceAController {
    private final RestTemplate restTemplate;

    public ServiceAController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @GetMapping("/call-service-b")
    public String callServiceB() {
        return restTemplate.getForObject("http://localhost:8081/service-b/hello", String.class);
    }
}

@Configuration
class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
