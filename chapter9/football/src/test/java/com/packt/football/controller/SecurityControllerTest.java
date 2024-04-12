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
    void getPrivate_authorized() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("packt", "packt")
                .getForEntity("/security/private", String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("this is a private content", responseEntity.getBody());
    }

    @Test
    void getPrivate_forbidden() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("user1", "user1")
                .getForEntity("/security/private", String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void getPublic_invaliduser() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("unknownuser", "invalidpassword")
                .getForEntity("/security/public", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getPrivate_unauthorized_invaliduser() {
        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth("unknownuser", "invalidpassword")
                .getForEntity("/security/private", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void getPrivate_unauthorized_nouser() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/security/private", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}

// package com.packt.football.controller;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.junit.jupiter.api.Assertions.*;
// import static
// org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(SecurityController.class)
// class SecurityControllerTest {

// @Autowired
// MockMvc mvc;

// @Test
// void getPublic() throws Exception {
// mvc.perform(get("/security/public"))
// .andExpect(status().isOk())
// .andExpect(content().string("this is a public content"));
// }

// @Test
// void getPrivate_authorized() throws Exception {
// mvc.perform(get("/security/private").with(user("packt").roles("ADMIN")))
// .andExpect(status().isOk())
// .andExpect(content().string("this is a private content"));
// }

// @Test
// void getPrivate_forbidden() throws Exception {
// mvc.perform(get("/security/private").with(user("user1").roles("USER")))
// .andExpect(status().is(403));
// }

// @Test
// void getPrivate_notAuthorized() throws Exception {
// mvc.perform(get("/security/private"))
// .andExpect(status().is(401));
// }
// }