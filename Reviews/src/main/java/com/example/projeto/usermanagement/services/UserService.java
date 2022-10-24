package com.example.projeto.usermanagement.services;

import javax.validation.ValidationException;

import com.example.projeto.usermanagement.models.User;
import com.example.projeto.usermanagement.repositories.UserRepository;
import com.example.projeto.usermanagement.api.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final UserEditMapper userEditMapper;
    private final UserViewMapper userViewMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserView create(final CreateUserRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("Username exists!");
        }

        User user = userEditMapper.create(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepo.save(user);

        return userViewMapper.toUserView(user);
    }

    @Transactional
    public UserView update(final Long id, final UpdateUserRequest request) {
        User user = userRepo.getById(id);
        userEditMapper.update(request, user);

        user = userRepo.save(user);

        return userViewMapper.toUserView(user);
    }

    @Transactional
    public UserView delete(final Long id) {
        User user = userRepo.getById(id);

        user.setUsername(user.getUsername().replace("@", String.format("_%s@", user.getUserId().toString())));
        user.setEnabled(false);
        user = userRepo.save(user);

        return userViewMapper.toUserView(user);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }

    public UserView getUser(final Long userId) {
        return userViewMapper.toUserView(userRepo.getById(userId));
    }

}
