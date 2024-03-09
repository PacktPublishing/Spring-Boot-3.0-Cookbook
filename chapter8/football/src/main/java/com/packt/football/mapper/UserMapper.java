package com.packt.football.mapper;

import com.packt.football.domain.User;
import com.packt.football.repo.UserEntity;

public class UserMapper {
    public static User map(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername());
    }
}
