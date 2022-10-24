package com.example.projeto.auth.api;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Data
public class AuthRequest {
    @NotNull
    @Email
    String username;
    @NotNull
    String password;
}
