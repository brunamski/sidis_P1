package com.example.projeto;

import com.example.projeto.usermanagement.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class UserTests {

    @Test
    void ensureUsernameMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                User user = new User("","banana");
            }
        });
    }

    @Test
    void ensureUsernameMustNotBeBlankSpaces() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                User user = new User("    ","banana");
            }
        });
    }

    @Test
    void ensurePasswordMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                User user = new User("diogo","");
            }
        });
    }

    @Test
    void ensurePasswordMustNotBeBlankSpaces() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                User user = new User("diogo","    ");
            }
        });
    }

    }