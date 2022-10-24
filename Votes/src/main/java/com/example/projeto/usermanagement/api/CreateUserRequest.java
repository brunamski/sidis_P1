package com.example.projeto.usermanagement.api;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Data
public class CreateUserRequest {
    @NotBlank
    @Email
    String username;

    @NotBlank
    String password;


    Set<String> authorities = new HashSet<>();
}
