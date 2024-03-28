package com.packt.football.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.football.domain.User;
import com.packt.football.service.UsersService;

@RequestMapping("/users")
@RestController
public class UserController {

    private UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<User> getUsers() {
        return usersService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(Integer id) {
        return usersService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody String name) {
        return usersService.createUser(name);
    }
}
