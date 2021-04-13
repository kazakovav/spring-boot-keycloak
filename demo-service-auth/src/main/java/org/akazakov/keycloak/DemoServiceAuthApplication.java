package org.akazakov.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoServiceAuthApplication implements CommandLineRunner {
    private static final String BEARER = "Bearer ";
    private static final String SERVICE_INFO_URL = "http://localhost:8080/api/service";

    private final KeycloakAuthClient keycloakAuthClient;

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory
            .getLogger(DemoServiceAuthApplication.class);

    public DemoServiceAuthApplication(KeycloakAuthClient keycloakAuthClient, RestTemplate restTemplate) {
        this.keycloakAuthClient = keycloakAuthClient;
        this.restTemplate = restTemplate;
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoServiceAuthApplication.class, args);
    }

    @Override
    public void run(String... args) {
        final KeycloakAuthResponse authenticate = keycloakAuthClient.authenticate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticate.getAccessToken());

        log.info("Make request to resource server");

        final ResponseEntity<String> responseEntity = restTemplate.exchange(SERVICE_INFO_URL, HttpMethod.GET, new HttpEntity(headers), String.class);

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to request");
            throw new RuntimeException("Failed to request");
        }

        log.info("Response data: {}", responseEntity.getBody());
    }
}
