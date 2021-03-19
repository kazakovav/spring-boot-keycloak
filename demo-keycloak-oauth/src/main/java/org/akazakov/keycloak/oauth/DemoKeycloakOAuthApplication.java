package org.akazakov.keycloak.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;

@SpringBootApplication
public class DemoKeycloakOAuthApplication {


	public static void main(String[] args) {
		SpringApplication.run(DemoKeycloakOAuthApplication.class, args);
	}

}
