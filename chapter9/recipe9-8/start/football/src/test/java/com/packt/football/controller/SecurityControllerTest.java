package com.packt.football.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

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
    void getPrivate_forbidden() throws Exception {
        mvc.perform(get("/security/private").with(user("user1").roles("USER")))
                .andExpect(status().is(403));
    }

    @Test
    void getPrivate_notAuthorized() throws Exception {
        mvc.perform(get("/security/private"))
                .andExpect(status().is(401));
    }

    @Test
    void getPublic_invaliduser() throws Exception {
        mvc.perform(get("/security/public").with(user("unknownuser").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void getPrivate_unauthorized_nouser() throws Exception {
        mvc.perform(get("/security/private"))
                .andExpect(status().is(401));
    }
}