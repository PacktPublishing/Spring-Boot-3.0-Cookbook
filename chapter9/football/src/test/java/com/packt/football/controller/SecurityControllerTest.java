package com.packt.football.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
class SecurityControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void getPublic() throws Exception {
        mvc.perform(get("/security/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("this is a public content"));
    }

    @Test
    void getPrivate_authorized() throws Exception {
        mvc.perform(get("/security/private").with(user("packt").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().string("this is a private content"));
    }

    @Test
    void getPrivate_notAuthorized1() throws Exception {
        mvc.perform(get("/security/private").with(user("user1").roles("USER")))
                .andExpect(status().is(403));
    }

    @Test
    void getPrivate_notAuthorized2() throws Exception {
        mvc.perform(get("/security/private"))
                .andExpect(status().is3xxRedirection());
    }
}