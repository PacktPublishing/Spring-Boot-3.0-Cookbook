package com.packt.football.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MBeansControllerTest {

    @MockBean
    MBeanServer mbeanServer;

    @Autowired
    TestRestTemplate testRestTemplate;
    @Test
    void listMBeans() throws MalformedObjectNameException {
        ObjectInstance instance = new ObjectInstance("java.lang:type=Memory", "java.lang.Memory");
        Set<ObjectInstance> mbeans = Set.of(instance);
        when(mbeanServer.queryMBeans(null, null)).thenReturn(mbeans);
        String[] beans = testRestTemplate.getForObject("/mbeans", String[].class);
        assertNotNull(beans);
        assertTrue(Arrays.asList(beans).contains("java.lang:type=Memory"));
    }
}