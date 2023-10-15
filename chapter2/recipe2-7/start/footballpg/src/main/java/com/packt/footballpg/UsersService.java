package com.packt.footballpg;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private UserRepository usersRepository;

    public UsersService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User createUser(String name) {
        UserEntity user = new UserEntity();
        user.setUsername(name);
        user = usersRepository.save(user);
        return new User(user.getId(), user.getUsername());
    }

    public List<User> getUsers() {
        return usersRepository.findAll().stream()
                .map(user -> new User(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public User getUser(Integer id) {
        return usersRepository.findById(id)
                .map(u -> new User(u.getId(), u.getUsername()))
                .orElseThrow();
    }

}
