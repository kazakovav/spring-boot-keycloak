package org.akazakov.keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakAuthClient {
    private static final Logger log = LoggerFactory
            .getLogger(KeycloakAuthClient.class);

    private static final String TOKEN_PATH = "/token";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    public static final String CLIENT_CREDENTIALS = "client_credentials";

    @Value("${app.keycloak.auth-url:http://localhost:8484/auth/realms/my_realm/protocol/openid-connect}")
    private String authUrl;

    @Value("${app.keycloak.client-id:service_client}")
    private String clientId;

    @Value("${app.keycloak.client-secret:acb719cf-4afd-42d3-91f2-93a60b3f2023}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public KeycloakAuthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KeycloakAuthResponse authenticate() {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(CLIENT_ID, clientId);
        paramMap.add(CLIENT_SECRET, clientSecret);
        paramMap.add(GRANT_TYPE, CLIENT_CREDENTIALS);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = authUrl + TOKEN_PATH;

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

        log.info("Try to authenticate");

        ResponseEntity<KeycloakAuthResponse> response =
                restTemplate.exchange(url,
                        HttpMethod.POST,
                        entity,
                        KeycloakAuthResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to authenticate");
            throw new RuntimeException("Failed to authenticate");
        }

        log.info("Authentication success");

        return response.getBody();
    }
}
