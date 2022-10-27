package com.example.projeto.usermanagement.api;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@Data
@AllArgsConstructor
public class UpdateUserRequest {
    Set<String> authorities;
}
