package com.example.projeto.usermanagement.models;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Value
@AllArgsConstructor
public class Role implements GrantedAuthority {


    public static final String REGISTERED = "REGISTERED";
    public static final String MODERATOR = "MODERATOR";
    public static final String ADMIN = "ADMIN";

    private String authority;
}
