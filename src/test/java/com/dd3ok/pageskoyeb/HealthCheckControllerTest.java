package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
}
