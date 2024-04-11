package com.packt.football.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/security")
@RestController
public class SecurityController {

    @GetMapping("/public")
    public String getPublic() {
        return "this is a public content";
    }

    @GetMapping("/private")
    public String getPrivate() {
        return "this is a private content";
    }
}
