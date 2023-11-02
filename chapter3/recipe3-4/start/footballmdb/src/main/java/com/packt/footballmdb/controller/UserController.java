package com.packt.footballmdb.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballmdb.repository.User;
import com.packt.footballmdb.service.UserService;

@RequestMapping("/users")
@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}/buyTokens")
    public Integer buyTokens(@PathVariable String id, @RequestBody Integer tokens) {
        return userService.buyTokens(id, tokens);
    }

    @PostMapping("/{id}/buyCards")
    public Integer buyCards(@PathVariable String id, @RequestBody Integer count) {
        return userService.buyCards(id, count);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
}
