package com.packt.football.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getPublic() {
        ResponseEntity<String> response = restTemplate.getForEntity("/security/public", String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("this is a public content", response.getBody());
    }

    @Test
    void getPrivate_authorized() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("packt", "packt")
                .getForEntity("/security/private", String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("this is a private content", responseEntity.getBody());
    }

    @Test
    void getPrivate_forbidden() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("user1", "user1")
                .getForEntity("/security/private", String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void getPrivate_unauthorized() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/security/private", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}