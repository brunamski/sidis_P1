package com.example.projeto.usermanagement.api;

import java.util.List;

import com.example.projeto.usermanagement.models.User;
import com.example.projeto.usermanagement.repositories.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Mapper(componentModel = "spring")
public abstract class UserViewMapper {

    @Autowired
    private UserRepository userRepo;

    public abstract UserView toUserView(User user);

    public abstract List<UserView> toUserView(List<User> users);

    public UserView toUserViewById(final Long userId) {
        if (userId == null) {
            return null;
        }
        return toUserView(userRepo.getById(userId));
    }

}
