package com.packt.football.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MBeansControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Test
    void listMBeans() {
        String beans = testRestTemplate.getForObject("/mbeans", String.class);
        assertNotNull(beans);
        assertTrue(beans.contains("java.lang:type=Memory"));
    }
}