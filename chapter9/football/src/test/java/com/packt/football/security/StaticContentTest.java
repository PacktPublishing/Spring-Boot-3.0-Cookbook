package com.packt.football.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.packt.football.configuration.SecurityConfiguration;
import com.packt.football.controller.FootballController;
import com.packt.football.service.FootballService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {FootballController.class})
@ContextConfiguration(classes = {SecurityConfiguration.class})
@AutoConfigureMockMvc
class StaticContentTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    FootballService footballService;

    @Test
    void loginOk() throws Exception {
        mvc.perform(formLogin().user("packt").password("packt"))
                .andExpect(status().isOk());
    }

    @Test
    void getPublicContent() throws Exception {
        mvc.perform(get("/public/sample.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string("this just a sample of public content"));
    }

    @Test
    @WithMockUser(username = "packt", roles = "ADMIN")
    void getPrivateContent() throws Exception {
        mvc.perform(get("/private/sample.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string("this just a sample of safe content"));
    }

    @Test
    @WithMockUser(username = "packt", roles = "USER")
    void getPrivateContent_fail() throws Exception {
        mvc.perform(get("/private/sample.txt"))
                .andExpect(status().is(401));
    }
}
