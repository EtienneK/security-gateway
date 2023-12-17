package com.etiennek.hac.gateway.security.authz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OAuth2AttributeGatewayFilterFactoryTest {
    @Autowired
    ApplicationContext applicationContext;

    WebTestClient webClient;

    @BeforeEach
    void beforeEach() {
        webClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @Test
    @WithMockUser
    void Should_fail_with_BAD_REQUEST_if_not_an_OAuth2_User() {
        webClient
                .get().uri("/")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void Should_fail_with_FORBIDDEN_if_attributes_dont_match() {
        webClient
                .mutateWith(mockOAuth2Login().attributes(attr -> attr.put("id", "WRONGVALUE")))
                .get().uri("/")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void Should_succeed_with_200_OK_if_attributes_match() {
        webClient
                .mutateWith(mockOAuth2Login().attributes(attr -> attr.put("id", "5174409")))
                .get().uri("/")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

    @Test
    void Should_succeed_with_200_OK_if_attributes_match_and_attribute_value_is_an_integer() {
        webClient
                .mutateWith(mockOAuth2Login().attributes(attr -> attr.put("id", 5174409)))
                .get().uri("/")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

}
