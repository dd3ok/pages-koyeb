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
import org.springframework.http.MediaType;
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

    @Test
    void pagesOriginCanPreflightWeddingUpdateAndDelete() {
        assertPreflightAllows("/api/wedding/comments/1", HttpMethod.PUT);
        assertPreflightAllows("/api/wedding/comments/1", HttpMethod.DELETE);
    }

    @Test
    void invalidContactRequestReturnsBadRequestJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
            {
              "name": "",
              "email": "kim@example.com",
              "message": "hello"
            }
            """;

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/home/contacts",
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)).isTrue();
        assertThat(response.getBody())
            .contains("\"status\":400")
            .contains("\"error\":\"Bad Request\"")
            .contains("\"path\":\"/api/home/contacts\"");
    }

    private void assertPreflightAllows(String path, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.setOrigin("https://dd3ok.github.io");
        headers.setAccessControlRequestMethod(method);

        ResponseEntity<String> response = restTemplate.exchange(
            path,
            HttpMethod.OPTIONS,
            new HttpEntity<>(headers),
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getAccessControlAllowOrigin()).isEqualTo("https://dd3ok.github.io");
        assertThat(response.getHeaders().getAccessControlAllowMethods()).contains(method);
    }
}
