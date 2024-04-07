package com.packt.football.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MBeansController.class)
@AutoConfigureMockMvc
class MBeansControllerTest {

    @MockBean
    MBeanServer mbeanServer;

    @Autowired
    MockMvc mvc;

    @Test
    void listMBeans() throws Exception {
        ObjectInstance instance = new ObjectInstance("java.lang:type=Memory", "java.lang.Memory");
        Set<ObjectInstance> mbeans = Set.of(instance);
        when(mbeanServer.queryMBeans(null, null)).thenReturn(mbeans);
        MvcResult result = mvc.perform(get("/mbeans"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
        ObjectMapper mapper = new ObjectMapper();
        String[] beans = mapper.readValue(content, String[].class);
        assertNotNull(beans);
        assertTrue(Arrays.asList(beans).contains("java.lang:type=Memory"));
    }
}