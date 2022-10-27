package com.example.projeto.usermanagement.api;

import com.example.projeto.usermanagement.models.Role;
import com.example.projeto.usermanagement.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Tag(name = "Admin")
@RestController
@RequestMapping(path = "api/admin/user")
@RolesAllowed(Role.ADMIN)
@RequiredArgsConstructor
public class UserAdminApi {

    private final UserService userService;

    @PostMapping
    public UserView create(@RequestBody @Valid final CreateUserRequest request) {
        return userService.create(request);
    }

    @PutMapping("{id}")
    public UserView update(@PathVariable final Long id, @RequestBody @Valid final UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("{id}")
    public UserView delete(@PathVariable final Long id) {
        return userService.delete(id);
    }

    @GetMapping("{userId}")
    public UserView get(@PathVariable final Long userId) {
        return userService.getUser(userId);
    }
}
