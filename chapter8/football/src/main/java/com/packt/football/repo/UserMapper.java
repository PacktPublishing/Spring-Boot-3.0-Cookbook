package com.packt.football.repo;

import com.packt.football.domain.User;

public class UserMapper {
    public static User map(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername());
    }

}
