package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.data.mongodb.uri=mongodb://127.0.0.1:27017/pages_koyeb_test?serverSelectionTimeoutMS=250"
    }
)
class HealthCheckControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void healthcheckDoesNotRequireDatabaseConnection() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/healthcheck", String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"status\":\"ok\"");
    }

    @Test
    void unusedHomeContactReadEndpointsAreNotExposed() {
        ResponseEntity<String> listResponse = restTemplate.getForEntity("/api/home/contacts", String.class);
        ResponseEntity<String> emailResponse = restTemplate.getForEntity(
            "/api/home/contacts/email/kim@example.com",
            String.class
        );

        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(emailResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void weddingApiIsNotExposedByDefault() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/wedding/comments", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void pagesOriginCanPreflightContactCreate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("https://dd3ok.github.io");
        headers.setAccessControlRequestMethod(HttpMethod.POST);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/home/contacts",
            HttpMethod.OPTIONS,
            new HttpEntity<>(headers),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getAccessControlAllowOrigin()).isEqualTo("https://dd3ok.github.io");
        assertThat(response.getHeaders().getAccessControlAllowMethods()).contains(HttpMethod.POST);
    }
}
